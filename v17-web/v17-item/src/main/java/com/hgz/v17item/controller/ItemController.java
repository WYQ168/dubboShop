package com.hgz.v17item.controller;
import	java.util.ArrayList;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IProductService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private Configuration configuration;

    @Reference
    private IProductService productService;

    @RequestMapping("createHTML/{id}")
    @ResponseBody
    public ResultBean createHTML(@PathVariable("id") Long id){
        //1.获取商品信息
        TProduct product = productService.selectByPrimaryKey(id);
        System.out.println(product);
        try {
            //2.获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            //3.设置模板数据
            Map<String,Object> data = new HashMap<>();
            data.put("product",product);
            //4.生成静态页
            //获取static的路径
            String serverpath= ResourceUtils.getURL("classpath:static").getPath();
            //构建输出流对象
            FileWriter out = new FileWriter(serverpath+ File.separator+id+".html");
            //生成静态页
            template.process(data,out);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean(500,"读取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean(500,"生成静态页面失败");
        }
        return new ResultBean(200,"生成静态页成功！");
    }

    @RequestMapping("batchCreate")
    @ResponseBody
    public ResultBean createHTML(@RequestParam List<Long> ids) {
        //结合真实服务器硬件条件来设置
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        //结论就是自己来创建线程池
        ExecutorService pool = new ThreadPoolExecutor(
                corePoolSize,corePoolSize*2,
                0L, TimeUnit.SECONDS,new LinkedBlockingQueue(100));

        List<Future> list =new ArrayList<> ();
        for (Long id : ids) {
           list.add(pool.submit(new createHTMLTask(id)));
        }
        //查看执行的结果
        for (Future future : list) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new ResultBean(200,"批量生成静态页成功！");
    }

   private class createHTMLTask implements Callable<Long>{

        private Long id;

       public createHTMLTask(Long id) {
           this.id = id;
       }

       @Override
       public Long call() throws IOException {
           //1.获取商品信息
           TProduct product = productService.selectByPrimaryKey(id);
           //2.获取模板对象
           Template template = null;
           try {
               template = configuration.getTemplate("item.ftl");
               //3.设置模板数据
               Map<String,Object> data = new HashMap<>();
               data.put("product",product);
               //4.生成静态页
               //获取static的路径
               String serverpath= ResourceUtils.getURL("classpath:static").getPath();
               //构建输出流对象
               FileWriter out = new FileWriter(serverpath+ File.separator+id+".html");
               //生成静态页
               template.process(data,out);
           } catch (IOException |TemplateException e) {
               e.printStackTrace();
               return id;
           }
           return 0L;
       }
   }

}