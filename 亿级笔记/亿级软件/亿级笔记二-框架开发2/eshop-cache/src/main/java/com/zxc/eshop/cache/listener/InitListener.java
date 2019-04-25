package com.zxc.eshop.cache.listener;

import com.zxc.eshop.cache.kafka.KafkaConcusmer;
import com.zxc.eshop.cache.prewarm.CachePreWarmThread;
import com.zxc.eshop.cache.rebuild.RebuildCacheThread;
import com.zxc.eshop.cache.spring.SpringContext;
import com.zxc.eshop.cache.zk.ZooKeeperSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {



    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //获得Spring 容器
        ServletContext sc = sce.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);

        new Thread(new KafkaConcusmer("cache-message")).start();
        new Thread(new RebuildCacheThread()).start();

        ZooKeeperSession.init();


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
