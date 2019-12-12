package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IProductTypeService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProductType;
import com.hgz.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService {

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Resource(name = "redisTemplate1")
    private RedisTemplate redisTemplate;


    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }

    @Override
    public List<TProductType> list() {
        List<TProductType> list = (List<TProductType>) redisTemplate.opsForValue().get("product:list");
        if (list == null || list.size()==0) {
            list = super.list();
            redisTemplate.opsForValue().set("product:list",list);
        }
        return list;
    }
}
