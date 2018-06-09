package com.xbalao.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cf88.model.mysql.mis.vo.Page;
import com.cf88.service.base.MysqlDao;
import com.cf88.service.mis.postgre.GtsDataChange;
import com.cf88.util.DBConst;
import com.cf88.util.GlobalParamUtil;
import com.cf88.util.SqlConverter;
import com.cf88.util.http.ResultPrinter;
import com.xbalao.freemark.CreateHtmlFreeMark;
import com.xbalao.model.Articles;
import com.xbalao.model.vo.ConfigData;
import com.xbalao.util.TransportClientUtils;

@Service
public class ArticlesService extends MysqlDao<Articles> {
	@Autowired
	FreeMarkerConfigurer freemarkerConfig;
	@Autowired
	AdsService adsService;
	@Autowired
	TypeService typeService;
	
	/**
	 * 获取更多数据
	 * @param ctime
	 * @return
	 */
	public List<?> getMore(Long ctime,Integer total) {
		if(total==null) total=0;
		JSONObject json = getWhereJson();
		//if(type!=null && type>1) json.put("type", type);
		//如果没有传时间，则按当前时间来算
		if(ctime==null  || ctime==0) ctime=System.currentTimeMillis();
		if(ctime!=null) json.put(DBConst.key_complexity_condition, "ctime<"+ctime);
		json.put(DBConst.key_orderBy, "ctime desc ");
		return this.getByParm(json,getFileds(),total, DBConst.condition_perPageTotal);
	}
	
	/**
	 * 首面左半部分的数据
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, ?> getLeftList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json =getWhereJson();
		json.put(DBConst.key_orderBy, "ctime desc ");
		List<Map<?,?>> list = this.getByParm(json,getFileds(),0, 30);
		map.put("leftList", list);
		map.put("minctime", getMin(list));
		return map;
	}
	
	public void createScollerHtml(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("scollerimgs", adsService.getIndexAds(2));
		createHtml(map, "/index/scoller.ftl","/static/cache/scoller.html");
	}
	
	/**
	 * 获取最小时间
	 * @param list
	 * @return
	 */
	private long getMin(List<Map<?,?>> list)
	{
		/*OptionalLong optionalLong = list.stream().mapToLong(new ToLongFunction<Map<?,?>>() {

			@Override
			public long applyAsLong(Map<?,?> map) {
				return GtsDataChange.obToLong(map.get("ctime"));
			}
		}).min();
		return optionalLong.getAsLong();*/
		if(list==null ||list.size()<=0) return 0;
		else return GtsDataChange.obToLong(list.get(list.size()-1).get("ctime"));
	}
	
	public String[] getFileds()
	{
		return new String[]{"title","id","viewers","shortImg","ctime","ctime_str","remark"};
	}
	
	public Map<String, ?> getRightList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = getWhereJson();
		//最新热点
		json.put(DBConst.key_orderBy, "istop desc,viewers desc ");
		map.put("toplist", this.getByParm(json,getFileds(),0, 18));
		//推荐阅读
		json.clear();
		json.put(DBConst.key_orderBy, "viewers desc,ctime desc ");
		//第一个广告位前面
		map.put("viewerslist1", this.getByParm(json,getFileds(), 0, 10));
		//第二个广告位前面
		map.put("viewerslist2", this.getByParm(json,getFileds(), 10,10));
		//第三个广告位前面
		map.put("viewerslist3", this.getByParm(json,getFileds(), 20,10));
		
