package com.zxc.eshop.eshopinventory.service;

import com.zxc.eshop.eshopinventory.requestss.Request;

/**
 * 请求异步执行的service
 */
public interface RequestAsyncProcessService {

    void process(Request request);

}
