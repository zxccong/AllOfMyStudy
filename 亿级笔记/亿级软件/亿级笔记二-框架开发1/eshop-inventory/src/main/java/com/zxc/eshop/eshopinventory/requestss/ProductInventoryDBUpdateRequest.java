package com.zxc.eshop.eshopinventory.requestss;

import com.zxc.eshop.eshopinventory.model.ProductInventory;
import com.zxc.eshop.eshopinventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修改对应商品的库存
 *
 * cache aside pattern
 *
 * (1) 删除缓存
 *
 * （2）更新数据库
 *
 *
 */
public class ProductInventoryDBUpdateRequest implements Request{

    /**
     * 商品库存
     */
    private ProductInventory productInventory;

    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {

        System.out.println("===============日志============：数据库更新请求开始执行商品id="+productInventory.getProductId()+",商品库存数量="+productInventory.getInventoryCnt());
        //1.删除缓存
        productInventoryService.removeProductInventoryCache(productInventory);
        System.out.println("=========日志=============已经删除缓存");

//        //为模拟演示先删除redis中的缓存，然后还没更新到数据库时候，请求过来了，这里人工sleep一下
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //2修改数据库的库存
        productInventoryService.updateProductInventory(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
