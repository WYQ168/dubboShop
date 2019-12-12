package com.hgz.v17cartservice;

import com.hgz.api.ICartService;
import com.hgz.commons.base.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17CartServiceImplApplicationTests {

    @Autowired
    private ICartService cartService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void addCart(){
        String token = "123456";
        ResultBean cartItem = cartService.add(token, 3L, 1000);
        ResultBean cartItem1 = cartService.add(token, 4L, 1090);
        ResultBean cartItem2 = cartService.add(token, 5L, 1080);
        System.out.println(cartItem);
    }

    @Test
    public void queryTest(){
        ResultBean cartItem = cartService.query("123456");
        System.out.println(cartItem);
    }

    @Test
    public void delTest(){
        ResultBean del = cartService.del("123456", 2L);
        System.out.println(del);
    }

    @Test
    public void updateTest(){
        //防止更新的是不存在的商品，需要拦截
        ResultBean update = cartService.update("123456", 7L,8888);
        System.out.println(update);
    }


}
