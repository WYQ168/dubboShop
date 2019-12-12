package com.hgz.v17config.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("limiter")
public class LimiterConfigController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                //到底是哪个消息被确认了
                System.out.println("correlationData: " + correlationData.getId());
                //TODO  2、更新消息的状态或者直接删除也行
            }
        }
    };

    @RequestMapping("change/{newRate}")
    @ResponseBody
    public String change(@PathVariable("newRate") Double newRate){

        System.out.println("update config.....");
        //设置消息异步回调处理函数
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //作为一个唯一标识
        CorrelationData correlationData = new CorrelationData(newRate.toString());
        //TODO 1、往消息记录表里面插入消息
        //发送消息
        rabbitTemplate.convertAndSend("","config-limiter-queue",newRate,correlationData);

        return  "success";
    }

    //TODO 3、定时任务，定期进行扫描消息表，进行补偿重发

}