		map.put("ads", adsService.getIndexAds(1));
		return map;
	}

	public JSONObject getWhereJson()
	{
		JSONObject json = new JSONObject();
		json.put("status", 1);
		return json;
	}
	/*
	 * 生成首页html
	 */
	public void creatIndexHtml(HttpServletRequest request)
	{
		createIndexRightHtml(request);
		createIndexLeftHtml(request);
		createScollerHtml(request);
		createTypesHtml(request);
	}
	/**
	 * 新闻分类标签
	 * @param request
	 */
	public void createTypesHtml(HttpServletRequest request) {
		createHtml(typeService.getTypes(), "/index/type.ftl","/static/cache/type.html");
	}
	/**
	 * 首页左边主体内容
	 * @param request
	 */
	public void createIndexLeftHtml(HttpServletRequest request) {
		createHtml(getLeftList(request), "/index/body_left.ftl",
				"/static/cache/body_left.html");
	}
	/**
	 * 首页右边主体内容
	 * @param request
	 */
	public void createIndexRightHtml(HttpServletRequest request) {
		createHtml(getRightList(request), "/index/body_right.ftl",
				"/static/cache/body_right.html");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createHtml(Map map, String ftl, String html) {
		ModelAndView mav = getModel(ftl,html);
		mav.addAllObjects(map);
		try {
			CreateHtmlFreeMark.createHTMLforNeed(freemarkerConfig, mav);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private ModelAndView getModel(String ftl, String html) {
		ModelAndView mav = new ModelAndView(ftl);
		mav.addObject("htmlPath", ResultPrinter.getRealPath("webapp.root")
				+ html);
		return mav;
	}

	/**
	 * 给用户浏览数据加一，并返回新闻
	 * @param id
	 * @return
	 */
	public Object addViewers(int id) {
		Map<?,?> map =  this.getById(id);
		if(map!=null)
		{
			JSONObject update = new JSONObject();
			JSONObject where = new JSONObject();
			update.put(DBConst.key_complexity_condition, "viewers=viewers+1");
			where.put("id",id);
			this.updateByPram(update, where);
		}
		return map;
	}

	/**
	 * 获取最新新闻
	 * @param keywords
	 * @return
	 */
	public Map<String,Object> getByNewList() {
			JSONObject json = getWhereJson();
			json.put(DBConst.key_orderBy, "ctime desc");
			return getPageList(json,GlobalParamUtil.getStart(),true);
	}

	/**
	 * 按关键词从el中搜索新闻
	 * @param keywords 关键词
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getByKeyWorlds(String keywords)
	{
			int now = GlobalParamUtil.getStart()/ConfigData.getPerPageTototal() +1;
			Page pager =getIdsByKeyWords(keywords,now);
			if(pager==null) return null;
			Map map= getPageList(new SqlConverter().inList("id", pager.getData(), Integer.class).orderByDesc("ctime").json(),0,false);
			pager.setStart(GlobalParamUtil.getStart());
			map.put("page", getPageHtml(pager,"key="+keywords));
			return map;
	}
	

	public List getMoreByKeyWords(String keywords,Integer page) {
		if(page==null || page<=1) page=1;
		Page pager = getIdsByKeyWords(keywords, page);
		if(pager==null) return null;
		return this.getByParm(new SqlConverter().inList("id",  pager.getData(), Integer.class).orderByDesc("ctime").json());
	}

	/**
	 * 从el中获取记录
	 * @param keywords
	 * @param page
	 * @return
	 */
	private Page getIdsByKeyWords(String keywords, int page) {
		TransportClientUtils transportClientUtils = new TransportClientUtils();
		transportClientUtils.build();
		transportClientUtils.setPageSize(ConfigData.getPerPageTototal());
		return transportClientUtils.getMulti(keywords, page);
	}
	
	/**
	 * 热点新闻
	 * @return
	 */
	public Map<String, Object> getOrderByHot() {
		JSONObject json = getWhereJson();
		json.put(DBConst.key_orderBy, "istop desc,ctime desc");
		return getPageList(json,GlobalParamUtil.getStart(),true);
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Object> getPageList(JSONObject json,int start,boolean getPage) {
		Page page= this.getByPage(json, getFileds(),start,ConfigData.getPerPageTototal());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("articles", page.getData());
		if(getPage)
			map.put("page", getPageHtml(page,""));
		return map;
	}
	
	private String getNextPageHref(int start,String param)
	{
		if(StringUtils.isBlank(param))param="r="+Math.random();
		return "/article/list?start="+start+"&"+param;
	}
	
	private String getPageHtml(Page page,String param)
	{
		if(page.getRecordsTotal()<=0) return "";
		int totalPage=page.getRecordsTotal()/ConfigData.getPerPageTototal()+(page.getRecordsTotal()%ConfigData.getPerPageTototal()>0?1:0);
		//最后一页的游标
		int endStart=(totalPage-1)*ConfigData.getPerPageTototal();
		//当前页码
		int now = page.getStart()/ConfigData.getPerPageTototal() +1;
		//起始位置与结束位置。一次只显示8个分布标签
		int start=now-4<=1?1:now-4;
		start=start<=0?1:start;
		int end=start+8;
		end=end>totalPage?totalPage:end;
		
		String pageHtml="";
		pageHtml+="<div class=\"pg-t1\">共"+page.getRecordsTotal()+"条记录 "+now+"/"+totalPage+" 页 &nbsp;&nbsp; "; 
		pageHtml+="		<a href='"+getNextPageHref(0,param)+"'>首页</a>  "; 
		pageHtml+="	</div>  "; 
		
		pageHtml+="	<div class=\"pg-t2\">     &nbsp; ";
		for (int i = start; i <= end; i++) {
			if(i==0)continue;
			if(start>1 && i==start)
			{
				pageHtml+="		<span >...</span> ";
			}
			if(now==i)//当前页，选中状态
			{
				pageHtml+="		<span class='current'>"+i+"</span> ";
			}
			else
				pageHtml+="		<a href='"+getNextPageHref((i-1)*ConfigData.getPerPageTototal(),param)+"'>"+i+"</a> ";
			if(end<totalPage  && i==end)
			{
				pageHtml+="		<span >...</span> ";
			}
		}
		 
		pageHtml+="		<a href='"+getNextPageHref(endStart,param)+"' >末页</a>  "; 
		pageHtml+="	</div>  ";
		return pageHtml;
	}
	
	/**
	 * 手动创建所有记录的索引
	 */
	@SuppressWarnings("unchecked")
	public void indexAllArticles() {
		int offset=0;
		int perTotal=50;
		JSONObject json = new SqlConverter().eq("status", 1).json();
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			log.info("start create offset->{}",offset);
			offset=i*perTotal;
			List<Map<?, ?>> list =  this.getByParm(json, new String[]{"title","remark","id","ctime"},offset,perTotal);
			if(list==null || list.size()<=0) break;
			TransportClientUtils transportClientUtils=	new TransportClientUtils().build();
			for (Map<?, ?> map2 : list) {
				Map<String,?> map = (Map<String, ?>) map2;
				transportClientUtils.save(map);
			}
		}
		
	}

}
