package com.hgz.v17miaosha.handler;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LimiterChange {

    @Autowired
    private RateLimiter rateLimiter;

    @RabbitHandler
    @RabbitListener(queues = "config-limiter-queue")
    public void processChange(Double rate){

        rateLimiter.setRate(rate);
    }
}
