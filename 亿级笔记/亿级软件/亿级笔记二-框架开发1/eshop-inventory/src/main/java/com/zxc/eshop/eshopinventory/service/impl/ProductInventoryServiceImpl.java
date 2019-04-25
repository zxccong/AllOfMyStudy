package com.zxc.eshop.eshopinventory.service.impl;

import com.zxc.eshop.eshopinventory.dao.RedisDao;
import com.zxc.eshop.eshopinventory.mapper.ProductInventoryMapper;
import com.zxc.eshop.eshopinventory.model.ProductInventory;
import com.zxc.eshop.eshopinventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("ProductInventoryService")
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private RedisDao redisDao;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
        System.out.println("===========日志============：已修改数据库库存商品Id="+productInventory.getProductId()+"库存为："+productInventory.getInventoryCnt());
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key="product:"+productInventory.getProductId();

        redisDao.delete(key);
        System.out.println("==========日志============已经删除redis的缓存key="+key);
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key="product:"+productInventory.getProductId();
        redisDao.set(key,String.valueOf(productInventory.getInventoryCnt()));
        System.out.println("=============日志=============：已更新商品库存的缓存商品id="+productInventory.getProductId()+"库存为"+productInventory.getInventoryCnt()+",key="+key);
    }

    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key="product:"+productId;
        String result=redisDao.get(key);

        try {
            if(result!=null&&!"".equals(result)){
                inventoryCnt =Long.valueOf(result);
                return new ProductInventory(productId,inventoryCnt);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;

    }
}
