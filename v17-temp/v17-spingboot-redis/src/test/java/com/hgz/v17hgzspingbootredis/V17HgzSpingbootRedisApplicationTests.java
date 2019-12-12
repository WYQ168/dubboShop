package com.hgz.v17hgzspingbootredis;

import com.google.common.hash.BloomFilter;
import com.hgz.v17hgzspingbootredis.entity.Student;
import com.hgz.v17hgzspingbootredis.filter.GoogleBloomFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17HgzSpingbootRedisApplicationTests {

    @Resource(name = "redisTemplate1")
    private RedisTemplate redisTemplate;

    @Autowired
    private GoogleBloomFilter googleBloomFilter;

    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("k222","v11");
        Object v1 = redisTemplate.opsForValue().get("k222");
        System.out.println(v1);
    }

    @Test
    public void cacheTest(){
        List<Student> StudentList = (List<Student>) redisTemplate.opsForValue().get("StudentList");
        System.out.println(StudentList);
        //只有获得锁的线程，才能去查询数据库
        //采用redis的分布式锁来实现

        if(StudentList==null){
            String uuid = UUID.randomUUID().toString();
            //在Redis4.0以上版本
            // redisTemplate.opsForValue().setIfAbsent("lock",uuid,1,TimeUnit.MINUTES);
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid);
            if(lock) {
                redisTemplate.expire("StudentList", 1, TimeUnit.MINUTES);

                System.out.println("从数据库中去取");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StudentList = new ArrayList<>();
                StudentList.add(new Student(29, "po"));
                StudentList.add(new Student(19, "tto"));
                redisTemplate.opsForValue().set("StudentList", StudentList);

                //释放锁
                Object lock1 = redisTemplate.opsForValue().get("lock");
                if (lock1.equals(uuid)) {
                    redisTemplate.delete("lock");
                }
            }
        }else{
            System.out.println("从缓存中取。。。。");
            for (Student student : StudentList) {
                System.out.println(student.getName());
            }
        }
    }

    @Test
    public void threadTest(){
        ExecutorService pool =
                new ThreadPoolExecutor(4,8,100,TimeUnit.MINUTES,
                        new LinkedBlockingQueue<Runnable>(100));
        for (int i = 0; i < 100; i++) {
            pool.submit(new Runnable() {
                public void run() {
                    cacheTest();
                }
            });
        }
    }

    @Test
    public void lockScriptTest(){
        //1.创建一个可以执行lua脚本的执行对象
        DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
        //2.获得要执行的脚本
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock.lua")));
        //3.设置返回类型
        lockScript.setResultType(Boolean.class);
        //4.封装参数
        List<String> keyList = new ArrayList<>();
        keyList.add("lock");
        String uuid= UUID.randomUUID().toString();
        keyList.add(uuid);
        keyList.add("60");
        //5.执行脚本
        Boolean result = (Boolean) redisTemplate.execute(lockScript, keyList);
        System.out.println("result--->"+result);
        Long expire = redisTemplate.getExpire("lock");
        System.out.println("expire--->"+expire);
    }

    @Test
    public void testGoogleBloomFilterTest(){
        int count = 0;
        for (long i = 101; i < 1000; i++) {
            boolean exists = googleBloomFilter.isExists(i);
            if(exists){
                count++;
            }
        }

        System.out.println(count);
    }

    @Test
    public void bloomAddTest(){
        //1.创建一个可以执行lua脚本的执行对象
        DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
        //2.获取要执行的脚本
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("add.lua")));
        //3.设置返回类型
        lockScript.setResultType(Boolean.class);
        //4.封装参数
        List<String> keyList = new ArrayList<>();
        keyList.add("myBloomFilter2");
        keyList.add("java");
        //5.执行脚本
        Boolean result = (Boolean) redisTemplate.execute(lockScript, keyList);
        System.out.println(result);
    }

}
