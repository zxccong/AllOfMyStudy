package com.zxc.eshop.eshopinventory.service.impl;

import com.zxc.eshop.eshopinventory.requestss.ProductInventoryCacheRefreshRequest;
import com.zxc.eshop.eshopinventory.requestss.ProductInventoryDBUpdateRequest;
import com.zxc.eshop.eshopinventory.requestss.Request;
import com.zxc.eshop.eshopinventory.requestss.RequestQueues;
import com.zxc.eshop.eshopinventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

@Service("requestAsyncProcessService")
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    @Override
    public void process(Request request) {
        try {
            //做请求的路由，根据每一个请求的商品id ，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue =getRoutingQueue(request.getProductId());
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId){

        RequestQueues requestQueues =RequestQueues.getInstance();
        //先获得ProductId的hash值
        String key=String.valueOf(productId);
        int h;
        int hash= (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);


        //对hash值取模，将hash到路由指定的内存队列中
        int index=( requestQueues.queueSize() - 1) & hash;

        System.out.println("===========日志=============:路由内存队列，商品id="+productId+"队列索引="+index);
        return requestQueues.getQueue(index);
    }

}
