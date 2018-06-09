package com.xbalao.interceptor.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xbalao.service.impl.InitService;


/**
 * 
 * Copyright (c) 2017
 * @ClassName:     InitListener.java
 * @Description:   启动时初始数据入口
 * 
 * @author:        hui
 * @version:       V1.0  
 * @Date:           2017年6月28日 上午10:46:55
 */
public class InitListener implements ServletContextListener {
    public Logger log = LogManager.getLogger(getClass());
   
    private ApplicationContext context;
    

    public void contextDestroyed(ServletContextEvent arg0) {
    	System.out.println("=======stop==========");
    }

    public void contextInitialized(ServletContextEvent sce) {
    	context = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        InitService initService = (InitService) context.getBean("initService");
        initService.init(context);
        
   }

}
