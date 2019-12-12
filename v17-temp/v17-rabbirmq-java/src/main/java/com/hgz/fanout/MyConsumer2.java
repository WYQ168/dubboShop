package com.hgz.fanout;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MyConsumer2 {
    private static final String  EXCHANGE = "publish";
    private static final String  QUEUE = "publish-queue-2";

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
        final Channel channel = connection.createChannel();
        //限流
        channel.basicQos(1);
        //声明队列，并将队列绑定到交换机上
        channel.queueDeclare(QUEUE,false,false,false,null);
        channel.queueBind(QUEUE,EXCHANGE,"");
        //3. 创建一个消费者对象
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message =new String(body,"utf-8");
                System.out.println("2接收到的消息："+message);
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        channel.basicConsume(QUEUE,true,consumer);

    }

}
