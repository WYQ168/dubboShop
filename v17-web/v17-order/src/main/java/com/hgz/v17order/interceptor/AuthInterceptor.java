package com.hgz.v17order.interceptor;


import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Reference
    private IUserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前是否处于登录状态
        //如果已经登录，将用户信息保存到request中
        //无论是否登录，都要放行
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    ResultBean resultBean = userService.CheckIsLogin(token);
                    if((resultBean.getStatusCode()).equals(200)){
                        //说明当前用户已经登录
                        request.setAttribute("user",resultBean.getData());
                        return true;
                    }
                }
            }
        }
        //response.sendRedirect("http:cart.qf.com:9095/sso/index");
        return false;
    }
}
