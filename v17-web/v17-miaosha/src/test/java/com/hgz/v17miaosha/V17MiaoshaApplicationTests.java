package com.hgz.v17miaosha;



import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17MiaoshaApplicationTests {

    @Autowired
    public IMiaoShaService miaoShaService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
        Long id =1L;
        TMiaoshaProduct product = miaoShaService.getById(id);
        System.out.println(product.getProductName());
    }
    @Test
    public void send(){

        //生成订单编号，保证唯一性
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmm");
        String orderNo = new StringBuilder(dataFormat.format(System.currentTimeMillis())).append(1L).toString();
        //抢购成功，生成订单
        Map<String,Object> params = new HashMap<>();
        params.put("userId",1L);
        params.put("productId",101L);
        params.put("count",1);
        params.put("productPrice",100L);
        params.put("orderNo",orderNo);
        rabbitTemplate.convertAndSend("order-exchange","add.order",params);
    }

}
