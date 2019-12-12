package com.hgz.api;

import com.github.pagehelper.PageInfo;
import com.hgz.entity.TProduct;
import com.hgz.commons.base.IBaseService;
import com.hgz.vo.TProductVO;

import java.util.List;

/**
 * @author huangguizhao
 */
public interface IProductService extends IBaseService<TProduct> {

    public PageInfo<TProduct> page(Integer pageIndex,Integer pageSize);

    public Long add(TProductVO vo);

    public Long update(TProductVO vo);

    int delByIds(List<Long> ids);
}
