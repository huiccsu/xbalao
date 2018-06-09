/*package com.xbalao.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.cf88.util.date.DateUtil;
import com.xbalao.model.Articles;
import com.xbalao.model.Imgs;
import com.xbalao.spider.HttpClientSpider;


@Service
public class QkljwSpiderService extends BaseService<Articles>{
	
	String url ="/hot.html";

	@Override
	public String getDomain() {
		return "http://www.qkljw.com";
	}
	
	@Override
	public List<Articles> parseHtmlArticles(String url)
	{
		HttpClientSpider httpClientSpider = new HttpClientSpider(); 
		Document document = httpClientSpider.getElements(getDomain()+url);
		Element  element =document.getElementById("u_news_list");
		List<Articles> list = new ArrayList<Articles>();
		Elements lis=	element.getElementsByTag("li");
		for (Element lis_element : lis) {
			Articles articles =	getArticles(lis_element);
			if(StringUtils.isNoneBlank(articles.getTitle()))
				list.add(articles);
		}
		return list;
	}
	
	public Articles getArticles(Element  element)
	{
		String url =getUrl(element);
		if(StringUtils.isBlank(url)) return null;
		Articles articles = new Articles();
		articles.setCtime_str(getCtime(element));
		articles.setRemark(getRemark(element));
		articles.setTitle(getTitle(element));
		articles.setUrl(url);
		articles.setViewers(getViewers(element));
		articles.setSrcId(getSrcId(url));
		articles.setSrcDomain(getDomain());
		articles.setCtime(getLongCtime(articles.getCtime_str()));
		
		//设置内存在最后一步,先保证库中不存在记录，不然白做工
		setContent(articles);
		articles.setShortImg(getQiniuImg(articles,getShortImg(element)));
		return articles;
	}
	
	private long getLongCtime(String ctime_str) {
		if(StringUtils.isBlank(ctime_str)) return System.currentTimeMillis();
		try {
			return DateUtil.string2Date(ctime_str, DateUtil.TIMESTAMP).getTime();
		} catch (Exception e) {
			return System.currentTimeMillis();
		}
	}

	*//**
	 * 从url中获取ID
	 * @param url2
	 * @return
	 *//*
	private String getSrcId(String url2) {
		Pattern pattern = Pattern.compile(getDomain()+"\\/article/(\\S+)\\.html$");
		Matcher  matcher = pattern.matcher(url2);
		if(matcher .matches())
		{
			return matcher.group(1);
		}
		return "";
	}

	public void setContent(Articles articles)
	{
		if(StringUtils.isBlank(articles.getUrl()))return ;
		HttpClientSpider httpClientSpider = new HttpClientSpider(); 
		Document document = httpClientSpider.getElements(articles.getUrl());
		Elements  elements =document.getElementsByClass("content");
		String result=null;
		if(elements.size()>=0)
		{
			Element element=elements.get(0);
			Elements imgs = element.getElementsByTag("img");
			for (Element imgs_element : imgs) {
				String img = imgs_element.attr("src");
				if(!isImg(img)) continue;
				imgs_element.attr("src", getQiniuImg(articles,img)); 
			}
			result=element.html();
		}
		articles.setContent(result);
	}
	
	
	

	*//**
	 * 获取图片
	 * @param element
	 * @return
	 *//*
	public String getShortImg(Element element)
	{
		Elements elements =element.getElementsByTag("img");
		for (Element imgElement : elements) {
			if("path".equals(imgElement.className()))
			{
				String src= imgElement.attr("src").trim();
				return getFullPath(src) ;
			}
		}
		return null;
	}
	*//**
	 * 标题
	 * @param element
	 * @return
	 *//*
	public String getTitle(Element element)
	{
		Elements elements =element.getElementsByTag("h5");
		for (Element h5Element : elements) {
			if("title".equals(h5Element.className()))
			{
				return h5Element.text().trim();
			}
		}
		return null;
	}
	*//**
	 * 描述
	 * @param element
	 * @return
	 *//*
	public String getRemark(Element element)
	{
		Elements elements =element.getElementsByTag("p");
		for (Element h5Element : elements) {
			if("description".equals(h5Element.className()))
			{
				return h5Element.text().trim();
			}
		}
		return null;
	}
	*//**
	 * 时间
	 * @param element
	 * @return
	 *//*
	public String getCtime(Element element)
	{
		Elements elements =element.getElementsByTag("span");
		for (Element h5Element : elements) {
			if("update_time".equals(h5Element.className()))
			{
				String ctime= h5Element.text().trim();
				return ctime+":00";
			}
		}
		return null;
	}
	
	*//**
	 * 时间
	 * @param element
	 * @return
	 *//*
	public int getViewers(Element element)
	{
		Elements elements =element.getElementsByTag("span");
		for (Element h5Element : elements) {
			if("view".equals(h5Element.className()))
			{
				return getIntVal(h5Element.text().trim());
			}
		}
		return 0;
	}
	
	*//**
	 * 时间
	 * @param element
	 * @return
	 *//*
	public String getUrl(Element element)
	{
		Elements elements =element.getElementsByTag("a");
		for (Element h5Element : elements) {
			String href=h5Element.attr("href").trim();
			if(matchArticleUrl(href))
				return getFullPath(href) ;
		}
		return null;
	}
	

}
*/