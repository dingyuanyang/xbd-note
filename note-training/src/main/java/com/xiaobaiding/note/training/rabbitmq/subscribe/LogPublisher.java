package com.xiaobaiding.note.training.rabbitmq.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class LogPublisher {

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
        System.out.println(LOG_EXCHANGE + "-->交换器准备发送消息");
        /**
         * 5 - 发送任务
         */
        for (int i = 0; i < 10; i++) {
            String log = "日志：" + (i);
            channel.basicPublish(LOG_EXCHANGE, "", null, log.getBytes());
            System.out.println("发布日志成功：" + log);
        }
        System.out.println("日志发送完成");
        channel.close();
        connection.close();
    }
}
