package com.zxc.eshop.eshopinventory.thread;



import com.zxc.eshop.eshopinventory.requestss.ProductInventoryCacheRefreshRequest;
import com.zxc.eshop.eshopinventory.requestss.ProductInventoryDBUpdateRequest;
import com.zxc.eshop.eshopinventory.requestss.Request;
import com.zxc.eshop.eshopinventory.requestss.RequestQueues;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 */
public class WorkerThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public WorkerThread(ArrayBlockingQueue<Request> queue){
        this.queue=queue;
    }

    @Override
    public Boolean call() throws Exception {

        try {
            while (true){
                //ArrayBlockingQueue
                //Blocking就是说明如果队列满了，或者是空的，那么都会在执行操作的时候，阻塞住
                Request request = queue.take();
                System.out.println("================日志================工作线程处理请求，商品id="+request.getProductId());

                boolean forceRefresh = request.isForceRefresh();
                if (!forceRefresh) {
                    RequestQueues requestQueues = RequestQueues.getInstance();
                    Map<Integer,Boolean> flagMap =requestQueues.getFlagMap();

                    //先做读请求的去重
                    if(request instanceof ProductInventoryDBUpdateRequest){
                        //如果是一个更新数据库的请求，那么就将那个productId对应的表示设置为true

                        flagMap.put(request.getProductId(),true);
                    }else if (request instanceof ProductInventoryCacheRefreshRequest){
                        //如果是缓存刷新的请求，那就判断，表示不为空，而且是true，就说明之前有个商品查询更新请求
                        Boolean flag = flagMap.get(request.getProductId());
                        if(flag!=null&&flag){
                            flagMap.put(request.getProductId(),false);
                        }

                        //如果是缓存刷新请求发现标识不为空，但是标识是false
                        //说明前面已经有一个数据库更新请求+一个缓存刷新请求了
                        if(flag!=null &&!flag){
                            return true ;
                        }

                        if(flag==null){
                            flagMap.put(request.getProductId(),false);
                        }

                    }
                }

                //执行这个request 操作
                request.process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
