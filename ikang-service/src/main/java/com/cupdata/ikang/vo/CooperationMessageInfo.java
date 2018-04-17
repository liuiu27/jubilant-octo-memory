package com.cupdata.ikang.vo;

import java.util.List;

public class CooperationMessageInfo {

	private String code;
	private String message;
	
	private List<FescoOrderDay> list;
	
	private List<CheckDataBackInfo> checkDataList;
	/**
	 * 1为正确，其他值为错误
	 */
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 错误内容/正确
	 */
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<FescoOrderDay> getList() {
		return list;
	}
	public void setList(List<FescoOrderDay> list) {
		this.list = list;
	}
	public List<CheckDataBackInfo> getCheckDataList() {
		return checkDataList;
	}
	public void setCheckDataList(List<CheckDataBackInfo> checkDataList) {
		this.checkDataList = checkDataList;
	}
	
	
}
