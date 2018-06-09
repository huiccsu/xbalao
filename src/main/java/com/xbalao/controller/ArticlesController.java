package com.xbalao.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cf88.util.GlobalParamUtil;
import com.cf88.util.consts.Const;
import com.cf88.util.http.ResultPrinter;
import com.xbalao.model.vo.ConfigData;
import com.xbalao.service.impl.ArticlesService;

@Controller
@RequestMapping("/article")
public class ArticlesController {
	@Autowired
	ArticlesService articlesService;
	
	/**
	 * 主页 左边列表数据
	 * @param request
	 */
	@RequestMapping(value="/indexleft")
	@ResponseBody
	public void createLeftHtml(HttpServletRequest request)
	{
		articlesService.createIndexLeftHtml(request);
	}
	
	/**
	 * 主页 左边列表数据
	 * @param request
	 */
	@RequestMapping(value="/indexright")
	@ResponseBody
	public void createRightHtml(HttpServletRequest request)
	{
		articlesService.createIndexRightHtml(request);
	}
	
	/**
	 * 主页 左边列表数据
	 * @param request
	 */
	@RequestMapping(value="/index")
	@ResponseBody
	public void createHtml(HttpServletRequest request)
	{
		articlesService.creatIndexHtml(request);
	}
	
	/**
	 * 主页 左边滚动图片
	 * @param request
	 */
	@RequestMapping(value="/scoller")
	@ResponseBody
	public void createScollerHtml(HttpServletRequest request)
	{
		articlesService.createScollerHtml(request);
	}
	
	/**
	 * 主页 左边滚动图片
	 * @param request
	 */
	@RequestMapping(value="/type")
	@ResponseBody
	public void createTypeHtml(HttpServletRequest request)
	{
		articlesService.createTypesHtml(request);
	}
	
	/**
	 * 主页更多
	 * @param request
	 */
	@RequestMapping(value="/more")
	@ResponseBody
	public Object more(Long ctime,String key,Integer page,Integer start)
	{
		if(StringUtils.isNoneBlank(key))
		{
			return ResultPrinter.printSuc(articlesService.getMoreByKeyWords(key,page));
		}
		return ResultPrinter.printSuc(articlesService.getMore(ctime,start));
	}
	
	/**
	 * 主页更多
	 * @param request
	 */
	@RequestMapping(value="/{id}")
	public Object more(@PathVariable("id") int id)
	{
		ModelAndView mav = new ModelAndView("/articles/detail");
        mav.addObject(Const.CREATE_HTML, false);
        mav.addObject("article", articlesService.addViewers(id));
		return mav;
	}
	/**
	 * 新闻列表
	 * @param request
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/list")
	public Object list(HttpServletRequest request,String key)
	{
		ModelAndView mav = getModel(request);
		if(StringUtils.isBlank(key))
			mav.addAllObjects(articlesService.getByNewList());
		else
			mav.addAllObjects(articlesService.getByKeyWorlds(key));
		return mav;
	}
	/**
	 * 热点新闻
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/hot")
	public Object list(HttpServletRequest request)
	{
		ModelAndView mav = getModel(request);
        mav.addAllObjects(articlesService.getOrderByHot());
		return mav;
	}
	/*
	 * 手动建索引
	 */
	@RequestMapping(value="/elindex_flush")
	@ResponseBody
	public Object elindex_flush(HttpServletRequest request,String pwd)
	{
		if(!ConfigData.getIndexFlushPwd().equals(pwd))
			return "没有操作权限";
		articlesService.indexAllArticles();
		return ResultPrinter.printSuc("操作成功");
	}

	
	private ModelAndView getModel(HttpServletRequest request) {
		GlobalParamUtil.setBaseParam(request);
		ModelAndView mav = new ModelAndView("/articles/list");
		mav.addObject(Const.CREATE_HTML, false);
		return mav;
	}
}
