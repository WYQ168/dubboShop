package com.hgz.v17miaosha.service.Impl;
import	java.text.SimpleDateFormat;
import	java.util.HashMap;


import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.exception.MiaoShaException;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MiaoShaServiceImpl implements IMiaoShaService {

    @Autowired
    private TMiaoshaProductMapper  productMapper;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

   // private Runtime runtime = Runtime.getRuntime();

    private static int count =0;

    @Override
    //@Cacheable(value = "product",key = "id")
    public TMiaoshaProduct getById(Long id) {
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get("product:" + id);
        if(product == null){
            product = productMapper.selectByPrimaryKey(id);
            redisTemplate.opsForValue().set("product:"+id,product);
        }
       // System.out.println(runtime.freeMemory());
        return product;
        //return productMapper.selectByPrimaryKey(id);
    }


    @Transactional
    public ResultBean killOld(Long userId, Long productId) {
        //用悲观锁实现的秒杀
        TMiaoshaProduct miaoshaProduct = productMapper.selectByPrimaryKey(productId);
        if(miaoshaProduct.getCount() >0){
            miaoshaProduct.setCount(miaoshaProduct.getCount()-1);
            miaoshaProduct.setUpdateTime(new Date());
            productMapper.updateByPrimaryKeySelective(miaoshaProduct);
            System.out.println("抢到了----->"+(++count));
            return new ResultBean(200,"抢够成功，正在跳转到支付页面");
        }
        return new ResultBean(400,"很遗憾你没有抢到");
    }

    @Override
    public ResultBean kill(Long userId, Long seckillId, String path) {

        StringBuilder pathKey = new StringBuilder("miaosha:")
                .append(":").append(userId).append(":").append(seckillId);
        Object o = redisTemplate.opsForValue().get(pathKey.toString());
        if( o == null){
                throw new MiaoShaException("这不是合法的秒杀路径！");
        }
        //一次性有效，用完就立马删除
        redisTemplate.delete(pathKey.toString());

        //1.获取到当前秒杀活动的信息
       // TMiaoshaProduct product = productMapper.selectByPrimaryKey(seckillId);
        StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(seckillId);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(killInfoKey.toString());
        //2.未开始
        if ("0".equals(product.getStatus())){
            throw new MiaoShaException("当前活动还未开始，请耐心等候");
        }
        //3.已结束
        if("2".equals(product.getStatus())){
            throw new MiaoShaException("当前活动已结束，请下次再来");
        }
        //4.确权用户信息的key
        StringBuilder key = new StringBuilder("miaosha:user:").append(seckillId);
        //获取到了抢购权
        Long add = redisTemplate.opsForSet().add(key.toString(), userId);
        if(add == 0){
            throw new MiaoShaException("您已经抢购到心仪的商品，请勿重复抢购！");
        }
        //获取当前的活动对应的商品
        StringBuilder killKey = new StringBuilder("miaosha:product:").append(seckillId);
        Long productId = (Long) redisTemplate.opsForList().leftPop(killKey.toString());
        if(productId == null){
            //移除掉已经中过奖的用户
            redisTemplate.opsForSet().remove(key.toString(),userId);
            throw new MiaoShaException("当前商品已被抢购一空");
        }

        //生成订单编号，保证唯一性
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmm");
        String orderNo = new StringBuilder(dataFormat.format(System.currentTimeMillis())).append(userId).toString();
        //抢购成功，生成订单
        Map<String,Object> params = new HashMap<> ();
        params.put("userId",userId);
        params.put("productId",product.getProductId());
        params.put("count",1);
        params.put("productPrice",product.getSalePrice());
        params.put("orderNo",orderNo);
        rabbitTemplate.convertAndSend("order-exchange","add.order",params);

        return new ResultBean(200,orderNo);
    }

    @Override
    public ResultBean getPath(Long userId, Long secKillId) {

        //客户端发起请求，获取到动态生成的path（前提是到了秒杀时间而且已经登录）

       //1.获取到当前秒杀活动的信息
        StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(secKillId);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(killInfoKey.toString());

        //2.未开始
        if ("0".equals(product.getStatus())){
            throw new MiaoShaException("当前活动还未开始，请耐心等候");
        }
        //3.已结束
        if("2".equals(product.getStatus())){
            throw new MiaoShaException("当前活动已结束，请下次再来");
        }
        //获取动态的秒杀地址
        String path = UUID.randomUUID().toString();
        //将path保存到Redis中
        StringBuilder pathKey = new StringBuilder("miaosha:")
                .append(":").append(userId).append(":").append(secKillId);
        redisTemplate.opsForValue().set(pathKey.toString(),path);
        //设置该地址的有效时间
        redisTemplate.expire(pathKey.toString(),1, TimeUnit.MINUTES);

        return new ResultBean(200,path);
    }

}
