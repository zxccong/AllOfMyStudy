package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {
    private static final String HOST_PROPERTY = "192.168.222.135";
    private static final String PORT_PROPERTY = "6400";
//    private static final String SPACE_PROPERTY = "tester";
//    private static final String DEFAULT_HOST = "192.168.222.137";
//    private static final String DEFAULT_PORT = "3301";
//    private static final String DEFAULT_SPACE = "512";

    private static int spaceNo;

    @Autowired
    private JedisUtil jedisUtil;

    @Test
    public void test() {


        Map<Integer, Integer> hashMap = new ConcurrentHashMap<>();
        hashMap.put(10, 0);
        hashMap.put(100, 0);
        hashMap.put(10000, 0);


        //所有的执行开始时间
        long start1 = System.currentTimeMillis();

        long num = 10;
        Thread[] threads = new Thread[Integer.parseInt(String.valueOf(num))];

        for (int j = 0; j < num; j++) {


            threads[j] = new Thread(() -> {

                Jedis jedis = new Jedis("192.168.222.135", 6400);
                Pipeline pipeline = jedis.pipelined();

                long start = System.currentTimeMillis();
                int count = 1;
                System.out.println(Thread.currentThread().getName() + ":start");

                //进行测试
                pipeline.get("k1");
                long end = System.currentTimeMillis();

                //关闭线程
                pipeline.sync();
                jedis.disconnect();

                long time = end - start;
                if (time < 10) {
                    Integer integer = hashMap.get(10);
                    hashMap.put(10, integer + new Integer(1));
                } else if (time < 100) {
                    Integer ten = hashMap.get(100);

                    hashMap.put(100, ten + new Integer(1));
                } else {
                    Integer ten = hashMap.get(10000);
                    hashMap.put(10000, ten + new Integer(1));
                }

//            System.out.println(list);
                count++;

            });
            threads[j].start();

        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        long end1 = System.currentTimeMillis();

        Double time2 = Double.parseDouble(String.valueOf(end1 - start1));
        System.out.println("all end in: " + time2);
        System.out.println("10ms end count: " + hashMap.get(10));
        System.out.println("100ms end count: " + hashMap.get(100));
        System.out.println("else end count: " + hashMap.get(10000));
        System.out.println("qps is :" + num / (time2 / new Long(1000)));


    }


}
