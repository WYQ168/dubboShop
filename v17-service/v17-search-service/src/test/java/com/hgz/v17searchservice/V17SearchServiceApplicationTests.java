package com.hgz.v17searchservice;




import com.hgz.api.ISearchService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SearchServiceApplicationTests {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private ISearchService searchService;

    @Test
    public void syncAllData(){
        ResultBean resultBean = searchService.syncAllData();
        System.out.println(resultBean.getData());
    }
    @Test
    public void synByIdTest(){
        ResultBean resultBean = searchService.synById(21L);
        System.out.println(resultBean.getData());
    }

    @Test
    public void querySolrTest(){
        ResultBean resultBean = searchService.queryByKeywords("华为p30");
        List<TProduct> products = (List<TProduct>) resultBean.getData();
        for (TProduct product : products) {
            System.out.println(product.getPrice());
        }
    }
    @Test
    public void contextLoads() {
    }

    @Test
    public void addOrUpdateTest() throws IOException, SolrServerException {
        //创建一个document对象
        SolrInputDocument document = new SolrInputDocument();
        //添加属性
        document.setField("id",9);
        document.setField("product_name","苹果888");
        document.setField("product_price",4499);
        document.setField("product_sale_point","便宜啊");
        document.setField("product_images","444433333");
        //保存
        solrClient.add("collection2",document);
        solrClient.commit("collection2",true,true);

    }
    @Test
    public void query() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("product_name:苹果888");
        QueryResponse queryResponse = solrClient.query("collection2",query);
        SolrDocumentList results = queryResponse.getResults();
        long numFound = results.getNumFound();
        System.out.println(numFound);
        for (SolrDocument result : results) {
            System.out.println("product_name-->"+result.get("product_name"));
        }
    }

    @Test
    public void delById() throws IOException, SolrServerException {
        solrClient.deleteById("3");
        solrClient.commit();
    }

    @Test
    public void delByQuery() throws IOException, SolrServerException {
        solrClient.deleteByQuery("product_name:小米MIX");
        solrClient.commit();
    }


}
