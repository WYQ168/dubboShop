package com.hgz.v17search.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    //声明队列
    @Bean
    public Queue getQueue(){
        return new Queue("topic-queue");
    }

    //声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange("topic-exchange");
    }

    //将队列绑定到交换机
    @Bean
    public Binding bindingExchange(Queue getQueue, TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("product.add");
    }
}
