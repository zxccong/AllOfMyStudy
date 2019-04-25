package com.example.demo;

import com.sun.jmx.snmp.tasks.ThreadService;
import org.tarantool.SocketChannelProvider;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.getProperties;

public class RedisApplicationTests2 {

    private static Integer THREAD_SIZE = 10;
    private static Integer QUERY_SIZE = 100000;
    static BlockingQueue<Object> queue = new LinkedBlockingDeque<>();
    static AtomicInteger count = new AtomicInteger(0);
    static TarantoolClient client = null;
    static Integer spaceNo = 512;
    static TarantoolClientConfig config = new TarantoolClientConfig();

    public static void main(String[] args) throws Exception {

//        start = System.currentTimeMillis();
//        withoutPipeline();
//        end = System.currentTimeMillis();
//        System.out.println("withoutPipeline:" + (end - start));
        client = init();
        ExecutorService service = Executors.newFixedThreadPool(THREAD_SIZE);
        for (int i = 0; i < THREAD_SIZE; i++) {
            service.submit(() -> RedisApplicationTests2.withoutPipeline());
        }
        long start = System.currentTimeMillis();
//        while (list.size()<=1000*1000) {
//            Thread.sleep(1000);
//            System.out.println("list"+list.size());
//        }.
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        list.stream().forEach(System.out::println);
        long end = System.currentTimeMillis();
        System.out.println("usePipeline:" + (end - start) + ",list size " + queue.size() + ",count size " + count);

    }

    public static TarantoolClientImpl init() {
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
            return new TarantoolClientImpl(socketChannelProvider, config);
    }

    public static void withoutPipeline() {
        try {
            Jedis jedis = new Jedis("192.168.222.135", 6400);
            for (int i = 0; i < QUERY_SIZE; i++) {
                queue.add(jedis.get("test2"));
            }
            jedis.disconnect();
        } catch (Exception e) {
        }
    }

    public static void usePipeline() {
        try {
            Jedis jedis = new Jedis("192.168.222.135", 6400);
            Pipeline pipeline = jedis.pipelined();
            for (int i = 0; i < QUERY_SIZE; i++) {
                pipeline.get("test2");
            }
            List<Object> objects = pipeline.syncAndReturnAll();
//            System.out.println(objects.size());
            queue.addAll(objects);
            count.incrementAndGet();
            jedis.disconnect();
        } catch (Exception e) {
        }
    }

    public static void useTarantool() {
        for (int i = 0; i < QUERY_SIZE; i++) {
            List<?> list = client.syncOps().select(spaceNo, 0, Arrays.asList(1), 0, 1, 0);
            queue.addAll(list);
        }
    }

}