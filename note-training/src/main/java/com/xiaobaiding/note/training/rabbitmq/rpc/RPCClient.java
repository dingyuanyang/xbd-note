package com.xiaobaiding.note.training.rabbitmq.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable {

    private final String RPC_QUEUE_NAME = "rpc_queue";
    private Connection connection;
    private Channel channel;

    public RPCClient() throws IOException, TimeoutException {
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
         connection = factory.newConnection();

        /**
         * 3 - 创建消息通道
         */
         channel = connection.createChannel();
    }

    public String call(String msg) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();
        String replayQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replayQueueName)
                .build();
        channel.basicPublish("",RPC_QUEUE_NAME,props,msg.getBytes("utf-8"));
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        String ctag = channel.basicConsume(replayQueueName, true, (consumerTag, delivery) -> {
            if (corrId.equals(delivery.getProperties().getCorrelationId())) {
                response.offer(new String(delivery.getBody(), "utf-8"));
            }
        }, tag -> {
        });
        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) {
        try {
            RPCClient rpcClient = new RPCClient();
            for (int i = 0; i < 50; i++) {
                String i_str = Integer.toString(i);
                System.out.println("请求远程调用fib(" + i_str + ")");
                String response = rpcClient.call(i_str);
                System.out.println("[.] 获得返回值：" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

