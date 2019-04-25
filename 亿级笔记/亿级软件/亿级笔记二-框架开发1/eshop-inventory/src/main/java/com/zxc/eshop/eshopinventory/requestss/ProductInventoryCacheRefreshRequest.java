package com.zxc.eshop.eshopinventory.requestss;

import com.zxc.eshop.eshopinventory.model.ProductInventory;
import com.zxc.eshop.eshopinventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductInventoryCacheRefreshRequest implements Request {

    /**
     * 商品库存
     */
    private Integer productId;

    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;
    /**
     * 是否强制刷新缓存
     */
   private boolean forceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId, ProductInventoryService productInventoryService, boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        //从数据库中查询最新的商品库存数量
        ProductInventory productInventory=productInventoryService.findProductInventory(productId);
        System.out.println("======日志========，查询到商品最新库存数量,商品id="+productInventory.getProductId()+"商品库存为："+productInventory.getInventoryCnt());
        //将最新商品库存数量，刷新到redis缓存中去
        productInventoryService.setProductInventoryCache(productInventory);
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

    @Override
    public Integer getProductId() {
        return productId;
    }
}
