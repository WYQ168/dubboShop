package com.hgz.api;

import com.hgz.commons.base.IBaseService;
import com.hgz.entity.TProductDesc;

public interface IProductDescService extends IBaseService<TProductDesc> {
    TProductDesc selectByProductId(Long id);
}
