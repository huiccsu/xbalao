package com.xbalao.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Service;

import com.cf88.service.base.RedisCacheService;
import com.cf88.service.exception.CFException;
import com.xbalao.model.vo.IPMessage;
import com.xbalao.util.HttpClientSpider;

/**
 * IP代理类
 * @author lanjun
 *
 */
@Service
public class IpProxyService {
	@Autowired
	private RedisCacheService redisCacheService;
	private static final  String IP_KEY="ip::proxy::list";
	@Autowired
	DataRunner dataRunner;
	
	private int ipTotal=50;
	
	public void cacheIpProxy()
	{
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			if(!cacheIpProxy(i)) break;
		}
	}
	
	public boolean cacheIpProxy(int page)
	{
		BoundSetOperations<String, Object> boundSetOperations =getBoundSetOperations();
		if(boundSetOperations.size()>=ipTotal) return false;
		HttpClientSpider httpClientSpider = new HttpClientSpider(getRandomIPMessage()); 
		Document document = httpClientSpider.getElements("http://www.xicidaili.com/nn/"+page);
		Elements trElements = document.select("table[id=ip_list]").select("tbody").select("tr");
		trElements.remove(0);
		List<IPMessage> list = new ArrayList<IPMessage>();
		for (Element trs : trElements) {
			 	IPMessage ipMessage = new IPMessage();
				String ipAddress = trs.select("td").get(1).text();
                String ipPort = trs.select("td").get(2).text();
                String ipType = trs.select("td").get(5).text();
                String ipSpeed = trs.select("td").get(6).select("div[class=bar]").attr("title");
                ipMessage.setIpSpeed(Double.valueOf(ipSpeed.replace("秒", "")));
                if(NumberUtils.isNumber(ipPort))
                {
                	ipMessage.setIpPort(Integer.valueOf(ipPort));
                }
                
                ipMessage.setIpAddress(ipAddress);
                ipMessage.setIpType(ipType);
                if(ipMessage.getIpPort()<=0 || ipMessage.getIpSpeed()<=0)
                	continue;
                if(filter(ipMessage)) 
                	list.add(ipMessage);
		}
		if(list.size()>0) boundSetOperations.add(list.toArray());
		return true;
	}
	
	/**
	 * 过滤不符合要求的
	 * @param ipMessage
	 * @return
	 */
	public  boolean filter(IPMessage ipMessage) {
        if ("HTTPS".equalsIgnoreCase(ipMessage.getIpType()) && ipMessage.getIpSpeed() <= 2.0) {
            return true;
        }
        return false;
    }
	
	public BoundSetOperations<String, Object> getBoundSetOperations()
	{
		return  redisCacheService.getRedisTemplate().boundSetOps(IP_KEY);
	}
	
	
	public void checkIsAlive()
	{
		BoundSetOperations<String, Object> boundSetOperations= getBoundSetOperations();
		Set<Object> set = boundSetOperations.members();
		for (int i = 0; i < set.size(); i++) {
			
		}
	}
	/**
	 * 随机获取IP
	 * @return
	 */
	public IPMessage getRandomIPMessage()
	{
		Object ob = getBoundSetOperations().randomMember();
		if(ob==null)
		{
			dataRunner.setGetIpFlag(true);
			return null;
		}
		IPMessage ipMessage =  (IPMessage) ob;
		HttpClientSpider httpClientSpider = new HttpClientSpider();
		boolean  isok =httpClientSpider.testProxy(ipMessage.getIpAddress(), ipMessage.getIpPort());
		if(isok) return ipMessage;
		else
		{
			BoundSetOperations<String, Object> boundSetOperations =getBoundSetOperations();
			boundSetOperations.remove(ob);
			if(boundSetOperations.size()<=ipTotal)
			{
				dataRunner.setGetIpFlag(true);
			}
			return getRandomIPMessage(); 
		}
	}
}
