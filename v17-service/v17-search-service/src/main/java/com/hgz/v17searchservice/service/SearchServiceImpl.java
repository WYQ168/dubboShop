package com.hgz.v17searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ISearchService;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.pojo.PageResultBean;
import com.hgz.entity.TProduct;
import com.hgz.mapper.TProductMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private TProductMapper productMapper;

    @Override
    public ResultBean syncAllData() {
        List<TProduct> list = productMapper.list();
        for (TProduct product : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", product.getId());
            document.setField("product_name", product.getName());
            document.setField("product_price", product.getPrice());
            document.setField("product_sale_point", product.getSalePoint());
            document.setField("product_images", product.getImages());

            try {
                solrClient.add("collection2",document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new ResultBean(500, "数据同步失败");
            }

            try {
                solrClient.commit("collection2",true,true);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new ResultBean(500, "数据同步失败");
            }
        }
        return new ResultBean(200, "数据同步成功");
    }

    @Override
    public ResultBean synById(Long id) {
        //1.查询源数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //2.MySQL-》solr
        //product -> docuement
        SolrInputDocument document = new SolrInputDocument();
        //2.设置相关的属性值
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_images",product.getImages());
        //3.保存
        try {
            solrClient.add("collection2",document);
            solrClient.commit("collection2",true,true);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean(500,"同步数据失败！");
        }
        return new ResultBean(200,"同步数据成功！");
    }

    @Override
    public ResultBean delById(Long id) {
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit("collection2",true,true);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean(500,"同步数据失败！");
        }
        return new ResultBean(200,"同步数据成功！");
    }

    @Override
    public ResultBean queryByKeywords(String keywords) {
        //1.查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(keywords == null || "".equals(keywords.trim())){
            queryCondition.setQuery("product_name:华为");
        }else{
            queryCondition.setQuery("product_name:"+keywords);
        }
        //设置高亮信息
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");


        //2.获取结果
        List<TProduct> list = null;
        try {
            QueryResponse response = solrClient.query("collection2",queryCondition);
            SolrDocumentList results = response.getResults();
            //results -> List
            list = new ArrayList<>(results.size());
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : results) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
               // product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());
                //TODO 传递product对象不合适，因为页面展示只需要4个字段

                //拿到高亮信息
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighlights = map.get("product_name");
                if(productNameHighlights!=null &&productNameHighlights.size()>0){
                        product.setName(productNameHighlights.get(0));
                }else{
                    product.setName(document.getFieldValue(" product_name").toString());
                }
                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean(500,null);
        }
        return new ResultBean(200,list);
    }

    @Override
    public ResultBean queryByKeywords(String keywords, Integer pageIndex, Integer pageSize) {
        //1.查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(keywords == null || "".equals(keywords.trim())){
            queryCondition.setQuery("product_name:华为");
        }else{
            queryCondition.setQuery("product_name:"+keywords);
        }
        //设置高亮信息
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //设置分页信息
        queryCondition.setStart((pageIndex-1)*pageSize);
        queryCondition.setRows(pageSize);
        //2.获取结果
        List<TProduct> list = null;

        PageResultBean<TProduct> pageResultBean =  new PageResultBean<>();
        long total =0;
        try {
            QueryResponse response = solrClient.query("collection2",queryCondition);
            SolrDocumentList results = response.getResults();

            total = results.getNumFound();

            //results -> List
            list = new ArrayList<>(results.size());
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : results) {
                //document -> product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                // product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());
                //TODO 传递product对象不合适，因为页面展示只需要4个字段

                //拿到高亮信息
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighlights = map.get("product_name");
                if(productNameHighlights!=null &&productNameHighlights.size()>0){
                    product.setName(productNameHighlights.get(0));
                }else{
                    product.setName(document.getFieldValue(" product_name").toString());
                }
                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean(500,null);
        }
        //给page赋值
        pageResultBean.setList(list);
        pageResultBean.setPageNum(pageIndex);
        pageResultBean.setTotal(total);
        pageResultBean.setPageSize(pageSize);
        pageResultBean.setPages((int) (total%pageSize==0?total%pageSize:(total/pageSize)+1));


        return new ResultBean(200,pageResultBean);
    }
}
