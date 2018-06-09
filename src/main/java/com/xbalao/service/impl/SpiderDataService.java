package com.xbalao.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf88.util.date.DateUtil;
import com.xbalao.model.ArticleUrls;
import com.xbalao.model.Articles;
import com.xbalao.util.HttpClientSpider;
import com.xbalao.util.RuleUtils;

@Service
public class SpiderDataService extends BaseService<Articles>{
	@Autowired
	ImgsService imgsService;
	
	public List<Articles> parseHtmlArticles(ArticleUrls articleUrls)
	{
		List<Articles> list = new ArrayList<Articles>();
		HttpClientSpider httpClientSpider=getHttpClientSpider(false);
		if(httpClientSpider==null) return null;
		Document document = httpClientSpider.getElements(articleUrls.getUrl());
		setRuleUtils(RuleUtils.getInstance(articleUrls.getRule()));
		Elements elements= getRuleUtils().getElements(document, "root");
		for (Element lis_element : elements) {
			Articles articles =	getArticles(lis_element,articleUrls);
			if(StringUtils.isNoneBlank(articles.getTitle()))
				list.add(articles);
		}
		return list;
	}
	
	public Articles getArticles(Element  element,ArticleUrls articleUrls)
	{
		String url =getUrl(element,articleUrls);
		if(StringUtils.isBlank(url)) return null;
		Articles articles = new Articles();
		setCtime(element,articles,articleUrls);
		articles.setRemark(getRemark(element));
		articles.setTitle(getTitle(element));
		articles.setUrl(url);
		articles.setViewers(getViewers(element));
		articles.set_viewers(articles.getViewers());
		articles.setSrcId(getSrcId(url,articleUrls));
		articles.setSrcDomain(articleUrls.getDomain());
		articles.setFromer(articleUrls.getNetName());
		articles.setStatus(1);
		//设置内存在最后一步,先保证库中不存在记录，不然白做工
		//setContent(articles, articleUrls);
		articles.setShortImg(getQiniuImg(articles,articleUrls,getShortImg(element,articleUrls)));
		return articles;
	}
	
	@Override
	public void setContent(Articles articles,ArticleUrls articleUrls)
	{
		if(StringUtils.isBlank(articles.getUrl()))return ;
		HttpClientSpider httpClientSpider=getHttpClientSpider(false);
		if(httpClientSpider==null) return;
		Document document = httpClientSpider.getElements(articles.getUrl());
		Elements  element = getRuleUtils().getElements(document, "root.content");
		if(element!=null && element.size()>0)
		{
			Element	element_0=element.get(0);
			String result=null;
			
			//看是否有要过滤的标签
			String tags = getRuleUtils().getJsonValue("root.content.filter");
			if(StringUtils.isBlank(tags))
			{
				Elements imgs = element_0.getElementsByTag("img");
				setImgElements(articles, articleUrls, imgs);
				result=element_0.html();
			}else
			{
				List<String> tagsArray = Arrays.asList(tags.toLowerCase().split("[//|]"));
				Elements  elements =element_0.getAllElements();
				if(tagsArray.contains("img"))
				{
					setImgElements(articles, articleUrls, elements);
				}
				Element newDiv =new Element("div");
				for (Element _temp : elements) {
					String tagName=_temp.tagName().toLowerCase();
					if(tagsArray.contains(tagName))
					{
						_temp.appendTo(newDiv);
					}
				}
				result=newDiv.html();
			}
			articles.setContent(result);
		}
	}

	private void setImgElements(Articles articles, ArticleUrls articleUrls,
			Elements _temp) {
		for (Element imgs_element : _temp) {
			String img = imgs_element.attr("src");
			if(!isImg(img)) continue;
			imgs_element.attr("src", getQiniuImg(articles,articleUrls,img)); 
		}
	}
		
	private String getShortImg(Element element,ArticleUrls articleUrls) {
		String src=  getRuleUtils().getElement(element, "shortImg").trim();
		return getFullPath(src,articleUrls) ;
	}


	private String getSrcId(String url,ArticleUrls articleUrls) {
		int index =url.lastIndexOf("/")+1;
		if(index>=1) 
		{
			url=url.substring(index, url.length());
			url=url.split("[//.]")[0];
			return url.replace(".html", "");
		}
		return "";
	}

	private int getViewers(Element element) {
		/*String viewers=  getRuleUtils().getElement(element, "viewers").trim();
		Pattern pattern = Pattern.compile(".*(\\d+).*");
		Matcher  matcher = pattern.matcher(viewers);
		if(matcher .matches())
		{
			String val=  matcher.group(1);
			if(NumberUtils.isNumber(val))return Integer.parseInt(val);
			return 0;
		}*/
		Random random = new Random();
		return random.nextInt(1000);
	}

	private String getTitle(Element element) {
		return  getRuleUtils().getElement(element, "title").trim();
	}

	private String getRemark(Element element) {
		return  getRuleUtils().getElement(element, "remark").trim();
	}

	/**
	 * 设置时间
	 * @param element
	 * @param articles
	 * @param articleUrls
	 */
	private void setCtime(Element element,Articles articles,ArticleUrls articleUrls) {
		String formater="yyyy-MM-dd HH:mm";
		String ctime=  getRuleUtils().getElement(element, "ctime_str").trim();
		//如果规则为空，说明要存抓取时间
		if(StringUtils.isBlank(articleUrls.getTimeFormate()))
		{
			 articles.setCtime(System.currentTimeMillis());
			 articles.setCtime_str(DateUtil.date2String(new Date(), formater));
		}
		else
		{
			//按要求格式化时间，如果格式化失败，则存当前时间
			try {
				Date date = DateUtil.string2Date(ctime, formater);
				articles.setCtime(date.getTime());
				articles.setCtime_str(ctime);
			} catch (Exception e) {
				 articles.setCtime(System.currentTimeMillis());
				 articles.setCtime_str(DateUtil.date2String(new Date(), formater));
			}
		}
		
	}

	/**
	 * 新闻详细地址
	 * @param element
	 * @return
	 */
	public String getUrl(Element element,ArticleUrls articleUrls)
	{
		String href=   getRuleUtils().getElement(element, "url").trim();
		if(matchArticleUrl(href))
			return getFullPath(href,articleUrls) ;
		return null;
	}

	/**
	 * 保存数据
	 */
	@Override
	public List<Object> save(List<Object> result, Map<String, Object> map,boolean isTest) {
		imgsService.save(result, map,isTest);
		return result;
	}
	

}
