package com.hgz.v17miaosha.task;
import	java.util.ArrayList;
import	java.util.Collection;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.List;

@Component
public class MiaoShaTask {

    @Autowired
    private TMiaoshaProductMapper productMapper;

/*    @Resource(name = "myRedisTemplate")
    private RedisTemplate redisTemplate;*/

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    //正常应该是定点扫描
    @Scheduled(cron = "0/5 * * * * ?")
    public void task(){
        //查询当前可以秒杀的活动
        List<TMiaoshaProduct> list = productMapper.getCanKillActiveProduct();
        if (list != null && !list.isEmpty()) {
            for (TMiaoshaProduct product : list) {
                //初始化Redis的信息
                    StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                    //修改方案
                //1、采用流水线的技术
                /*redisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                        for(int i =0;i<product.getCount();i++){
                            redisTemplate.opsForList().leftPush(key.toString(), product.getProductId());
                        }
                        return null;
                    }
                });*/
                //2、一次性保存一个集合
                Collection<Long> ids =new ArrayList<Long> (product.getCount());
                for (Integer i = 0; i < product.getCount(); i++) {
                    ids.add(product.getProductId());
                }
                redisTemplate.opsForList().leftPushAll(key.toString(),ids);

                //更新状态
                product.setStatus("1");
                productMapper.updateByPrimaryKey(product);

                //保存当前活动的信息到Redis中
                StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(product.getId());
                redisTemplate.opsForValue().set(killInfoKey.toString(),product);

            }
            System.out.println("初始化Redis完毕");
        }
    }

    //Redis
    //定期扫描，将当前已经结束的活动，做相关的清理工作
    @Scheduled(cron = "0/5 * * * * ?")
    public void scanCanStopKillProduct(){
        //1，查询当前可以开启秒杀的活动
        List<TMiaoshaProduct> list = productMapper.getCanStopKillProduct();
        //2. 判断
        if(list != null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                //1，清空Redis中相关的信息
                StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                redisTemplate.delete(key.toString());

                StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(product.getId());
                redisTemplate.delete(killInfoKey.toString());

                //2，更新数据库表的记录为已结束
                product.setStatus("2");
                productMapper.updateByPrimaryKey(product);
            }
            //slf4j
            //log
            System.out.println("清理完成....");
        }
    }


}
