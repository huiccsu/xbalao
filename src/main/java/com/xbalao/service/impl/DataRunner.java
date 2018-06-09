package com.xbalao.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.cf88.model.RedisBean;
import com.cf88.model.StaticConfigData;
import com.cf88.service.base.AbstractMQProducer;
import com.cf88.service.base.BaseService;
import com.cf88.service.base.RedisCacheService;
import com.cf88.service.base.SynDataService;
import com.cf88.service.mis.api.PublicApiService;
import com.cf88.service.mis.impl.BiAppLogService;
import com.cf88.service.mis.impl.CustomerSerivce;
import com.cf88.service.mis.impl.TradeLogsService;
import com.cf88.service.mis.impl.UserLoginLogService;
import com.cf88.service.mis.impl.mq.MqWorkerService;
import com.cf88.service.mis.increment.TracterActiveUserService;
import com.cf88.service.mis.postgre.AcountInfoService;
import com.cf88.util.consts.CacheKey;
import com.cf88.util.consts.Const;

/**
 * Copyright (c) 2017
 * 
 * @ClassName: DataRunner.java
 * @Description: 线程同步数据
 * 
 * @author: hui
 * @version: V1.0
 * @Date: 2017年6月29日 下午2:22:55
 */

@Service
public class DataRunner extends BaseService{
	
	@Autowired
	ImgsService imgsService;
	@Autowired
	IpProxyService ipProxyService;
	@Autowired
	ArticleUrlsService articleUrlsService;
	
	static ThreadPoolExecutor  executorService =null;
	private static Map<String,Long> threadStep= new HashMap<String,Long>();
	private static int poolSize=3;
	boolean getIpFlag=true;
	
	
	public boolean getIpFlag() {
		return getIpFlag;
	}

	public void setGetIpFlag(boolean getIpFlag) {
		this.getIpFlag = getIpFlag;
	}

	public void init()
	{
		 if(executorService==null)
			 executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
		 
	}
	
	/**
	 * 线程池的线程
	 * @return
	 */
	public void getExecutor()
	{
		long completed=executorService.getCompletedTaskCount();
		JSONObject json = new JSONObject();
		json.put("act", executorService.getActiveCount());
		json.put("count", executorService.getTaskCount());
		json.put("completed", completed);
		log.info("Executor=>{}", json.toString());
		//如果有发现已经完成的
		if(completed>0)
		{
			//指定时间来启动线程
			long delayTime=15*60000;
			for (int i = 1; i <= poolSize; i++)
			{
				if(completed<=0) break;
				long excutetime=threadStep.get(String.valueOf(i));
				//如果指定时间内发现没有执行了，说明线此线程已经挂了
				if(System.currentTimeMillis()-excutetime>delayTime)
				{
					runner(i);
					completed--;
					log.info("start runner=>{}",i);
				}
			}
		}
	}
	
	/***
	 * 
	 * @Description: 启动线程进行数据同步
	 * @param:
	 * @return: void
	 */
	public void runner() {
		init();
		for (int i = 1; i <= poolSize; i++)
		{
			runner(i);
		}
	}
		
	public void runner(final int type)
	{
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				while (true)
				{
					logStartExcute( type);
					threadStep.put(String.valueOf(type), System.currentTimeMillis());
					if(type==1)
						upload();
					else if(type==2)
					{
						cacheIpProxy();
					}
					else if(type==3)
					{
						spideArticles();
					}
				}
			}

		});
	}

	/**
	 * 线程启动日志
	 * @param type
	 */
	private  void logStartExcute(int type)
	{
		if(type==1)
		{
			log.info("{} upload img starting....",Thread.currentThread().getName());
		}
		else if(type==2)//同步激活状态
		{
			log.info("{} cacheIpProxy starting....",Thread.currentThread().getName());
		}
		else if(type==3)//同步交易数据
		{
			log.info("{}  spideArticles starting....",Thread.currentThread().getName());
		}
	}
	
	/**
	 * 5分钟抓一次文章
	 */
	private void spideArticles() {
		try {
			articleUrlsService.spider();
		} catch (Exception e) {
			log.error(e.getMessage());
			sleep(30000);
		}finally
		{
			sleep(300000);
		}
	}
	/**
	 * 5分钟抓一次IP
	 */
	private void cacheIpProxy() {
		try {
			if(getIpFlag())
				ipProxyService.cacheIpProxy();
			sleep(300000);
		} catch (Exception e) {
			log.error(e.getMessage());
			sleep(30000);
		}
	}
	/**
	 * 6秒上传一次图片
	 */
	private void upload() {
		try {
			imgsService.upload();
		} catch (Exception e) {
			log.error(e.getMessage());
			sleep(30000);
		}finally
		{
			sleep(30000);
		}
	}
	
	public void stop()
	{
		System.exit(0);
	}
	/**
	 * 
	 * @Description: 阻塞线程
	 * @param: @param times
	 * @return: void
	 */
	private void sleep(long times) {
		try {
			Thread.sleep(times);
		} catch (InterruptedException e) {
			log.info("Thread.sleep=>{}" , e.getMessage());
		}
	}
	
}
