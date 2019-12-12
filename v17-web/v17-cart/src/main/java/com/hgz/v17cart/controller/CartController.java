package com.hgz.v17cart.controller;

import com.hgz.api.ICartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    @RequestMapping("add/{productId}/{count}")
    public ResultBean add(@PathVariable("productId") Long productId, @PathVariable("count") Integer count,
                          @CookieValue(name = "cart_token",required = false) String cartToken,
                          HttpServletResponse response, HttpServletRequest request){

        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            //说明已经登录
            return cartService.add(userToken,productId,count);
        }
        //未登录的时候，创建一个UUID作为token
        if(cartToken == null){
            cartToken = UUID.randomUUID().toString();
        }
        reflushService(cartToken, response);
        return cartService.add(cartToken,productId,count);
    }



    @RequestMapping("query")
    public ResultBean query( @CookieValue(name = "cart_token",required = false) String cartToken,
                             HttpServletResponse response,HttpServletRequest request){

        String userToken = (String) request.getAttribute("user");
        if (userToken != null) {
            return cartService.query(userToken);
        }
        if(cartToken != null){
            ResultBean query = cartService.query(cartToken);
            Object data = query.getData();
            reflushService(cartToken, response);
            return new ResultBean(200,data);
        }
        return new ResultBean(400,null);
    }

    @RequestMapping("update/{productId}/{count}")
    public ResultBean update(@PathVariable("productId") Long productId,@PathVariable("count") Integer count,
                             @CookieValue(name = "cart_token",required = false) String cartToken,
                             HttpServletResponse response,HttpServletRequest request){
        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            return cartService.update(userToken, productId, count);
        }
        if(cartToken != null){
            ResultBean update = cartService.update(cartToken, productId, count);
            reflushService(cartToken, response);
            return update;
        }
        return new ResultBean(400,false);
    }


    @RequestMapping("del/{productId}")
    public ResultBean del(@PathVariable("productId") Long productId,
                          @CookieValue(name = "cart_token",required = false) String cartToken,
                          HttpServletResponse response,HttpServletRequest request){
        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            return cartService.del(userToken, productId);
        }
        if(cartToken != null){
            ResultBean del = cartService.del(cartToken, productId);
            reflushService(cartToken,response);
            return del;
        }

        return null;
    }


    private void reflushService(@CookieValue(name = "cart_token", required = false) String cartToken, HttpServletResponse response) {
        //写cookie到客户端，更新有效期
        Cookie cookie = new Cookie("cart_token",cartToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("qf.com");
        cookie.setMaxAge(15*24*60*60);
        response.addCookie(cookie);
    }
}
