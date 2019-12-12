package com.hgz.v17cartservice.service;
import	java.util.ArrayList;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ICartService;
import com.hgz.commons.base.ResultBean;
import com.hgz.pojo.CartItem;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CartServiceImpl implements ICartService {

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public ResultBean add(String token, Long productId, Integer count) {
        //根据token查询购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        //当购物车已经存在，则更新数量
        if (cart.size() != 0) {
            //如果当前的购物车拥有商品，则更新数量
            if(redisTemplate.opsForHash().hasKey(key.toString(),productId)){
                //拿到该商品信息
                CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
                //更新数量
                cartItem.setCount(cartItem.getCount()+count);
                //更新时间
                cartItem.setUpdateTime(new Date());
                //保存变更的商品信息
                redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
                //设置有效期
                redisTemplate.expire(key.toString(),30, TimeUnit.DAYS);

                return new ResultBean(200,true);
            }
        }
        //否则直接添加到商品到购物车
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setCount(count);
        cartItem.setUpdateTime(new Date());
        //添加商品到购物车
        redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
        //设置有效期
        redisTemplate.expire(key.toString(),30, TimeUnit.DAYS);
        return new ResultBean(200,true);
    }

    @Override
    public ResultBean del(String token, Long productId) {
        //拿到key后 删除
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Long delete = redisTemplate.opsForHash().delete(key.toString(), productId);
        if(delete != null){
            return new ResultBean(200,true);
        }
        return new ResultBean(400,false);
    }

    @Override
    public ResultBean update(String token, Long productId, Integer count) {
        //根据token查询购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
        if(cartItem != null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key.toString(),productId,new CartItem(productId,count,new Date()));
            return new ResultBean(200,true);
        }
        return new ResultBean(400,false);
    }

    @Override
    public ResultBean query(String token) {
        //根据token查询购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        if(cart.size() != 0){
            //1、获取到values，但是并没有按照时间排序
            Collection<Object> values = cart.values();
            //实现排序
            TreeSet<CartItem> treeSet = new TreeSet<>();
            for (Object value : values) {
                CartItem cartItem = (CartItem) value;
                treeSet.add(cartItem);
            }
            //将treeSet转换为list
            List<CartItem> result = new ArrayList<> (treeSet);
            return new ResultBean(200,result);
        }
        return new ResultBean(400,null);
    }

    @Override
    public ResultBean merge(String noLoginKey, String loginKey) {
        //判断未登录购物车是否存在
        Map<Object, Object> noLoginCart = redisTemplate.opsForHash().entries(noLoginKey);
        if(noLoginCart.size() == 0){
            //无需合并
            return new ResultBean(200,"不需要合并");
        }
        //判断已登录购物车是否存在
        Map<Object, Object> loginCart = redisTemplate.opsForHash().entries(loginKey);
        if(loginCart.size() == 0){
            //把未登录的购物车变成已经登陆的购物车
            redisTemplate.opsForHash().putAll(loginKey,noLoginCart);
            //删除未登录的购物车
            redisTemplate.opsForHash().delete(noLoginKey);
            return new ResultBean(200,"合并购物车");
        }
        //两种状态的购物车都存在，需要比较合并
        //相同的商品需要叠加，否则直接添加
        //以已登录的购物车为基准，遍历未登录的购物车，不断做比较
        noLoginCart.forEach((productId,cartItem) -> {
            CartItem loginCartItem= (CartItem) redisTemplate.opsForHash().get(loginKey, productId);
            if(loginCartItem != null){
                //已登录购物车有商品，则进行叠加
                CartItem noLoginCartItem = (CartItem) cartItem;
                loginCartItem.setCount(loginCartItem.getCount()+noLoginCartItem.getCount());
                loginCartItem.setUpdateTime(new Date());
            }else{
                //不存在直接添加
                redisTemplate.opsForHash().put(loginKey,productId,cartItem);
            }

        });
        return null;
    }
}
