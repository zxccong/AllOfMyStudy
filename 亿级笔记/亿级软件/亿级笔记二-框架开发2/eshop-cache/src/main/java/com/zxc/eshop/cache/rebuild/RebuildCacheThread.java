package com.zxc.eshop.cache.rebuild;

import com.zxc.eshop.cache.model.ProductInfo;
import com.zxc.eshop.cache.service.CacheService;
import com.zxc.eshop.cache.spring.SpringContext;
import com.zxc.eshop.cache.zk.ZooKeeperSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RebuildCacheThread implements  Runnable {

    private static SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void run() {

        RebuildCacheQueue rebuildCacheQueue= RebuildCacheQueue.getInstance();
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();

        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        while (true){
            ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();
            zkSession.acquireDistributedLock(productInfo.getId());


            ProductInfo existedProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());

            if (existedProductInfo!=null){
                //比较当前数据时间版本比已有数据时间版是新还是旧
                try {
                    Date date= sdf.parse(productInfo.getModifiedTime());
                    Date existedDate =sdf.parse(existedProductInfo.getModifiedTime());
                    if (date.before(existedDate)){
                        System.out.println("==========日志===========：current date["+productInfo.getModifiedTime()+"] is before  "+existedProductInfo.getModifiedTime());
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("==========日志===========：current date["+productInfo.getModifiedTime()+"] is after  "+existedProductInfo.getModifiedTime());
            }else{
                System.out.println("existed product info is null");
            }



            //调用商品服务
            cacheService.saveProductInfo2LocalCache(productInfo);
            System.out.println("==========日志===========：获取刚保存到本地的商品信息"+cacheService.getProductInfoFromLocalCache(productInfo.getId()));
            cacheService.saveProductInfo2RedisCache(productInfo);


            //释放分布式锁
            zkSession.releaseDistributedLock(productInfo.getId());
        }
    }
}
