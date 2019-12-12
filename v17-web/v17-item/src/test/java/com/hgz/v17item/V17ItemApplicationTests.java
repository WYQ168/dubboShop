package com.hgz.v17item;
import	java.io.FileWriter;
import java.io.File;
import	java.io.OutputStreamWriter;
import	java.io.Writer;

import com.hgz.v17item.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17ItemApplicationTests {

    @Autowired
    private Configuration configuration;

    @Test
    public void create() throws IOException, TemplateException {
        //模板
        Template template = configuration.getTemplate("freemarker.ftl");
        //数据
        Map<String,Object> data = new HashMap<>();
        data.put("username","wuyingqiang");
        Student student = new Student("zhangsan",23,new Date());
        data.put("student",student);
        //整合

        Writer writer = new FileWriter("D:\\IdeaProjects\\v17\\v17-web\\v17-item\\src\\main\\resources\\static\\freemarker.ftl");
        template.process(data,writer);
        //
        System.out.println("生成静态页面成功！");


    }

    @Test
    public void contextLoads() {
    }


}
