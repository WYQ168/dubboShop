package com.hgz.v17center.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    //声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange("topic-exchange");
    }

}


