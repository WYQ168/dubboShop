package com.hgz.v17login.controller;
import	java.util.HashMap;
import java.util.Map;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("sso")
public class LoginController {

    @Reference
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("checkLogin")
    @ResponseBody
    public ResultBean checkLogin(TUser user,HttpServletResponse response){
        ResultBean resultBean = userService.selectCheckLogin(user);
        if((resultBean.getStatusCode()).equals(200)) {
            //TODO 写cookie给客户端，保存凭证

            //1、获取UUID
            String uuid = (String) resultBean.getData();
            //2、创建cookie对象
            Cookie cookie = new Cookie("user_token", uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setDomain("qf.com");
            //3、写cookie到客户端
            response.addCookie(cookie);
        }
        return resultBean;
    }

    @RequestMapping("checkLogin4PC")
    public String checkLogin4PC(TUser user, HttpServletRequest request,
                                HttpServletResponse response,
                                @CookieValue(name = "cart_token",required = false) String cartToken){
        ResultBean resultBean =userService.selectCheckLogin(user);
        if((resultBean.getStatusCode()).equals(200)){
            //TODO 写cookie给客户端，保存凭证

            //1、获取UUID
            //String uuid = (String) resultBean.getData();
            Map<String,String> datas = (Map<String, String>) resultBean.getData();
            String uuid =datas.get("jwtToken");
            //2、创建cookie对象
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setDomain("qf.com");
            //3、写cookie到客户端
            response.addCookie(cookie);

            //发送消息到交换机
            Map<String,String> params = new HashMap<>();
            params.put("noLoginKey",cartToken);
            params.put("loginKey",datas.get("username"));
            rabbitTemplate.convertAndSend("sso-exchange","user.login",params);

            //request.getSession().setAttribute("user",user.getUsername());
            //登录成功，默认跳转首页
            return "redirect:http://localhost:9091";
        }
        return "login";
    }

  /*  @GetMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        //TODO
        //1、获取cookie的值
        Cookie[] cookies = request.getCookies();
        //2、去Redis中查询是否存在该凭证信息
        if(cookies != null && cookies.length > 0){
            //遍历查询我们需要的那个cookie
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    ResultBean resultBean = userService.CheckIsLogin(uuid);
                    return resultBean;
                }
            }
        }
       return new ResultBean(400,null);
    }*/

    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name ="user_token",required =false) String uuid){
        if(uuid !=null){
                ResultBean resultBean = userService.CheckIsLogin(uuid);
                return resultBean;
            }
        return new ResultBean(400,null);
    }

    @GetMapping("checkIsLogin4PC")
    public String checkIsLogin4PC(){
        return null;
    }


    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name ="user_token",required =false) String token,HttpServletResponse response){
        //request.getSession().removeAttribute("user");
        //将cookie清除，设置有效期为0
        if(token != null){
            //1、创建cookie对象
            Cookie cookie = new Cookie("user_token",token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setDomain("qf.com");
            cookie.setMaxAge(0);
            //3、写cookie到客户端
            response.addCookie(cookie);
        }
        return new ResultBean(200,true);
    }

    @PostMapping("logout4PC")
    public String logout4PC(TUser user){
        return "";
    }


    @RequestMapping("index")
    public String index(TUser user){
        return "login";
    }


}
