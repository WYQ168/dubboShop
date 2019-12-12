package com.hgz.v17miaosha.controller;

import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("remind")
public class RemindController {

    @Autowired
    private IRemindService remindService;

    @RequestMapping("add/{txseckillId}/{userId}")
    public ResultBean add(@PathVariable("txseckillId") Integer txseckillId,@PathVariable("userId") Integer userId){

        return remindService.add(txseckillId,userId);
    }


}
