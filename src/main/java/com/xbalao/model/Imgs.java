package com.xbalao.model;

import java.io.Serializable;

import com.cf88.model.annotation.Table;
import com.cf88.model.annotation.Transient;

@Table(tableName="t_imgs")
public class Imgs implements Serializable{

	@Transient
	private static final long serialVersionUID = -6765861230502955903L;
	@Transient
	private int id;
	private String 	src; 
	//在七牛的相对路劲
	private String deSrc;
	private int status;
	private long ctime;
	private long dtime;
	private String domain;
	private String 	srcId;
	public String getDeSrc() {
		return deSrc;
	}
	public void setDeSrc(String deSrc) {
		this.deSrc = deSrc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public long getDtime() {
		return dtime;
	}
	public void setDtime(long dtime) {
		this.dtime = dtime;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	
}
