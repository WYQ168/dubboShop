package com.hgz.v17index.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IProductTypeService;
import com.hgz.entity.TProductType;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("index")
public class IndexController {

    @Reference(check = false)
    private IProductTypeService productTypeService;


    @RequestMapping("show")
    public String show(Model model){
        List<TProductType> list = productTypeService.list();
        model.addAttribute("list",list);
        return "index";
    }
}
