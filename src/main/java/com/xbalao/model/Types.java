package com.xbalao.model;

import java.io.Serializable;

import com.cf88.model.annotation.Table;
import com.cf88.model.annotation.Transient;

/**
 * 新闻分类
 * @author lanjun
 *
 */
@Table(tableName="t_type")
public class Types implements Serializable{
	@Transient
	private static final long serialVersionUID = -6441918748071830382L;
	@Transient
	private int id;
	private String name;
	private int  status;
	private int  sorted;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSorted() {
		return sorted;
	}
	public void setSorted(int sorted) {
		this.sorted = sorted;
	}

}
