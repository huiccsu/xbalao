package com.xbalao.model;

import java.io.Serializable;
import com.cf88.model.annotation.Table;
import com.cf88.model.annotation.Transient;
import net.sf.json.JSONObject;

@Table(tableName="t_article_url")
public class ArticleUrls implements Serializable{
	@Transient
	private static final long serialVersionUID = 5991031519936981892L;
	@Transient
	private int id;
	private String domain;
	private String url;
	private String rule;
	//1:正常启动 0：未启用
	private int status;
	//时间格式规则
	private String timeFormate;
	private String netName;
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getTimeFormate() {
		return timeFormate;
	}
	public void setTimeFormate(String timeFormate) {
		this.timeFormate = timeFormate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public JSONObject getRuleJson() {
		return JSONObject.fromObject(rule);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
