package com.xiaobaiding.note.training.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息生产者
 */
public class RabbitMQConsumer {

    /**
     * 消息队列名称
     */
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 1 - 设置RabbitMQ连接工厂信息
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.87");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        /**
         * 2 - 新建连接信息
         */
        Connection connection = factory.newConnection();

        /**
         * 3 - 创建消息通道
         */
        Channel channel = connection.createChannel();

        /**
         * 4 - 声明队列和通道
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(QUEUE_NAME + "-->当前消费者已经准备要接收消息了");
        /**
         * 5 - 创建消息消费者
         */
        Consumer consumer = new DefaultConsumer(channel) {
            /***
             * 消费者在接收到消息以后，会出发消息发送事件
             * @param consumerTag
             * @param envelope
             * @param properties
             * @param body
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("消费者收到了消息：" + msg);
            }
        };
        /**
         * 6 - 通知生产者信息收到
         */
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
