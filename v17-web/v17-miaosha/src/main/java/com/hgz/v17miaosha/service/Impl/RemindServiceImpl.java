package com.hgz.v17miaosha.service.Impl;
import	java.util.HashMap;
import	java.text.SimpleDateFormat;
import	java.util.Calendar;
import java.util.Date;
import java.util.Map;


import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class RemindServiceImpl implements IRemindService {

    @Autowired
    private TMiaoshaProductMapper productMapper;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultBean add(Integer txseckillId, Integer userId) {
        //拿到秒杀的商品
        TMiaoshaProduct miaosha = productMapper.selectByPrimaryKey(Long.parseLong(txseckillId.toString()));
        //提前十分钟通知对方
        Calendar cal = Calendar.getInstance();
        cal.setTime(miaosha.getStartTime());
        //设置提前十分钟
        cal.add(Calendar.MINUTE,-10);
        //设置提醒秒杀的时间戳
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String score = sdf.format(time);

        //将这个提醒xixni保存到Redis中去
        Map<String,Integer> map = new HashMap<> ();
        map.put("txseckillId",txseckillId);
        map.put("userId",userId);

        redisTemplate.opsForZSet().add("miaosha:remind",map,Double.parseDouble(score));
        return new ResultBean(200,"已保存好提醒功能");
    }
}
