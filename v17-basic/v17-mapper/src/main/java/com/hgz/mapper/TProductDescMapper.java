package com.hgz.mapper;

import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProductDesc;
import org.apache.ibatis.annotations.Param;

public interface TProductDescMapper extends IBaseDao<TProductDesc> {

    TProductDesc selectByProductId(Long id);


    int updateByProductId(@Param("id") Long id, @Param("productDesc") String productDesc);
}