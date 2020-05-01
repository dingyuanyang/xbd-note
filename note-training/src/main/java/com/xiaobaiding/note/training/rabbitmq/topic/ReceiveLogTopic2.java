package com.xiaobaiding.note.training.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogTopic2 {
    private final static String LOG_TOPIC_EXCHANGE = "LOGS_TOPIC";

    /**
     * 速度.颜色.动物
     */
    private final static String[] TOPIC_KEYS = new String[]{
            "*.*.rabbit",
            "lazy.#",
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
        channel.exchangeDeclare(LOG_TOPIC_EXCHANGE, "topic");
        String queue = channel.queueDeclare().getQueue();
        /**
         * 5 - 绑定队列类型为topic
         */
        channel.queueBind(queue, LOG_TOPIC_EXCHANGE, "*.*.rabbit");
        channel.queueBind(queue, LOG_TOPIC_EXCHANGE,  "lazy.#");
        System.out.println("Q2：队列准备接收消息！");
        /**
         * 6 - 收取队列信息
         */
        DeliverCallback callback = (tag, delivery) -> {
            String animal = new String(delivery.getBody(), "utf-8");
            System.out.println("动物信息:" + delivery.getEnvelope().getRoutingKey() + ":" + animal);
        };
        channel.basicConsume(queue, true, callback, tag -> {
        });
    }
}
