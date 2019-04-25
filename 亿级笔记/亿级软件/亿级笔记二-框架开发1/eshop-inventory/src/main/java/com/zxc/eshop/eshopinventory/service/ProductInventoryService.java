package com.zxc.eshop.eshopinventory.service;

import com.zxc.eshop.eshopinventory.model.ProductInventory;

public interface ProductInventoryService {

    /**
     * 更新商品库存
     * @param productInventory
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除Redis中商品库存的缓存
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 设置商品库存的缓存
     * @param productInventory
     */
    void setProductInventoryCache(ProductInventory productInventory);

    /**
     * 获得商品库存的缓存
     * @return
     */
    ProductInventory getProductInventoryCache(Integer productId);
}
