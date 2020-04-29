package com.xiaobaiding.note.training_mq_04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogDirect2 {

    private final static String LOG_DIRECT_EXCHANGE = "LOGS_DIRECT";

    private final static String[] ROUTING_KEYS = new String[]{
           "info"
    };

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
        channel.exchangeDeclare(LOG_DIRECT_EXCHANGE, "direct");
        String queue = channel.queueDeclare().getQueue();
        /**
         * 5 - 绑定队列类型为info
         */
        for (String key : ROUTING_KEYS) {
            channel.queueBind(queue, LOG_DIRECT_EXCHANGE, key);
        }
        System.out.println(ROUTING_KEYS.toString()+"队列准备接收消息，info");
        /**
         * 6 - 收取队列信息
         */
        DeliverCallback callback = (tag, delivery) -> {
            String log = new String(delivery.getBody(), "utf-8");
            System.out.println("日志信息收到:" + delivery.getEnvelope().getRoutingKey() + ":" + log);
        };
        channel.basicConsume(queue, true, callback, tag -> {
        });
    }
}
