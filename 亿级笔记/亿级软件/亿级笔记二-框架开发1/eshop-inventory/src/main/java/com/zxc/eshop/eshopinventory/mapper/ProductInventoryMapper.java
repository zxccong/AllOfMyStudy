package com.zxc.eshop.eshopinventory.mapper;

import com.zxc.eshop.eshopinventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

public interface ProductInventoryMapper {

     void updateProductInventory(ProductInventory productInventory);

     ProductInventory findProductInventory(@Param("productId") Integer productId);
}
