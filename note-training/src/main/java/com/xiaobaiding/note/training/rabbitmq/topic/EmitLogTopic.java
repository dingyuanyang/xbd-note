package com.xiaobaiding.note.training.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {

    private final static String LOG_TOPIC_EXCHANGE = "LOGS_TOPIC";

    /**
     * 速度.颜色.动物
     */
    private final static String[] TOPIC_KEYS = new String[]{
            "quick.orange.rabbit",
            "lazy.orange.elephant",
            "quick.orange.fox",
            "lazy.brown.fox",
            "lazy.pink.rabbit",
            "quick.brown.fox",
            "lazy.orange.elephant",
            "lazy.orange.male.rabbit"
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
        System.out.println(LOG_TOPIC_EXCHANGE + "-->准备发布动物日志");
        for (String topic : TOPIC_KEYS) {
            channel.basicPublish(LOG_TOPIC_EXCHANGE, topic, null, topic.getBytes());
            System.out.println("发布动物日志：" + topic);
        }
        System.out.println("发布动物日志完成");
        channel.close();
        connection.close();

    }
}
