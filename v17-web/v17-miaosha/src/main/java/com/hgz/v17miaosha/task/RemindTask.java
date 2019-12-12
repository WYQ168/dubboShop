package com.hgz.v17miaosha.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class RemindTask {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Scheduled(cron = "0 10 * * * * ")
    public void task(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String score = sdf.format(new Date());

        Set<Object> reminds = redisTemplate.opsForZSet().rangeByScore("miaosha:remind",
                Double.valueOf(score), Double.valueOf(score));
        for (Object remind : reminds) {
            Map params = (Map) remind;
            Object txseckillId = params.get("txseckillId");
            Object userId = params.get("userId");
            log.info("获取到秒杀秒杀的信息：{}，{}",txseckillId,userId);
        }
        //移除掉这些已经提醒过的信息
        redisTemplate.opsForZSet().removeRangeByScore("miaosha:remind",
                Double.valueOf(score), Double.valueOf(score));
    }
}
