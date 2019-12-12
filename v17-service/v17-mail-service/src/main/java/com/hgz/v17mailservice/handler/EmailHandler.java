package com.hgz.v17mailservice.handler;

import com.hgz.api.IMailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailHandler {

    @Autowired
    private IMailService mailService;

    @RabbitListener(queues = "email.send")
    @RabbitHandler
    public void send(String to,String subject,String content){
        mailService.SendSimpleMail(to,subject,content);
    }
}
