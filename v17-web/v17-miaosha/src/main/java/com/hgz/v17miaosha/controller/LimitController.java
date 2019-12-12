package com.hgz.v17miaosha.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@RequestMapping("limit")
@Slf4j
@Controller
public class LimitController {
    //每秒生成的令牌个数
    private RateLimiter rateLimiter = RateLimiter.create(2);

    @RequestMapping("miaosha")
    @ResponseBody
    public String miaosha() {
        //1.客户端从令牌桶获取令牌，如果500毫秒内没有获取到令牌，则降级处理
        boolean tryAcquire = rateLimiter.tryAcquire(10, TimeUnit.MILLISECONDS);

        if (!tryAcquire) {
            // 2.降级处理
            System.out.println("获取令牌失败.....");
            return "当前抢购人数太多，请稍后再试！";
        }
        // 3.正常业务逻辑处理
        System.out.println("进入秒杀逻辑");
        return "秒杀的结果是：成功!";
    }

    public static void main(String[] args){
        RateLimiter rateLimiter = RateLimiter.create(10.0);
        System.out.println(rateLimiter.acquire(20));
        System.out.println(rateLimiter.acquire(1));
        System.out.println(rateLimiter.acquire(1));
    }
}
