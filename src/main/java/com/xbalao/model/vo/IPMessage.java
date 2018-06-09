package com.xbalao.model.vo;

import java.io.Serializable;

/**
 * Created by paranoid on 17-4-10.
 *
 * 显式地定义serialVersionUID有两种用途：
 *　1、在某些场合，希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有相同的serialVersionUID；
 *　2、在某些场合，不希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有不同的serialVersionUID。
 *
 * 具体详情希望大家百度
 */

public class IPMessage implements Serializable {
	private static final long serialVersionUID = 27778491953853486L;
	private String ipAddress;
    private int ipPort;
    private String ipType;
    private double ipSpeed;
    private int aliveTime;	
	public int getAliveTime() {
		return aliveTime;
	}
	public void setAliveTime(int aliveTime) {
		this.aliveTime = aliveTime;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getIpPort() {
		return ipPort;
	}
	public void setIpPort(int ipPort) {
		this.ipPort = ipPort;
	}
	public String getIpType() {
		return ipType;
	}
	public void setIpType(String ipType) {
		this.ipType = ipType;
	}
	public double getIpSpeed() {
		return ipSpeed;
	}
	public void setIpSpeed(double ipSpeed) {
		this.ipSpeed = ipSpeed;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.ipAddress+":"+this.ipPort;
	}
}
