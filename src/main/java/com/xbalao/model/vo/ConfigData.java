package com.xbalao.model.vo;

import java.io.Serializable;

public class ConfigData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1575768545834569703L;
	private static String host;
	private static int port;
	private static int perPageTototal;
	private static String indexFlushPwd;

	public static String getIndexFlushPwd() {
		return indexFlushPwd;
	}

	public  void setIndexFlushPwd(String indexFlushPwd) {
		ConfigData.indexFlushPwd = indexFlushPwd;
	}

	public static int getPerPageTototal() {
		return perPageTototal;
	}

	public  void setPerPageTototal(int perPageTototal) {
		ConfigData.perPageTototal = perPageTototal;
	}

	public static String getHost() {
		return host;
	}

	public void setHost(String host) {
		ConfigData.host = host;
	}

	public static int getPort() {
		return port;
	}

	public void setPort(int port) {
		ConfigData.port = port;
	}
}
