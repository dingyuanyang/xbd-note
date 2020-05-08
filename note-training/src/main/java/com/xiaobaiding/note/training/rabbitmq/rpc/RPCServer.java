package com.xiaobaiding.note.training.rabbitmq.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

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
        channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);
        //
        channel.queuePurge(RPC_QUEUE_NAME);

        channel.basicQos(1);

        System.out.println("服务器等待RPC请求");

        Object monitor = new Object();

        DeliverCallback callback = (tag,delivery)->{
            AMQP.BasicProperties replayProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();
            String response= "";
            try {
                String msg = new String(delivery.getBody(),"utf-8");
                int n = Integer.parseInt(msg);
                System.out.println("[.]fib(" + msg + ")");
                response+=fib(n);
            } catch (RuntimeException e) {
                System.out.println("[.] " + e.toString());
            }finally {
                channel.basicPublish("",delivery.getProperties().getReplyTo(),replayProps,response.getBytes("utf-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                //mq客户端消费者线程通知rpc服务自己的线程
                synchronized (monitor){
                    monitor.notify();
                }
            }
        };

        channel.basicConsume(RPC_QUEUE_NAME,false,callback,tag->{});
        //等待并准备使用来自RPC客户机的消息。
        while (true){
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
