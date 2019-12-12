package com.hgz.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producter {
    private static final String  QUEUE_NAME = "work";

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
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //4.发送消息
        for (int i = 0; i < 10; i++) {
            String message = "message:"+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("第"+i+"个消息发送成功！");
        }
    }
}
