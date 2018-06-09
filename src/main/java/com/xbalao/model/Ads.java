package com.xbalao.model;

import java.io.Serializable;

import com.cf88.model.annotation.Table;
import com.cf88.model.annotation.Transient;
/**
 * 广告位置
 * @author lanjun
 *
 */
@Table(tableName="t_ad")
public class Ads implements Serializable{
	@Transient
	private static final long serialVersionUID = 5873673925257049250L;
	@Transient
	private int id;
	private String img;
	private String url;
	private String desc;
	private String type;
	private int istop;
	public int getIstop() {
		return istop;
	}
	public void setIstop(int istop) {
		this.istop = istop;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
