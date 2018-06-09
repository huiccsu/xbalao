package com.xbalao.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.cf88.service.exception.CFException;
import com.xbalao.model.vo.IPMessage;



public class HttpClientSpider {
	
	public  Logger log = LogManager.getLogger(getClass()); 
	 
	public String host;
	public Integer port;
	public String charset="UTF-8";
	public int timeout=60000 ;
	public HttpClientSpider(String host, Integer port, String charset,Integer timeout) {
		super();
		this.host = host;
		this.port = port;
		this.charset = charset;
		this.timeout = timeout;
	}
	public HttpClientSpider(String host, Integer port, String charset) {
		super();
		this.host = host;
		this.port = port;
		this.charset = charset;
	}
	public HttpClientSpider(String host, Integer port) {
		super();
		this.host = host;
		this.port = port;
	}
	public HttpClientSpider() {
		super();
	}
	public HttpClientSpider(IPMessage iPMessage) {
		if(iPMessage!=null && StringUtils.isNoneBlank(iPMessage.getIpAddress())  && iPMessage.getIpPort()>0)
		{
			this.host = iPMessage.getIpAddress();
			this.port = iPMessage.getIpPort();
		}
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	
	public String getHtml(String url)
	{
		try {
			return (String) getFromResponseBody(url, String.class);
		} catch (IOException e) {
			log.error("url->{} error->{}",url,e.getMessage()); 
			return null;
		}
	}
	
	public Object getFromResponseBody(String url,Class type) throws HttpException, IOException
	{
		HttpClient httpClient = new HttpClient();
		if(StringUtils.isNoneBlank(host) && port!=null)
			httpClient.getHostConfiguration().setHost(host, port);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(this.getTimeout());
        HttpMethod method = new GetMethod(url);
    	method.getParams().setContentCharset(this.getCharset());
        httpClient.executeMethod(method);
        StatusLine statusLine= method.getStatusLine();
        if(statusLine.getStatusCode()==200)
        {
        	if(type==String.class)
        		return method.getResponseBodyAsString();
        	else if(type==Byte.class)
        		return getByteFromMethod( method);
        }
        else log.info("url->{} statuccode->{}",url,statusLine.getStatusCode());
        return null;
	}
	
	/**
	 * 测试代理
	 * @return
	 */
	public boolean testProxy(String host, int port,int timeout)
	{
		this.host=host;
		this.port=port;
		this.timeout=timeout;
		String html=getHtml("https://www.baidu.com");
		if(StringUtils.isBlank(html)) return false;
		else return true;
	}
	/**
	 * 测试代理
	 * @return
	 */
	public boolean testProxy(String host, int port)
	{
		return testProxy(host,port,5000);
	}
	
	public byte[] getByte(String url)
	{
		try {
			return (byte[]) getFromResponseBody(url, Byte.class);
		} catch (IOException e) {
			log.error("url->{} error->{}",url,e.getMessage()); 
			return null;
		}
	}

	private byte[]  getByteFromMethod(HttpMethod method) throws IOException
	{
 	   try {
			return readInputStream(method.getResponseBodyAsStream());
		} catch (Exception e) {
			log.error("error->{}",e.getMessage()); 
		}
 	   return null;
	}
	
	/**
	 * 从输入流中获取数据
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception{
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int len = 0;
	    while( (len=inStream.read(buffer)) != -1 ){
	        outStream.write(buffer, 0, len);
	    }
	    inStream.close();
	    return outStream.toByteArray();
	}
	
	public Document getElements(String url) {
		String html=getHtml( url);
		if(html==null) throw new CFException("url->"+url+" html content is null");
		return  Jsoup.parse(html);
	}
	
}
