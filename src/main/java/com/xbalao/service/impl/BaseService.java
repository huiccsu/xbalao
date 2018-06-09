package com.xbalao.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cf88.service.base.MysqlDao;
import com.cf88.service.mis.postgre.GtsDataChange;
import com.cf88.util.SqlConverter;
import com.xbalao.model.ArticleUrls;
import com.xbalao.model.Articles;
import com.xbalao.model.Imgs;
import com.xbalao.model.vo.IPMessage;
import com.xbalao.model.vo.QiNiuImg;
import com.xbalao.util.HttpClientSpider;
import com.xbalao.util.RuleUtils;

public abstract class BaseService<T> extends MysqlDao<T>{
	@Autowired
	IpProxyService ipProxyService;
	
	/**
	 * 
	 * @param canMySelft 是否可以基本自己的IP进行抓取
	 * @return
	 */
	public HttpClientSpider getHttpClientSpider(boolean canMySelft)
	{
		IPMessage iPMessage=ipProxyService.getRandomIPMessage();
		if(!canMySelft && iPMessage==null) return null;
		return  new HttpClientSpider(iPMessage); 
	}
	 
	public RuleUtils ruleUtils=null;
	
	public RuleUtils getRuleUtils() {
		return ruleUtils;
	}
	public void setRuleUtils(RuleUtils ruleUtils) {
		this.ruleUtils = ruleUtils;
	}

	/**
	 * 
	 * @param articleUrls
	 * @param isTest 是否是测试，测试的话不入库
	 */
	public List<Object>  bathSave(ArticleUrls articleUrls,boolean isTest)
	{
		List<Articles> list =parseHtmlArticles(articleUrls);
		if(list==null || list.size()<=0) return null;
		List<Object> result = getNotInDbs(list, getDbSrcIds(list,articleUrls));
		if(result!=null && result.size()>0)
		{
			setContent(result,articleUrls);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("list", result);
			return  save(result,map,isTest);
		}
		return null;
	}
	
	/**
	 * 保存数据，不写实现是为控制事务
	 * @param result
	 * @param map
	 */
	public abstract List<Object>  save(List<Object> result, Map<String, Object> map,boolean isTest) ;
	
	public abstract List<Articles> parseHtmlArticles(ArticleUrls articleUrls);

	
	public List<Articles> setContent(List<Object> result,ArticleUrls articleUrls)
	{
		List<Articles> list = new ArrayList<Articles>();
		for (Object object : result) {
			Articles articles=	(Articles) object;
			setHtmlContent(articles,articleUrls);
			//内容非空才有效
			if(!StringUtils.isBlank(articles.getContent()))
				list.add(articles);
		}
		return list;
	}

	/**
	 * 对内容进行try cactch，失败的话不影响后面的抓取
	 * @param articles
	 * @param articleUrls
	 */
	private void setHtmlContent(Articles articles,ArticleUrls articleUrls)
	{
		try {
			setContent(articles,articleUrls);
		} catch (Exception e) {
			log.error("error->",e.getMessage());
		}
	}
	
	public abstract void setContent(Articles articles,ArticleUrls articleUrls);

	
	
	
	/**
	 * 字符串是否图片路径
	 * @param img
	 * @return
	 */
	public boolean isImg(String img)
	{
		img= prex(img);
		if(StringUtils.isBlank(img)) return false;
		String imgeArray [] = {"bmp","dib","gif","jfif","jpe","jpeg","jpg","png", "tif","tiff","ico"}; 
		for (String string : imgeArray) {
			if(string.equalsIgnoreCase(img))
				return true;
		}
		return false;
	}
	
	private String getPath()
	{
		LocalDate today =LocalDate.now();
		String path ="/upload/"+today.getYear()+""+today.getMonthValue()+""+today.getDayOfMonth()+"/";
		path+=System.currentTimeMillis()+"_"+RandomStringUtils.random(5,"abcdefjijklmnopkrstuvwsyz");
		return path;
	}
	
	public String randomImgPath(String img)
	{
		return getPath()+"."+prex(img);
	}
	private  String prex(String img)
	{
		img=StringUtils.isBlank(img)?"":img;
		String prefix[] =img.split("\\.");
		if(prefix.length<=1) return null;
		return prefix[prefix.length-1];
	}
	
	public String getQiniuImgDomain()
	{
		return QiNiuImg.getQiniu_visit_url();
	}

	public boolean matchArticleUrl(String url)
	{
		if(StringUtils.isBlank(url)) return false;
		return true;
	}
	public String getFullPath(String src,ArticleUrls articleUrls)
	{
		if(src.startsWith("http")) return src;
		else if(src.startsWith("/")) return articleUrls.getDomain()+src;
		return null;
	}
	public String getQiniuImg(Articles articles,ArticleUrls articleUrls,String url)
	{
		return  getQiniuImgDomain()+setImgs(articles,articleUrls,url);
	}
	
	/**
	 * 获取图片
	 * @param articles
	 * @param fullImg
	 */
	public String setImgs(Articles articles,ArticleUrls articleUrls,String srcImg) {
		String fullSrcImg=getFullPath(srcImg,articleUrls);
		Imgs imgs = new Imgs();
		imgs.setCtime(System.currentTimeMillis());
		imgs.setDomain(articles.getSrcDomain());
		imgs.setDeSrc(randomImgPath(srcImg));
		imgs.setSrcId(articles.getSrcId());
		imgs.setSrc(fullSrcImg);
		imgs.setStatus(0);
		articles.getImgs().add(imgs);
		return imgs.getDeSrc();
	}
	
	public int getIntVal(String val)
	{
		if(NumberUtils.isNumber(val)) return Integer.parseInt(val);
		else return 0;
	}

	private List getNotInDbs(List<Articles> list,String  inDbs) {
		if(StringUtils.isNoneBlank(inDbs))
		{
			final List<String> srdIdsList =Arrays.asList(StringUtils.split(inDbs,","));
			List result = list.stream().filter(new Predicate<Articles>() {
				@Override
				public boolean test(Articles t) {
					if(srdIdsList.contains(t.getSrcId()))
						return false;
					return true;
				}
			}).collect(Collectors.toList());
			return result;
		}
		return list;
	}

	private String  getDbSrcIds(List<Articles> list,ArticleUrls articleUrls) {
		List<Object> srcIds  = list.stream().map(new Function<Articles, String>() {
			@Override
			public String apply(Articles t) {
				return StringUtils.isBlank(t.getSrcId())?"0":t.getSrcId();
			}
		}).collect(Collectors.toList());
		JSONObject json =new SqlConverter().inList("srcId", srcIds, String.class).eq("srcDomain", articleUrls.getDomain()).json();
		Map map= this.getOnlyOne(json,new String[]{"GROUP_CONCAT(srcId) as srcId"});
		if(map==null) return null;
		return GtsDataChange.obToString(map.get("srcId"), "");
	}
	
}
