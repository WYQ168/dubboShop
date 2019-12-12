package com.hgz.v17cart.handler;

import com.hgz.api.ICartService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SSOHandler {

    @Reference
    private ICartService cartService;

    @RabbitListener(queues = "sso-queue-cart")
    @RabbitHandler
    public void process(Map<String,String> params){
        String noLoginKey = params.get("noLoginKey");
        String loginKey = params.get("loginKey");
        //合并购物车
        cartService.merge("user:cart:"+noLoginKey,"user:cart:"+loginKey);

    }
}
