package com.hgz;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

public class JedisTest {

    @Test
    public void StringTest(){
        Jedis jedis = new Jedis("193.112.19.133",6379);
        jedis.auth("java1907");
        jedis.set("k1","v1");
        String k1 = jedis.get("k1");
        System.out.println(k1);

        jedis.mset("name","wyq","age","19");
        List<String> mget = jedis.mget("name", "age");
        System.out.println(mget);

    }
}
