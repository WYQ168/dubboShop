package com.hgz.v17userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.util.CodeUntils;
import com.hgz.entity.TUser;
import com.hgz.mapper.TUserMapper;
import com.hgz.api.IUserService;
import com.hgz.v17userservice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService  {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }

    @Override
    public ResultBean checkUserNameIsExists() {
        return null;
    }

    @Override
    public ResultBean checkPhoneIsExists() {
        return null;
    }

    @Override
    public ResultBean checkEmailIsExists() {
        return null;
    }

    @Override
    public ResultBean generateCode(String Identification) {
        //1、生成验证码
        String code = CodeUntils.generateCode(4);
        //2、往Redis中保存一个凭证跟验证码的对应关系
        redisTemplate.opsForValue().set(Identification,code,2, TimeUnit.MINUTES);
        //3、发送消息，给手机发送验证码
        Map<String,String> map = new HashMap<>();
        map.put("Identification",Identification);
        map.put("code",code);
        rabbitTemplate.convertAndSend("sms-topic-exchange","sms.code",map);

        Map<String,String> map2 = new HashMap<>();
        map2.put("to","485782489@qq.com");
        map2.put("subject", "测试");
        map2.put("content", "发送一个普通邮件");
        rabbitTemplate.convertAndSend("email-topic-queue","email.send",map2);
        return null;
    }

    @Override
    public ResultBean selectCheckLogin(TUser user) {
        //拿到用户的信息
        TUser currentUser = userMapper.selectByIdentification(user.getUsername());
        //根据查询的密码信息，作比较
        if(currentUser !=null){
            if(user.getPassword().equals(currentUser.getPassword())){
                //TODO 往Redis里面保存凭证信息
                //1、生成UUID
                //String uuid = UUID.randomUUID().toString();
                //2、保存到Redis中去，代替原来的session
                //redisTemplate.opsForValue().set("user:token"+uuid,currentUser.getUsername(),30, TimeUnit.MINUTES);
                JwtUtils jwtUtils = new JwtUtils();
                jwtUtils.setSecretKey("java1907");
                jwtUtils.setTtl(30*60*60);
                String jwtToken = jwtUtils.createJwtToken(currentUser.getId().toString(), currentUser.getUsername());

                //return new ResultBean(200,currentUser.getUsername());
                //TODO 构建一个map，返回令牌和唯一标识
                Map<String,String > map = new HashMap<> ();
                map.put("jwtToken",jwtToken);
                map.put("username",currentUser.getUsername());
                return new ResultBean(200,map);
            }
        }
        return new ResultBean(404,null);
    }

    @Override
    public ResultBean CheckIsLogin(String uuid) {
        //拼接key
        //StringBuilder key =new StringBuilder("user:token").append(uuid);
        //查询Redis
        //String username = (String) redisTemplate.opsForValue().get(key.toString());
      /*  if(username != null){
            //刷新凭证的有效期
            redisTemplate.expire(key.toString(),30,TimeUnit.MINUTES);
            return new ResultBean(200,username);
        }*/

        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecretKey("java1907");
        //解析令牌
        try {
            Claims claims = jwtUtils.parseJwtToken(uuid);
            String username = claims.getSubject();
            return new ResultBean(200,username);
        }catch (RuntimeException e){
            return new ResultBean(400,null);
        }
    }


}
