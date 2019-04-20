package com.zxc.eshop.cache.prewarm;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zxc.eshop.cache.model.ProductInfo;
import com.zxc.eshop.cache.service.CacheService;
import com.zxc.eshop.cache.spring.SpringContext;
import com.zxc.eshop.cache.zk.ZooKeeperSession;

public class CachePreWarmThread extends Thread {


    @Override
    public void run() {
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();

        String taskidList =zkSession.getNodeData("/taskid-list");

        System.out.println("[CachePreWarmThread获取到taskis列表]taskidList="+taskidList);

        if (taskidList !=null && !"".equals(taskidList)){
            String[] taskidListSplisted = taskidList.split(",");
            for (String taskid :taskidListSplisted) {
                String taskidLockPath="/taskid-lock-"+taskid;
                boolean result = zkSession.acquireFastFailedDistributedLock(taskidLockPath);
                if(!result){
                    continue;
                }

                String taskidStatusLockPath="/taskid-status-lock-"+taskid;
                zkSession.acquireDistributedLock(taskidStatusLockPath);

                String taskidStatus = zkSession.getNodeData("/taskid-status-" + taskid);
                System.out.println("[CachePreWarmThread获取task的预热状态]taskid="+taskid+",status="+taskidStatus);

                if("".equals(taskidStatus)){
                    String productidList=zkSession.getNodeData("/task-hot-product-list-"+taskid);
                    System.out.println("[CachePreWarmThread获取到task的热门商品列表]productidList="+productidList);
                    JSONArray productidJSONArray= JSONArray.parseArray(productidList);
                    for(int i = 0;i<productidJSONArray.size();i++){

                        Long productId = productidJSONArray.getLong(i);
                        String productInfoJSON = "{\"id\":"+productId+",\"name\": \"iphone"+productId+"手机\",\"price\":5599,\"shopId\":1,\"modifiedTime\":\"2019-01-01 12:00:00\"}";
                        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
                        cacheService.saveProductInfo2RedisCache(productInfo);
                        System.out.println("[CachePreWarmThread将商品数据设置到本地缓存中]productInfo="+productInfo);
                        cacheService.saveProductInfo2LocalCache(productInfo);
                        System.out.println("[CachePreWarmThread将商品数据设置到redis缓存中]productInfo="+productInfo);

                    }
                    zkSession.createNode("/taskid-status-" + taskid);
                    zkSession.setNodeData("/taskid-status-" + taskid,"success");
                }

                zkSession.releaseDistributedLock(taskidStatusLockPath);

                zkSession.releaseFastFailedDistributedLock(taskidLockPath);
            }

        }
    }
}
