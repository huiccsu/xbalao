package com.xbalao.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cf88.service.base.MysqlDao;
import com.cf88.service.mis.postgre.GtsDataChange;
import com.cf88.util.DBConst;
import com.cf88.util.data.upload.QiNiuImgUpload;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.xbalao.mapper.ArticlesMapper;
import com.xbalao.model.Articles;
import com.xbalao.model.Imgs;
import com.xbalao.model.vo.QiNiuImg;
import com.xbalao.util.HttpClientSpider;
import com.xbalao.util.TransportClientUtils;

/**
 * 抓取的图片处理类
 * @author lanjun
 *
 */
@Service
public class ImgsService extends MysqlDao<Imgs>{
	@Autowired
	ArticlesMapper articlesMapper;
	
	@Transactional
	public void save(List<Object> result,Map<String,Object> map,boolean isTest)
	{
		if(!isTest)
		{
			articlesMapper.batchSave(map);
			saveAllImgs(result);
			saveIndex(result);
		}
	}
	
	/**
	 * 创建索引
	 * @param result
	 */
	private void saveIndex(List<Object> result) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			TransportClientUtils transportClientUtils = new TransportClientUtils();
			transportClientUtils.build();
			for (Object object : result) {
				map = new HashMap<String,Object>();
				Articles  articles= (Articles) object;
				map.put("title", articles.getTitle());
				map.put("ctime", articles.getCtime());
				map.put("remark", articles.getRemark());
				transportClientUtils.save(map);
			}
		} catch (Exception e) {
			log.info("error->{}",e.getMessage());
		}
		
	}

	/**
	 * 批量保存图片
	 * @param list
	 * @return
	 */
	public void saveAllImgs(List<Object>  list)
	{
		if(list==null || list.size()<0) return ;
		List<Imgs> result = new ArrayList<Imgs>();
		for (Object object : list) {
			Articles articles=(Articles) object;
			result.addAll(articles.getImgs());
		}
		if(result.size()>0)
		{
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("list", result);
			articlesMapper.batchSaveImgs(map);
		}
	}
	
	public void  upload() 
	{
		for (int i=0;i<Integer.MAX_VALUE;i++) {
			int total = uploadImg() ;
			if(total<=0) break;
		}
	}
	
	/**
	 * 上传图片操作
	 */
	public int  uploadImg() {
		List<Map<?, ?>> list = getImgs();
		if(list==null ||list.size()<=0) return 0;
		QiNiuImgUpload upload = new QiNiuImgUpload(QiNiuImg.getQiniu_bucket(),QiNiuImg.getQiniu_accessKey(),QiNiuImg.getQiniu_secretKey(),QiNiuImg.getQiniu_visit_url());
		for (Map<?, ?> map : list) {
			String fileName = GtsDataChange.obToString(map.get("deSrc"));
			String src = GtsDataChange.obToString(map.get("src"));
			if(StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(src))
			{
				int code= update(upload , src, fileName);
			    if(code>0)
			    {
			    	this.updateByPram(getUpdateJson(code), getWhereJson(GtsDataChange.obToInt(map.get("id"), 0)));
			    }
			}
		}
		return list.size();
	}

	/**
	 * 拉取没有上传过的列表
	 * @return
	 */
	private List<Map<?, ?>> getImgs() {
		JSONObject json = new JSONObject();
		json.put("status", 0);
		json.put(DBConst.key_orderBy, "ctime desc");
		return  this.getByParm(json);
	}
	
	/**
	 * 上传图片
	 * @param upload
	 * @param src
	 * @param fileName
	 * @return
	 */
	private int update(QiNiuImgUpload upload ,String src,String fileName)
	{
		HttpClientSpider httpClientSpider = new HttpClientSpider();
		byte[] bye = httpClientSpider.getByte(src);
		if(bye==null) return -1;
		if(fileName.startsWith("/"))fileName=fileName.replaceFirst("/", "");
		Response response  =upload.upload(bye, fileName,Zone.huadong());
	    if(response.statusCode!=200)
	    {
	    	log.error("src->{} code->{} fileName->{}",src,response.statusCode,fileName);
	    }
	    return response.statusCode;
	}
	/**
	 * 要更新的字段
	 * @return
	 */
	public JSONObject getUpdateJson(int code)
	{
		JSONObject json = new JSONObject();
		json.put("status", code);
		json.put("dtime", System.currentTimeMillis());
		return json;
	}
	
	/**
	 * 主键where条件
	 * @param id
	 * @return
	 */
	public JSONObject getWhereJson(int id)
	{
		JSONObject json = new JSONObject();
		json.put("id", id);
		return json;
	}
}
