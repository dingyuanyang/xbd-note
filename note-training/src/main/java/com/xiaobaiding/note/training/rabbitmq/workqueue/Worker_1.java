package com.xiaobaiding.note.training.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker_1 {

    private final static String QUEUE_NAME = "TASK_QUEUE";

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
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(QUEUE_NAME + "-->当前工作者1已经准备要接收任务了");
        /**
         *5 - 一次只接受一条未处理的消息
         * accept only one unack-ed message at a time (see below)
         */
        channel.basicQos(1);
        /**
         * 6 - 收到消息以后的
         */
        DeliverCallback callback = (tag, delivery) -> {
            String taskMsg = new String(delivery.getBody(), "utf-8");
            System.out.println("当前工作者1接收到的任务是：" + taskMsg);
            try {
                /**
                 * 7 - 做工作
                 */
                doWork(taskMsg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                System.out.println("工作者1完成工作");
                /**
                 * 8 - 完成工作后通知MQ
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME,autoAck,callback,tag->{});
    }

    private static void doWork(String task) throws InterruptedException {
        Thread.sleep(1000);
    }
}
