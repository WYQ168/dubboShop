package com.hgz.v17miaosha.config;

import com.hgz.v17miaosha.interceptor.LimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LimiterInterceptor limiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //new LimiterInterceptor() 自己创建的对象，不归Spring管理
        //注册拦截器
        registry.addInterceptor(limiterInterceptor).addPathPatterns("/**");
    }
}
