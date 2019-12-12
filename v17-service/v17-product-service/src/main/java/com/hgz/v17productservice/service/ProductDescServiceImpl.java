package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IProductDescService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProductDesc;
import com.hgz.mapper.TProductDescMapper;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class ProductDescServiceImpl extends BaseServiceImpl<TProductDesc> implements IProductDescService {

    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public TProductDesc selectByProductId(Long id) {
        return productDescMapper.selectByProductId(id);
    }

    @Override
    public IBaseDao<TProductDesc> getBaseDao() {
        return productDescMapper;
    }
}
