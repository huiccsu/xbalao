package com.xbalao.service.base;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cf88.util.GlobalParamUtil;
import com.cf88.util.consts.Const;
import com.cf88.util.http.Printer;
import com.cf88.util.reflect.RegExpValidatorUtils;

/***
 * 
 * Copyright (c) 2017
 * @ClassName:     LoggerAdvice.java
 * @Description:   参数检验+分页page全局拦截设置+请求参数日志化
 * 
 * @author:        hui
 * @version:       V1.0  
 * @Date:           2017年6月28日 上午10:47:38
 */
public class LoggerAdvice
{
	/**
	 * 日志
	 */
   private static final  Logger LOGGER = LogManager.getLogger(LoggerAdvice.class);

   /**
    *正则处理类
    */
   private  static  RegExpValidatorUtils cmu= RegExpValidatorUtils.getInstance();
    
   private  static String urls="/public/marketprice/cf.json";
    
    public static  Map<String,Object> log(HttpServletRequest request)
    {
        //统计拦截分页数据
        GlobalParamUtil.setBaseParam(request);
        String uri = request.getRequestURI();
        
        //不要拦截的url。防止日志量
        if(urls.indexOf(uri)>=0)
        {
        	 return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, String[]> params = request.getParameterMap();
        
        //记录用户请求接口参数
        if (!params.isEmpty())
        {
            logInfo(uri,params,request);
        }
        
        Object ob = Const.checkLengMap.get(uri);
        if (ob != null)
        {
            return checkParameter(request,ob);
        }
        return null;
    }
   
    public  static  void logInfo(String uri, Map<String, String[]> params,HttpServletRequest request )
    {
    	StringBuilder sb = new StringBuilder();
    	if(params!=null && !params.isEmpty())
    	{
	        Set<Entry<String, String[]>> paramSet = params.entrySet();
	        for (Entry<String, String[]> entry : paramSet)
	        {
	             sb.append(entry.getKey()).append("->").append(Arrays.toString(entry.getValue())).append(" ");
	        }
    	}
    	LOGGER.info("----request:method:{} {} params:{} ip {}", request.getMethod(), uri, sb.toString(), getIpAddr(request));
    }
    /*
     * 拦截对于长度有要求的字段
     */
    public static Map<String,Object> checkParameter(HttpServletRequest request,Object ob)
    {
        //"备注#remark#0#100"
        String[] needCKParams=(String[]) ob;
        for (int i = 0; i < needCKParams.length; i++)
        {
            String param = needCKParams[i];
            String elems[]= param.split("[#]");
            
            //如果仅有一个字段，代表要进行特别化的处理
            if(elems.length==3) 
            {
               String content = request.getParameter(elems[0]);
               String result= specialCheck(content,elems[1],elems[2]);
               if(StringUtils.isNotBlank(result))
                     return Printer.printConstsErrorMsg(result); 
            }else if(elems.length==4) 
            {
                String content = request.getParameter(elems[1]);
                content=content==null?"":content.trim();
                int minLength=Integer.parseInt(elems[2]);
                int maxLength=Integer.parseInt(elems[3]);
                //如果长度不在指定范围内有话，则直接返回给前台提示
                if(StringUtils.length(content)>maxLength || StringUtils.length(content)<minLength)
                {
                	LOGGER.info("参数异常 params:{}", param);
                    return Printer.printConstsErrorMsg(MessageFormat.format(Const.getMsg(1000), elems[0],minLength,maxLength));
                }
            }
        }
        return null;
    }

    /*
     * 正则配置
     */
    private static String specialCheck(String content, String regexType,String resultContent)
    {
      
        boolean result= cmu.getRegex(regexType, content);
        if(!result)
            return resultContent;
        return null;
    }
    /*
	 * IP地址
	 */
	public static String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                    	LOGGER.error(e.getMessage(), e);
                    }  
                    if(inet==null) return "0.0.0.0";
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            } 
            return ipAddress;
    }
}
