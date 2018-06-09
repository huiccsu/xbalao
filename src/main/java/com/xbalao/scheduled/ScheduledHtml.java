package com.xbalao.scheduled;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xbalao.service.impl.ArticlesService;

/**
 * 
 * Copyright (c) 2017
 * @ClassName:     
 * @Description:    
 * 
 * @author:        dh
 * @version:       V1.0  
 * @Date:           2017年9月7日 
 */
@Component
public class ScheduledHtml {
	public Logger log = LogManager.getLogger(getClass());
	@Autowired
	ArticlesService articlesService;

	@Scheduled(cron="0 0/1 * * * ?")  
	public void  creatIndexHtml()
	{
		articlesService.creatIndexHtml(null);
	}
	
   
	   
}
