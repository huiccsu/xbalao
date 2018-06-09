package com.xbalao.service.exception;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.cf88.model.annotation.ControllerType;
import com.cf88.service.exception.CFException;
import com.cf88.util.http.ResultPrinter;
import com.cf88.util.reflect.RegExpValidatorUtils;
import com.xbalao.service.base.LoggerAdvice;
/***
 * 
 * Copyright (c) 2017
 * @ClassName:     DefaultGlobalExceptionResolver.java
 * @Description:    Exception同意处理入口,
 * 对于以json格式请求的App，遇上错误直接返回code=3000的错误信息，防止APP因为后台跑错而崩溃
 
 * 
 * @author:        hui
 * @version:       V1.0  
 * @Date:           2017年6月28日 上午10:46:19
 */
public class DefaultGlobalExceptionResolver implements HandlerExceptionResolver{
	/**
	 * 日志
	 */
	private static final  Logger LOGGER = LogManager.getLogger(DefaultGlobalExceptionResolver.class);
	/**
	 * 异常处理，如果 是ajax请求，返回错误信息，如果是jsp页面，则跳转error.jsp页面
	 */
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse response, Object handler, Exception e) {
		LOGGER.error(e.getMessage(),e);
		logInfo(req);
		ControllerType pageAuthType =getControllerType( req,  response, handler);
		if(pageAuthType!=null && pageAuthType.isHtml())
		{
			Map<String, Object> attributes = new HashMap<String, Object>();  
	        attributes.put("ex", e);
			ModelAndView mv = new ModelAndView("../../static/html/error",attributes);
			return mv;
		}
		else
		{
			String msg=e.toString();
			//如果是自定义的错误，返回自定义的消息体
			if(e instanceof CFException)
			{
				//如果抛出的是数字，说明是自定义好的错误常量
				if(RegExpValidatorUtils.getInstance().getRegex("intege1", msg))
					ResultPrinter.print(response,ResultPrinter.toString(ResultPrinter.printConstsErrorCode(Integer.parseInt(msg))));
				else ResultPrinter.print(response,ResultPrinter.toString(ResultPrinter.printConstsErrorMsg(e.getMessage())));
			}
			else
			{
				//对于数据库有唯一建的，提示用户信息不同
				if(StringUtils.isNotBlank(msg) && msg.contains("Duplicate") && msg.contains("com.mysql.jdbc.exceptions"))
				{
					ResultPrinter.print(response,ResultPrinter.toString(ResultPrinter.printConstsErrorMsg("请务重复操作")));
				}
				else
				{
					ResultPrinter.print(response,ResultPrinter.toString(ResultPrinter.printConstsErrorCode(3000)));
				}
			}
			return null;
		}
	}
	/**
	 * 获取controll类型
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 */
	protected ControllerType  getControllerType(HttpServletRequest request, HttpServletResponse response,Object handler)
	{
		 if (handler instanceof HandlerMethod) {
			 HandlerMethod handlerMethod = (HandlerMethod) handler;
             Method method = handlerMethod.getMethod();
             ControllerType pageAuthType=  method.getAnnotation(ControllerType.class);
             if(pageAuthType==null)
            	 pageAuthType= handlerMethod.getBeanType().getAnnotation(ControllerType.class);
             return pageAuthType;
		 }
		return  null;
	}
	
	/**
	 * 记录错误接口信息
	 * @param request
	 */
	private void logInfo(HttpServletRequest request)
	{
		String uri = request.getRequestURI();
		LOGGER.info("error url >>{} agent>>{}",uri,request.getHeader("User-Agent"));
		
		Map<String, String[]> params = request.getParameterMap();
		LoggerAdvice.logInfo( uri,params, request );
	}
}
