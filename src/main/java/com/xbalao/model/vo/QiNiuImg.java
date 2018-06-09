package com.xbalao.model.vo;

/**
 * 七牛图片相关数据
 * 
 * @author Administrator
 * 
 */
public class QiNiuImg {
	private static String qiniu_accessKey = "M8yjuTlQQtEdFisB13sF_kgJNahapkGHxoq9jXlv",
			qiniu_secretKey = "ey4v2qm8pP7yZ4ZeX5-pvxaQRmPGIiMic-O3DJD5",
			qiniu_bucket = "xbalao",
			qiniu_smart = "small", qiniu_big = "big",qiniu_middling="middling",qiniu_visit_url="http://p9scstw14.bkt.clouddn.com";

	public static String getQiniu_visit_url() {
			return qiniu_visit_url;
	}

	public static void setQiniu_visit_url(String qiniu_visit_url) {
		QiNiuImg.qiniu_visit_url = qiniu_visit_url;
	}

	public  static String getQiniu_middling() {
		return qiniu_middling;
	}

	public static String getQiniu_accessKey() {
			return qiniu_accessKey;
	}


	public static String getQiniu_secretKey() {
			return qiniu_secretKey;
	}


	public static String getQiniu_bucket() {
			return qiniu_bucket;
	}



	public static String getQiniu_smart() {
		return qiniu_smart;
	}



	public static String getQiniu_big() {
		return qiniu_big;
	}



}
