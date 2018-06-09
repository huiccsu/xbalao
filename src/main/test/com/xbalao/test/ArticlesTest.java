package com.xbalao.test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.index.IndexResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cf88.model.mysql.mis.vo.Page;
import com.cf88.service.mis.postgre.GtsDataChange;
import com.xbalao.model.Articles;
import com.xbalao.service.impl.ArticlesService;
import com.xbalao.service.impl.ImgsService;
import com.xbalao.service.impl.IpProxyService;
import com.xbalao.util.TransportClientUtils;

public class ArticlesTest extends BaseTest{
	@Autowired
	ArticlesService articlesService;
	@Autowired
	ImgsService imgsService;
	@Autowired
	IpProxyService ipProxyService;
	@Test
	public void testHtml()
	{
		//articlesService.createIndexLeftHtml(null);
	}
	
	@Test
	public void testSider()
	{
		//qkljwSpiderService.save("/index.html");
	}

	@Test
	public void testImgs()
	{
		//imgsService.upload();
	}
	@Test
	public void testIpProxy()
	{
		//ipProxyService.cacheIpProxy();
	}
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	public void testarsave() throws InterruptedException, ExecutionException
	{
		//551
		List<Map<?, ?>> list =  articlesService.getByParm(null, new String[]{"title","remark","id","ctime"},Integer.MAX_VALUE);
		TransportClientUtils transportClientUtils=	new TransportClientUtils("192.168.35.84",9300).build();
		for (Map<?, ?> map2 : list) {
			Map<String,?> map = (Map<String, ?>) map2;
			IndexResponse indexResponse =transportClientUtils.save(map);
			System.out.println(transportClientUtils.get(GtsDataChange.obToString(map.get("id"))));
		}
		
	}
	
	@Test
	public void testMutil()
	{
		TransportClientUtils transportClientUtils=	new TransportClientUtils("192.168.35.84",9200).build();
		Page page =transportClientUtils.getMulti("区块链",1);
		System.out.println(page);
	}
	
	@Test
	public void testDel() throws InterruptedException, ExecutionException
	{
		TransportClientUtils transportClientUtils=	new TransportClientUtils("192.168.35.84",9200).build();
		transportClientUtils.delete("557");
	}
	
}
