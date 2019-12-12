package com.hgz.v17userservice;

import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17UserServiceApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    public void checkLogin() {
        TUser user = new TUser();
        user.setUsername("123456");
        user.setPassword("123");
        ResultBean resultBean = userService.selectCheckLogin(user);
        System.out.println(resultBean.getStatusCode());
    }

    @Test
    public void jwtTokenCreateTest(){
        JwtBuilder builder = Jwts.builder()
                .setId("123").setSubject("吴应强")
                .setIssuedAt(new Date())
                //添加自定义属性
                .claim("role","admin")
                .setExpiration(new Date(new Date().getTime()+600000))
                .signWith(SignatureAlgorithm.HS256,"wuyingqiang");

        String jwtToken = builder.compact();
        System.out.println(jwtToken);
    }


    @Test
    public void jwtTokenParseTest(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJzdWIiOiLlkLTlupTlvLoiLCJpYXQiOjE1NzM3MDIyNjAsInJvbGUiOiJhZG1pbiIsImV4cCI6MTU3MzcwMjg2MH0.EN4KnXs8DzlHf4tYnhkP0Wo120T_NLT1aRVV5Hq-vSc";

        Claims claims = Jwts.parser().setSigningKey("wuyingqiang")
                .parseClaimsJws(token).getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getExpiration());
        //获取属性
        System.out.println(claims.get("role"));

    }
}
