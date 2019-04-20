package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tarantool.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


import static java.lang.System.getProperties;

public class DemoApplicationTests {
    private static final String HOST_PROPERTY = "192.168.222.135";
    private static final String PORT_PROPERTY = "3301";
    private static final String SPACE_PROPERTY = "tester";
    private static final String DEFAULT_HOST = "192.168.222.137";
    private static final String DEFAULT_PORT = "3301";
    private static final String DEFAULT_SPACE = "512";

    private static int spaceNo;

    public static void main(String[] args) {


        Properties props = getProperties();
        int port = Integer.parseInt(props.getProperty(PORT_PROPERTY, DEFAULT_PORT));
        String host = props.getProperty(HOST_PROPERTY, DEFAULT_HOST);
        spaceNo = Integer.parseInt(props.getProperty(SPACE_PROPERTY, DEFAULT_SPACE));

//        config.username = "test";
//        config.password = "test";

        TarantoolClientConfig config = new TarantoolClientConfig();
        SocketChannelProvider socketChannelProvider = new SocketChannelProvider() {
            @Override
            public SocketChannel get(int retryNumber, Throwable lastError) {
                if (lastError != null) {
                    lastError.printStackTrace(System.out);
                }
                try {
                    System.out.println("开始连接");
                    SocketChannel channel = SocketChannel.open(new InetSocketAddress("192.168.222.137", 3301));
                    System.out.println("连接成功");
                    return channel;

                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }

            }
        };
        TarantoolClient client = new TarantoolClientImpl(socketChannelProvider, config);
//用来记录完成时间：个数的Map
        Map<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(10, 0);
        hashMap.put(100, 0);
        hashMap.put(10000, 0);


        //所有的执行开始时间
        long start1 = System.currentTimeMillis();
        //线程数量
        long num = 1000;
        Thread[] threads = new Thread[Integer.parseInt(String.valueOf(num))];

        for (int j = 0; j < num; j++) {


            threads[j] =  new Thread(() -> {
                long start = System.currentTimeMillis();
                int count = 1;
                System.out.println(Thread.currentThread().getName() + ":start");

                List<?> list = client.syncOps().select(spaceNo, 0, Arrays.asList(1), 0, 1, 0);
//                System.out.println(list);
                long end = System.currentTimeMillis();
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

        Double time2 = Double.parseDouble(String.valueOf(end1 - start1))  ;
        System.out.println("all end in: " + time2);
        System.out.println("10ms end count: " + hashMap.get(10));
        System.out.println("100ms end count: " + hashMap.get(100));
        System.out.println("else end count: " + hashMap.get(10000));
        System.out.println("qps is :" + num / (time2/new Long(1000)));


    }







}
