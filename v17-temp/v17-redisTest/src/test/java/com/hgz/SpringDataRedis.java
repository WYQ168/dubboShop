package com.hgz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void SpringDataRedisTest(){
        redisTemplate.opsForValue().set("wyq3","444444");
        Object wyq = redisTemplate.opsForValue().get("wyq3");
        System.out.println(wyq);
    }

}
