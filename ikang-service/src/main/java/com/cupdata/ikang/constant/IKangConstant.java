package com.cupdata.ikang.constant;

import org.apache.commons.lang3.StringUtils;

import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.MD5Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IKangConstant {
	
	/**
	 * 接口版本号
	 */
	public static final String VERSION = "0.1";
	/**
	 * 调用可预约日期接口的URL
	 * @return
	 */
	public static String getAppointURL(){
		String url = getIKANGInterfaceURL();
		if(StringUtils.isNoneBlank(url)){
			return url.concat("findServiceOrderDay");
		}
		return "";
	}
	
	/**
	 * 签名
	 */
	public static String getSignature(){
		return MD5Util.encode("_ikang@".concat(getOnlyCode()).concat("$_").concat(DateTimeUtil.getCurDate()));
	}
	
	public static String getUrlData(){
		return "&onlycode="+getOnlyCode()+"&signature="+getSignature()+"&version="+VERSION;
	}
	
	/**
	 * 调用保存订单信息URL
	 * @return
	 */
	public static String getSaveOrderURL(String bankCode){
		String url = getIKANGInterfaceURL();
		if(StringUtils.isNoneBlank(url)){
			return url.concat("saveOrderInfoAuto");
		}
		return "";
	}
	

	public static String getCheckDataBackURL(){
		String url = getIKANGInterfaceURL();
		if(StringUtils.isNoneBlank(url)){
			return url.concat("findCheckDataBack");
		}
		return "";
	}
	
	public static String getIKANGInterfaceURL(){
		String url = "";
		return url;
	}
	static String getOnlyCode(){
		String onlyCode = "";
		return onlyCode;
	}
}
