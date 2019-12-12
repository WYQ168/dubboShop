package com.hgz.v17search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ISearchService;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.pojo.PageResultBean;
import com.hgz.entity.TProduct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("syncAllData")
    @ResponseBody
    public ResultBean syncAllData(){
            return searchService.syncAllData();
    }

    @RequestMapping("queryByKeyWords")
    @ResponseBody
    public ResultBean queryByKeyWords(String keywords){
        return searchService.queryByKeywords(keywords);
    }
    @RequestMapping("queryByKeyWords4PC/{pageIndex}/{pageSize}")
    public String queryByKeyWords4PC(String keywords,@PathVariable("pageIndex") Integer pageIndex,
                                     @PathVariable("pageSize") Integer pageSize, Model model){
        ResultBean resultBean = searchService.queryByKeywords(keywords,pageIndex,pageSize);
        if("200".equals(resultBean.getStatusCode().toString())){
            //List<TProduct> list = (List<TProduct>) resultBean.getData();
            PageResultBean<TProduct> pageResultBean = (PageResultBean<TProduct>) resultBean.getData();
            model.addAttribute("page",pageResultBean);
        }
        return "list";
    }
}
