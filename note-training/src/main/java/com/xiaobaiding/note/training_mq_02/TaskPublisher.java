package com.xiaobaiding.note.training_mq_02;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 任务发布者
 */
public class TaskPublisher {

    private final static String QUEUE_NAME = "TASK_QUEUE";

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
         * 4 - 声明队列和通道
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(QUEUE_NAME + "-->当前任务发布者已经准备要发送任务了");
        /**
         * 5 - 发送任务
         */
        for (int i = 0; i < 20; i++) {
            String task = "任务：" + (i) + ":做点什么？";
            channel.basicPublish("", QUEUE_NAME, null, task.getBytes());
            System.out.println("发布任务成功："+task);
        }
        System.out.println("任务发送完成");
        channel.close();
        connection.close();
    }

}
