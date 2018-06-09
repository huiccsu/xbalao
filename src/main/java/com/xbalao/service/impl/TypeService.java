package com.xbalao.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf88.service.base.MysqlDao;
import com.cf88.util.DBConst;
import com.xbalao.model.Types;
/**
 * 新闻分类标签
 * @author lanjun
 *
 */
@Service
public class TypeService extends MysqlDao<Types>{

	@Autowired
	ArticlesService articlesService;

	public Map<String, Object> getTypes() {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = new JSONObject();
		json.put(DBConst.key_orderBy, "sorted");
		map.put("types", this.getByParm(json));
		return map;
	}

}
