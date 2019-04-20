package com.zxc.eshop.eshopinventory.requestss;

public interface Request {

    void process();
    Integer getProductId();
    boolean isForceRefresh();

}
