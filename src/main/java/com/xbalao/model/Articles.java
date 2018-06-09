package com.xbalao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.cf88.model.annotation.Table;
import com.cf88.model.annotation.Transient;
@Table(tableName="t_article")
public class Articles implements Serializable{

	@Transient
	private static final long serialVersionUID = -1100662653615007824L;
	@Transient
	private int id;
	//中等小图
	private String shortImg;
	//最小图
	private String  shorterImg;
	private String  title;
	private String  keywrolds;
	private String  remark;
	private long   ctime;
	private String   ctime_str;
	private int  viewers;
	private int  _viewers;
	private String  content;
	private String  fromer;
	private String  editer;
	private int  status;
	private String  type;
	private int  istop;
	//来源的域名
	private String srcDomain;
	//抓取来源的ID
	private String srcId;
	@Transient
	private String url;
	@Transient
	private List<Imgs>  imgs = new ArrayList<Imgs>();
	public int get_viewers() {
		return _viewers;
	}
	public void set_viewers(int _viewers) {
		this._viewers = _viewers;
	}
	public List<Imgs> getImgs() {
		return imgs;
	}
	public void setImgs(List<Imgs> imgs) {
		this.imgs = imgs;
	}
	public String getSrcDomain() {
		return srcDomain;
	}
	public void setSrcDomain(String srcDomain) {
		this.srcDomain = srcDomain;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getShorterImg() {
		return shorterImg;
	}
	public void setShorterImg(String shorterImg) {
		this.shorterImg = shorterImg;
	}
	public String getCtime_str() {
		return ctime_str;
	}
	public void setCtime_str(String ctime_str) {
		this.ctime_str = ctime_str;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortImg() {
		return shortImg;
	}
	public void setShortImg(String shortImg) {
		this.shortImg = shortImg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeywrolds() {
		return keywrolds;
	}
	public void setKeywrolds(String keywrolds) {
		this.keywrolds = keywrolds;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public int getViewers() {
		return viewers;
	}
	public void setViewers(int viewers) {
		this.viewers = viewers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFromer() {
		return fromer;
	}
	public void setFromer(String fromer) {
		this.fromer = fromer;
	}
	public String getEditer() {
		return editer;
	}
	public void setEditer(String editer) {
		this.editer = editer;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIstop() {
		return istop;
	}
	public void setIstop(int istop) {
		this.istop = istop;
	}

}
