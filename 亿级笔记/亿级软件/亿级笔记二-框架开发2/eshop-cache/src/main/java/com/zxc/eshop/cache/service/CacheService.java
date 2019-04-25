package com.zxc.eshop.cache.service;

import com.alibaba.fastjson.JSONObject;
import com.zxc.eshop.cache.mapper.CacheMapper;
import com.zxc.eshop.cache.model.ProductInfo;
import com.zxc.eshop.cache.model.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

/**
 *
 */
@Service("cacheService")
public class CacheService {

    public static final String CACHE_NAME = "local";

    @Autowired
    private JedisCluster jedisCluster;


    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo){
        return productInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public  ProductInfo getLocalCache(Long id){
        return null;
    }

    /**
     * 将商品信息写到本地缓存中去
     * @param productInfo
     */
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo){
        return productInfo;
    }


    /**
     * 将商品信息写到本地缓存中去
     * @param shopInfo
     */
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo){
          return  shopInfo;
    }


    /**
     * 从本地缓存中读取商品信息
     * @param productId
     */
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Long productId){
        return null;
    }


    /**
     * 从本地缓存中读取商店信息
     * @param shopId
     */
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Long shopId){
        return  null;
    }

    /**
     * 将商品信息写到redis中
     */
    public void saveProductInfo2RedisCache(ProductInfo productInfo){
        String key="product_info_"+productInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));
    }

    /**
     * 将商品信息写到redis中
     */
    public void saveShopInfo2RedisCache(ShopInfo shopInfo){
        String key="shop_info_"+shopInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));
    }

    /**
     * 从Redis读取商品信息
     * @param productId
     */
    public ProductInfo getProductInfoFromRedisCache(Long productId){
        String key="product_info_"+productId;
        String json = jedisCluster.get(key);
        if (json!=null){
            return JSONObject.parseObject(json,ProductInfo.class);
        }else {
            return null;
        }

    }


    /**
     * 从Redis中读取店铺信息
     * @param shopId
     */
    public ShopInfo getShopInfoFromRedisCache(Long shopId){
        String key="shop_info_"+shopId;
        String json = jedisCluster.get(key);
        if (json!=null){
            return JSONObject.parseObject(json,ShopInfo.class);
        }else {
            return null;
        }


    }

}
