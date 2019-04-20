package com.zxc.eshop.cache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.zxc.eshop.cache.model.ProductInfo;
import com.zxc.eshop.cache.model.ShopInfo;
import com.zxc.eshop.cache.service.CacheService;
import com.zxc.eshop.cache.spring.SpringContext;
import com.zxc.eshop.cache.zk.ZooKeeperSession;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * kafka消息处理线程
 */
public class KafkaMessageProcessor implements Runnable {

    private KafkaStream kafkaStream;

    private CacheService cacheService;

    private static SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public KafkaMessageProcessor(KafkaStream kafkaStream) {
        this.kafkaStream = kafkaStream;
        this.cacheService= (CacheService)SpringContext.getApplicationContext().getBean("cacheService");
    }

    public void run() {
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
            String message = new String(it.next().message());

             //将message转换为json对象
            JSONObject messageJSONObject = JSONObject.parseObject(message);

            //从这里出去消息对应的服务标识
            String serviceId = messageJSONObject.getString("serviceId");

            //如果是商品信息服务
            if (("productInfoService".equals(serviceId))){
                processProductInfoChangeMessage(messageJSONObject);
            }else if (("shopInfoService").equals(serviceId)){
                processShopInfoChangeMessage(messageJSONObject);
            }
        }
    }

    /**
     * 处理商品信息变更的消息
     * @param messageJSONObject
     */
    private void processProductInfoChangeMessage(JSONObject messageJSONObject){

        //提取出商品Id
        Long productId = messageJSONObject.getLong("productId");

        //调用商品服务接口
        //直接用注释模拟

        String productInfoJSON = "{\"id\":4,\"name\": \"iphone8手机\",\"price\":5599,\"shopId\":1,\"modifiedTime\":\"2019-01-01 12:00:00\"}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON,ProductInfo.class);

        //在写入Redis缓存之前，应该获取一个zk分布式锁
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
        zkSession.acquireDistributedLock(productId);

        //获取到了锁
        ProductInfo existedProductInfo = cacheService.getProductInfoFromRedisCache(productId);

        if (existedProductInfo!=null){
            //比较当前数据时间版本比已有数据时间版是新还是旧
            try {
                Date date= sdf.parse(productInfo.getModifiedTime());
                Date existedDate =sdf.parse(existedProductInfo.getModifiedTime());
                if (date.before(existedDate)){
                    System.out.println("==========日志===========：current date["+productInfo.getModifiedTime()+"] is before  "+existedProductInfo.getModifiedTime());
                    return;
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
        zkSession.releaseDistributedLock(productId);
    }


    /**
     * 处理店铺信息变更
     * @param messageJSONObject
     */
    private void processShopInfoChangeMessage(JSONObject messageJSONObject){

        //提取出商品Id
        Long productId = messageJSONObject.getLong("productId");
        Long shopId = messageJSONObject.getLong("shopId");

        //调用商品服务接口
        //直接用注释模拟

        String shopInfoJSON = "{\"id\":1,\"name\": \"手机店\",\"level\":3}";
        ShopInfo shopInfoInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);

        //调用商品服务
        cacheService.saveShopInfo2LocalCache(shopInfoInfo);
        System.out.println("==========日志===========：获取刚保存到本地的店铺信息"+cacheService.getShopInfoFromLocalCache(shopInfoInfo.getId()));
        cacheService.saveShopInfo2RedisCache(shopInfoInfo);

    }

}
