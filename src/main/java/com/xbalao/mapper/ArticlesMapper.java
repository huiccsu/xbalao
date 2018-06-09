package com.xbalao.mapper;

import java.util.Map;

import com.cf88.mapper.SqlMapper;
import com.xbalao.model.Articles;


public interface ArticlesMapper extends SqlMapper<Articles> {

	void batchSave(Map<String,Object>  map);

	void batchSaveImgs(Map<String,Object>  map);
}
