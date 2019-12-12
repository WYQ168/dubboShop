package com.hgz.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producter {
    private static final String  EXCHANGE = "d-exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("193.112.19.133");
        connectionFactory.setVirtualHost("java1907");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("java1907");
        connectionFactory.setPassword("123");
        Connection connection = connectionFactory.newConnection();
        //2. 基于连接对象创建channel
        Channel channel = connection.createChannel();
        //3. 声明一个队列

        channel.exchangeDeclare(EXCHANGE, "direct");
        //4.发送消息

        String message1 = "第一条消息";
        channel.basicPublish(EXCHANGE, "key1", null, message1.getBytes());
        String message2 = "第二条消息";
        channel.basicPublish(EXCHANGE, "key2", null, message2.getBytes());
        System.out.println("消息发送成功！");

    }
}
