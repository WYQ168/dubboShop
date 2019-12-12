package com.hgz.v17register.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.util.CodeUntils;
import com.hgz.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
public class UserController {

    @Reference
    private IUserService userService;

    @GetMapping("checkUserNameIsExists/{username}")
    @ResponseBody
    public ResultBean checkUserNameIsExists(@PathVariable("username") String username){
        return null;
    }

    @GetMapping("checkPhoneIsExists/{phone}")
    @ResponseBody
    public ResultBean checkPhoneIsExists(@PathVariable("phone") String phone){
        return null;
    }

    @GetMapping("checkEmailIsExists/{email}")
    @ResponseBody
    public ResultBean checkEmailIsExists(@PathVariable("email") String email){
        return null;
    }

    @PostMapping("generateCode/{Identification}")
    @ResponseBody
    public ResultBean generateCode(@PathVariable("Identification") String Identification){
        return userService.generateCode(Identification);
    }

    /*适合处理异步请求*/
    @RequestMapping("register")
    @ResponseBody
    public ResultBean register(TUser user){
        return null;
    }

    /*适合处理同步请求，跳转到相关页面*/
    @RequestMapping("register4PC")
    public String register4PC(TUser user){
        return "register";
    }

}
