package com.hgz.v17miaosha.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LimiterInterceptor implements HandlerInterceptor {

    //每秒钟产生的令牌个数
    //private RateLimiter rateLimiter = RateLimiter.create(1);

    @Autowired
    private RateLimiter rateLimiter;
    /**
      * 重写前置拦截
      * @param request
      * @param response
      * @param handler
      * @return
      * @throws Exception
      */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //限流逻辑
        //尝试获取令牌
        boolean tryAcquire = rateLimiter.tryAcquire(50, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            // 2.降级处理
            System.out.println("获取令牌失败.....");
            return false;
        }
        // 3.正常业务逻辑处理
        System.out.println("进入秒杀逻辑");
        return true;

    }
}
