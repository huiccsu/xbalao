package com.xbalao.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cf88.service.base.MysqlDao;
import com.cf88.util.DBConst;
import com.xbalao.model.Ads;

@Service
public class AdsService extends MysqlDao<Ads>{
	
	/**
	 * 获取首页的广告位
	 * @return
	 */
	public List getIndexAds(int type)
	{
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put(DBConst.key_orderBy, "istop");
		return this.getByParm(json, null,4);
	}
}
