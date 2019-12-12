package com.hgz.v17smsservice.handler;

import com.hgz.api.ISMS;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SMSHandler {

    @Autowired
    private ISMS sms;

    @RabbitListener(queues = "sms-topic-queue")
    @RabbitHandler
    public void processSendCode(Map<String,String> map){
        String identification = map.get("Identification");
        String code = map.get("code");
        sms.sendCodeMessage(identification,code);
    }

}
