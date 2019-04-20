package com.zxc.eshop.cache.rebuild;

import com.zxc.eshop.cache.model.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 重建缓存的内存队列
 */
public class RebuildCacheQueue {

    private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<ProductInfo>(1000);

    public void addProductInfo(ProductInfo productInfo){
        try {
            queue.put(productInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProductInfo takeProductInfo(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  static class Singleton{
        private static RebuildCacheQueue  instance;

        static {
            instance = new RebuildCacheQueue();
        }

        public static  RebuildCacheQueue getInstance(){
            return instance;
        }

    }

    public static  RebuildCacheQueue getInstance(){
        return Singleton.getInstance();
    }

    public static  void init(){
        getInstance();
    }
}
