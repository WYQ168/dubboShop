package com.hgz.api;

import com.hgz.commons.base.ResultBean;

import java.io.IOException;

public interface ISearchService {
/*
做全量同步
在数据初始化的时候使用
 */
    public ResultBean syncAllData();

    /**
     * 增量同步
     * @param id
     * @return
     */
    public ResultBean synById(Long id);

    /**
     * 删除
     * @param id
     * @return
     */
    public ResultBean delById(Long id);

    /**
     * 查询操作
     * @param keywords
     * @return
     */
    public ResultBean queryByKeywords(String keywords);

    ResultBean queryByKeywords(String keywords, Integer pageIndex, Integer pageSize);

}
