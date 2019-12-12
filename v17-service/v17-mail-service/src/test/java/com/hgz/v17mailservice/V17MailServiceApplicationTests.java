package com.hgz.v17mailservice;

import cn.hutool.extra.template.TemplateEngine;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17MailServiceApplicationTests {

    @Reference
    private IMailService MailService;

 /*   @Autowired
    private TemplateEngine templateEngine;
*/

    @Test
    public void Simple() {
        MailService.SendSimpleMail("1994397122@qq.com","我比较无聊","我是你爸爸");
        System.out.println("发送邮件成功");
    }

    @Test
    public void HTML() {
        MailService.SendHTMLMail("1667678068@qq.com","我比较无聊","<a href='http://www.baidu.com'>跳转百度</a>");
        System.out.println("发送邮件成功");
    }

    @Test
    public void Template(){

    }



}
