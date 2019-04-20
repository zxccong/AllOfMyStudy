package com.zxc.eshop.cache.controller;

import com.alibaba.fastjson.JSONObject;
import com.zxc.eshop.cache.model.ProductInfo;
import com.zxc.eshop.cache.model.ShopInfo;
import com.zxc.eshop.cache.prewarm.CachePreWarmThread;
import com.zxc.eshop.cache.rebuild.RebuildCacheQueue;
import com.zxc.eshop.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/test")
    @ResponseBody
    public String  getCachedUserInfo(ProductInfo productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("/testGetCache")
    @ResponseBody
    public ProductInfo testGetCache(Long id){
        return cacheService.getLocalCache(id);
    }

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(Long productId){

        ProductInfo productInfo=null;
         productInfo = cacheService.getProductInfoFromRedisCache(productId);
         if (productInfo!=null) {
             System.out.println("==========日志===========：从Redias获得商品缓存" + productInfo);
         }
         if (productInfo==null){
             productInfo = cacheService.getProductInfoFromLocalCache(productId);
             if (productInfo!=null) {
                 System.out.println("==========日志===========：从本地ehcache获得商品缓存" + productInfo);
             }
         }

         if (productInfo==null){
             //从数据库拉取缓存
             String productInfoJSON = "{\"id\":"+productId+",\"name\": \"iphone8手机\",\"price\":5599,\"shopId\":1,\"modifiedTime\":\"2019-01-01 12:01:00\"}";
              productInfo = JSONObject.parseObject(productInfoJSON,ProductInfo.class);
            //将内存推到内存队列中
             RebuildCacheQueue rebuildCacheQueue=RebuildCacheQueue.getInstance();
             rebuildCacheQueue.addProductInfo(productInfo);

         }

        return productInfo;
    }

    @RequestMapping("/getShopInfo")
    @ResponseBody
    public ShopInfo getShopInfo(Long shopId){
        ShopInfo shopInfo=null;
        shopInfo = cacheService.getShopInfoFromRedisCache(shopId);
        System.out.println("==========日志===========：从Redis获得商店缓存"+shopInfo);
        if (shopInfo==null){
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            System.out.println("==========日志===========：从本地ehcache获得商店缓存"+shopInfo);
        }

        if (shopInfo==null){
            //从数据库拉取缓存

        }

        return shopInfo;
    }


    @RequestMapping("/prewarmCache")
    @ResponseBody
    public void prewarmCache(){
        new CachePreWarmThread().start();
    }

}
