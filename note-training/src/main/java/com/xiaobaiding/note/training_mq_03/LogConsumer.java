package com.xiaobaiding.note.training_mq_03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class LogConsumer {


    private final static String LOG_EXCHANGE = "LOGS";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 1 - 设置RabbitMQ连接工厂信息
         */
        String host = "192.168.0.87";
        String username = "admin";
        String password = "admin";
        Integer port = 5672;
        ConnectionFactory factory = new ConnectionFactory();
        // 连接地址DEFAULT_HOST = "localhost"
        factory.setHost(host);
        //设置访问用户，default=guest，rabbitmq在3.3以后禁止使用guest操作
        factory.setUsername(username);
        //设置访问用户密码，default=guest，rabbitmq在3.3以后禁止使用guest操作
        factory.setPassword(password);
        factory.setPort(port);
        /**
         * 2 - 新建连接信息
         */
        Connection connection = factory.newConnection();

        /**
         * 3 - 创建消息通道
         */
        Channel channel = connection.createChannel();

        /**
         * 4 - 将接收到的所有消息广播给它知道的所有队列
         */
        channel.exchangeDeclare(LOG_EXCHANGE, "fanout");
        /**
         * 5 - 通过系统获取一个临时的，独特的，自动删除的队列
         */
        String queueName = channel.queueDeclare().getQueue();

        /**
         * 6 - 绑定临时生成的队列和转换器
         */
        channel.queueBind(queueName, LOG_EXCHANGE, "");
        System.out.println("准备接收转换器消息：" + LOG_EXCHANGE);

        /**
         * 7 - 接收消息
         */
        DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf-8");
            System.out.println("当前日志消费者已经接收到消息：" + msg);
        });
        channel.basicConsume(queueName, true, deliverCallback, k -> {
        });
    }
}
