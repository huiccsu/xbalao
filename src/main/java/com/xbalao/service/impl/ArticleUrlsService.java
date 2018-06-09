package com.xbalao.service.impl;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cf88.service.base.MysqlDao;
import com.xbalao.model.ArticleUrls;

@Service
public class ArticleUrlsService extends MysqlDao<ArticleUrls>{
	@Autowired
	SpiderDataService spiderDataService;
	/**
	 * 爬虫入口
	 */
	public void spider() {
		try {
			 spider(null,false);
		} catch (Exception e) {
			log.error("e->{}",e.getMessage());
		}
	}
	
	public void spider(Integer id,boolean isTest) {
		List<Map<?,?>> mapList= getUrls(id);
		if(mapList==null || mapList.size()<=0) return;
		for (Map<?,?> map : mapList) {
			ArticleUrls articleUrls = new ArticleUrls();
			articleUrls =this.mapToEntity(map, articleUrls);
			spiderDataService.bathSave(articleUrls,isTest);
		}
	}
	
	public List<Map<?,?>>  getUrls()
	{
		return  getUrls(null);
	}
	
	public List<Map<?,?>>  getUrls(Integer id)
	{
		JSONObject json = new JSONObject();
		json.put("status", 1);
		if(id!=null) json.put("id", id);
		return  this.getByParm(json,Integer.MAX_VALUE);
	}
}
