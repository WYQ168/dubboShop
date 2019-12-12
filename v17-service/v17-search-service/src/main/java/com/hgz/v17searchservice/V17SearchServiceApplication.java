package com.hgz.v17searchservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.hgz.mapper")
public class V17SearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V17SearchServiceApplication.class, args);
    }

}
