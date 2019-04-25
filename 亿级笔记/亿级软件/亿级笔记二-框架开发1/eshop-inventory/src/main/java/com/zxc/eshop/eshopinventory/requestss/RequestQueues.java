package com.zxc.eshop.eshopinventory.requestss;

import com.zxc.eshop.eshopinventory.thread.RequestProcessorThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求内存队列
 */
public class RequestQueues {

    /**
     * 内存队列
     */
    private List<ArrayBlockingQueue<Request>> queues=new ArrayList<ArrayBlockingQueue<Request>>();

    /**
     *标识位：表示请求之前是否有读取请求
     *
     */
    private Map<Integer,Boolean> flagMap = new ConcurrentHashMap<>();

    /**
     * 绝对线程安全的一种单例的方式
     *
     * 静态内部类的方式，初始化单例
     */
    private  static class Singleton{

        private static RequestQueues instance;

        static {
            instance = new RequestQueues();
        }

        public static RequestQueues getInstance(){
            return instance;
        }

    }

    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类初始化，一定只会发生一次，不管多个线程去并发初始化
     * @return
     */
    public static RequestQueues getInstance(){
        return RequestQueues.Singleton.getInstance();
    }

    /**
     * 初始化的便捷方法
     */
    public static void init(){
        getInstance();
    }

    /**
     * 添加内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue){
        this.queues.add(queue);
    }

    /**
     * 获取内存中队列的数量
     * @return
     */
    public int queueSize(){
        return queues.size();
    }

    public ArrayBlockingQueue<Request> getQueue(int index){
        return queues.get(index);
    }

    public Map<Integer,Boolean> getFlagMap(){
        return flagMap;
    }
}
