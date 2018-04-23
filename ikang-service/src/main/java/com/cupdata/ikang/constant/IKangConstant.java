package com.cupdata.ikang.constant;

import com.cupdata.sip.common.lang.constant.TimeConstants;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.sip.common.lang.utils.MD5Util;

public class IKangConstant {
	
	/**
	 * 接口版本号
	 */
	public static final String VERSION = "0.1";
	
	
	public static final String url = "http://uatservices.health.ikang.com/ikang-service/rs/cooperation/";
	
	/**
	 * 调用可预约日期接口的URL
	 * @return
	 */
	public static String getAppointURL(){
			return url + "findServiceOrderDay";
	}
	
	/**
	 * 签名
	 */
	public static String getSignature(){
		return MD5Util.encode("_ikang@".concat(getOnlyCode()).concat("$_").concat(DateTimeUtil.getStringDate(TimeConstants.DATE_PATTERN_1)));
	}
	
	public static String getUrlData(){
		return "&onlycode="+getOnlyCode()+"&signature="+getSignature()+"&version="+VERSION;
	}
	
	/**
	 * 调用保存订单信息URL
	 * @return
	 */
	public static String getSaveOrderURL(){
		return url + "saveOrderInfoAuto";
	}
	

	public static String getCheckDataBackURL(){
			return url + "findCheckDataBack";
	}
	
	static String getOnlyCode(){
		return "fNE7NVk$4*sz5@5";
	}
}
