package com.hgz.v17item.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Bean
    public Queue getQueue(){
        return new Queue("topic2-queue");
    }

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange("topic-exchange");
    }

    @Bean
    public Binding bindingExchange(Queue getQueue,TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("product.add");
    }


}
