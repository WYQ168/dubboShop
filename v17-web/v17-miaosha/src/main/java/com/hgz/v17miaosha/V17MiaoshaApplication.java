package com.hgz.v17miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling //开启定时任务
@SpringBootApplication
@MapperScan("com.hgz.v17miaosha.mapper")
public class V17MiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(V17MiaoshaApplication.class, args);
    }

}
