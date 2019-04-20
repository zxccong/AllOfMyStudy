package com.zxc.eshop.eshopinventory.thread;

import com.zxc.eshop.eshopinventory.requestss.Request;
import com.zxc.eshop.eshopinventory.requestss.RequestQueues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestProcessorThreadPool {

    /**
     * 线程池
     */
    private ExecutorService threadPool =  Executors.newFixedThreadPool(10);




    public RequestProcessorThreadPool(){
        RequestQueues requestQueues= RequestQueues.getInstance();
        for (int i=0;i<10;i++){
            ArrayBlockingQueue<Request> queue =new ArrayBlockingQueue<>(100);
            requestQueues.addQueue(queue);
            threadPool.submit(new WorkerThread(queue));
        }
    }


    /**
     * 绝对线程安全的一种单例的方式
     *
     * 静态内部类的方式，初始化单例
     */
    private  static class Singleton{

        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance(){
            return instance;
        }

    }

    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类初始化，一定只会发生一次，不管多个线程去并发初始化
     * @return
     */
    public static RequestProcessorThreadPool getInstance(){
        return Singleton.getInstance();
    }

    /**
     * 初始化的便捷方法
     */
    public static void init(){
        getInstance();
    }

}
