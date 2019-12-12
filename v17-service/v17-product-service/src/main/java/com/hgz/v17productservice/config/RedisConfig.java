package com.hgz.v17productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name="redisTemplate1")
    public RedisTemplate<String,Object> getRedisTemplate1(RedisConnectionFactory redisConnectionFactory){
            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            return redisTemplate;
    }
}
