package com.hgz.v17hgzspingbootredis.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Component
public class GoogleBloomFilter {

    private BloomFilter bloomFilter;

    @PostConstruct
    public void initBloomFilter(){
        //1.模拟获取到原始数据
        List<Long> sourceList = new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            sourceList.add(i);
        }

        bloomFilter = BloomFilter.create(Funnels.longFunnel(),sourceList.size(),0.001);

        for (Long s : sourceList) {
            bloomFilter.put(s);
        }
    }
    public boolean isExists(Long id){
        return bloomFilter.mightContain(id);
    }


}
