package com.hgz.v17search.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ISearchService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CenterProductHandler {

    @Reference
    private ISearchService searchService;

    @RabbitHandler
    @RabbitListener(queues = "topic-queue")
    public void process(Long newId){
        System.out.println(newId);
        searchService.synById(newId);
    }
}
