package com.hgz.v17miaosha.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.exception.MiaoShaException;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("miaosha")
public class MiaoShaController {

    @Autowired
    private IMiaoShaService miaoShaService;


    @RequestMapping("get")
    public String selectById(Long id, Model model){
        TMiaoshaProduct miaoshaProduct = miaoShaService.getById(id);
        model.addAttribute("product",miaoshaProduct);
        return "index";
    }

    @RequestMapping("getPath")
    @ResponseBody
    public ResultBean getPath(Long userId,Long secKillId){
        try{
            return  miaoShaService.getPath(userId,secKillId);
        }catch (MiaoShaException e){
            return new ResultBean(404,e.getMessage());
        }
    }

    @RequestMapping("kill/{path}")
    @ResponseBody
    public ResultBean kill(Long id, @PathVariable("path") String path) {
        Long userId = 1L;
        try {
            return miaoShaService.kill(userId, id, path);
        } catch (MiaoShaException e) {
            return new ResultBean(404, e.getMessage());
        }
    }


}
