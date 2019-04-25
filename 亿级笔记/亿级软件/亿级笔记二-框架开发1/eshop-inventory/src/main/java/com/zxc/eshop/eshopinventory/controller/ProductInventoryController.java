package com.zxc.eshop.eshopinventory.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.zxc.eshop.eshopinventory.model.ProductInventory;
import com.zxc.eshop.eshopinventory.requestss.ProductInventoryCacheRefreshRequest;
import com.zxc.eshop.eshopinventory.requestss.ProductInventoryDBUpdateRequest;
import com.zxc.eshop.eshopinventory.requestss.Request;
import com.zxc.eshop.eshopinventory.service.ProductInventoryService;
import com.zxc.eshop.eshopinventory.service.RequestAsyncProcessService;
import com.zxc.eshop.eshopinventory.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductInventoryController {

    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    @Autowired
    private ProductInventoryService productInventoryService;


    /**
     * 更新商品库存
     * @return
     */
    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public Response updateProductInventory(ProductInventory productInventory) {

        Response response =null;
        try {
            System.out.println("=========日志===========接收到更新商品库存的请求，商品id="+productInventory.getProductId()+"，商品库存="+productInventory.getInventoryCnt());
            Request request=new ProductInventoryDBUpdateRequest(productInventory,productInventoryService);
            requestAsyncProcessService.process(request);
            response =new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response=new Response(Response.FAILURE);
        }
        return response;
    }

    /**
     * 读取商品库存，更新缓存
     * @return
     */
    @RequestMapping("/getProductInventory")
    @ResponseBody
    public ProductInventory getProductInventory(Integer productId) {
        System.out.println("==========日志==========接收到一个读请求，商品id="+productId);
        ProductInventory productInventory;
        try {
            Request request=new ProductInventoryCacheRefreshRequest(productId,productInventoryService,false);
            requestAsyncProcessService.process(request);


            //将请求扔给service 异步去处理以后，就需要while(true)一会 ，在这里hang住
            //去尝试等待前面有库存更新的操作，同时缓存刷新操作，最新的数据刷新到缓存中
            long startTime=System.currentTimeMillis();
            long endTime = 0L;
            long waitTime=0L;
            while (true){

                if (waitTime>200){
                    break;
                }
                //尝试去redis 去读取一次商品库存的缓存数据
                 productInventory = productInventoryService.getProductInventoryCache(productId);

                //如果读取到结果，就返回
                if(productInventory!=null){
                    System.out.println("==========日志==============：在200ms内读取到了redis中的数据商品id="+productInventory.getProductId()+"，库存为"+productInventory.getInventoryCnt());
                    return productInventory;
                }else{
                    Thread.sleep(20);
                    endTime=System.currentTimeMillis();
                    waitTime=endTime-startTime;
                }

            }
            //没能从缓存中读取数据就从数据库中读取数据
             productInventory = productInventoryService.findProductInventory(productId);
            if (productInventory!=null){
                 request=new ProductInventoryCacheRefreshRequest(productId,productInventoryService,true);
                requestAsyncProcessService.process(request);
                return productInventory;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductInventory(productId,-1L);
    }

}
