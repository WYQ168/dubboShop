package com.hgz.v17center.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductDescService;
import com.hgz.api.IProductService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import com.hgz.entity.TProductDesc;
import com.hgz.vo.TProductVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangguizhao
 */
@Component
@Controller
@RequestMapping("product")
public class ProductController {

    //启动时检查机制
    @Reference
    private IProductService productService;

    @Reference
    private IProductDescService productDescService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct getById(@PathVariable("id") Long id){
        return productService.selectByPrimaryKey(id);
    }
    @RequestMapping("list")
    public String list(Model model){
        List<TProduct> list = productService.list();
        model.addAttribute("list",list);
        return "product/list";
    }
    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model,@PathVariable("pageIndex") Integer pageIndex,@PathVariable("pageSize") Integer pageSize){
        PageInfo<TProduct> page = productService.page(pageIndex,pageSize);
        System.out.println(pageIndex+"--->"+pageSize);
        model.addAttribute("page",page);
        System.out.println(page);
        return "product/list";
    }


    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                //到底是哪个消息被确认了
                System.out.println("correlationData: " + correlationData.getId());
                //TODO  2、更新消息的状态或者直接删除也行
            }
        }
    };

    @RequestMapping("add")
    public String add(TProductVO vo){
        Long newId =productService.add(vo);
        System.out.println(vo);
        //需要携带一个附加数据传递过去
        CorrelationData correlationData = new CorrelationData(newId.toString());
        //设置消息异步回调处理函数
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend("topic-exchange","product.add",newId);
        return "redirect:/product/page/1/1";
    }


    @RequestMapping("delById/{id}")
    @ResponseBody
    public int delById(@PathVariable("id") Long id){
        int result = productService.deleteByPrimaryKey(id);
        System.out.println(result);
        return result;
    }

    @RequestMapping("toUpdate/{id}")
    @ResponseBody
    public TProductVO toUpdate(@PathVariable("id") Long id,Model model){
        TProduct Product = productService.selectByPrimaryKey(id);
        System.out.println(Product);
        TProductDesc ProductDesc = productDescService.selectByProductId(id);
        System.out.println(ProductDesc);
        return new TProductVO(Product,ProductDesc.getProductDesc());
    }
    @RequestMapping("update")
    public String update(TProductVO vo){
        Long update = productService.update(vo);
        System.out.println("update--->"+update);
        return "redirect:/product/page/1/1";
    }
    @RequestMapping("delByIds")
    @ResponseBody
    public ResultBean delByIds(@RequestParam List<Long> ids){
        System.out.println(ids);
        int count = productService.delByIds(ids);
        if(count > 0){
            return new ResultBean(200,"删除成功");
        }
        return new ResultBean(500,"删除失败");
    }
    @RequestMapping("index")
    public String index(){
        return "product/index";
    }

}
