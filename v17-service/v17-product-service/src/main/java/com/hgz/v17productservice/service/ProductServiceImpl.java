package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductService;
import com.hgz.entity.TProduct;
import com.hgz.entity.TProductDesc;
import com.hgz.mapper.TProductDescMapper;
import com.hgz.mapper.TProductMapper;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.vo.TProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author huangguizhao
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;


    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        //设置分页
        PageHelper.startPage(pageIndex,pageSize);
        //拿到list集合
        List<TProduct> list = this.list();
        //返回分页信息
        PageInfo<TProduct> pageInfo = new PageInfo<>(list, 3);
        return pageInfo;
    }

    @Override
    @Transactional
    public Long add(TProductVO vo) {
        productMapper.insertSelective(vo.getProduct());
        TProductDesc productDesc = new TProductDesc();
        productDesc.setProductId(vo.getProduct().getId());
        productDesc.setProductDesc(vo.getProductDesc());
        productDescMapper.insertSelective(productDesc);
        return vo.getProduct().getId();
    }

    @Override
    @Transactional
    public Long update(TProductVO vo) {
        int i = productMapper.updateByPrimaryKeySelective(vo.getProduct());
        System.out.println("i--->"+i);
        int i1 = productDescMapper.updateByProductId(vo.getProduct().getId(),vo.getProductDesc());
        System.out.println("i1--->"+i1);
        return vo.getProduct().getId();
    }

    @Override
    public int delByIds(List<Long> ids) {
        return productMapper.updateFlagToFalse(ids);
    }


}
