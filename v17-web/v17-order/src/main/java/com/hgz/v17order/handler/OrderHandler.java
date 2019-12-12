package com.hgz.v17order.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderHandler {


    @RabbitListener(queues ="order-queue")
    @RabbitHandler
    public void process(Map<String,Object> params, Channel channel, Message message){
        System.out.println(params);
        System.out.println(params.get("orderNo"));
        try{
            System.out.println("模拟生成订单表以及订单表的相关信息");
            Thread.sleep(3000);

            //手工确认
            long tag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(tag,false);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
