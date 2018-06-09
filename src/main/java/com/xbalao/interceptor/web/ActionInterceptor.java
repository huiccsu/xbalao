package com.xbalao.interceptor.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cf88.util.http.Printer;

public class ActionInterceptor implements HandlerInterceptor 
{
	public  Logger log = LogManager.getLogger(getClass());
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("WatchExecuteTime");
	final long misSecond=5000;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		startTimeThreadLocal.set(System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long time = System.currentTimeMillis() - startTimeThreadLocal.get();
		if (time > misSecond)
		{
			 log.info("url[{}] execute[{}]", request.getRequestURI(), time,Printer.getIpAddr(request));
			 LoggerAdvice.log(request);
		}
	}

}
