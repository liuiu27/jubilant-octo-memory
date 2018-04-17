package com.cupdata.shengda.vo;

/**
 * 
* @ClassName: ShengdaDrivingOrderRes 
* @Description: 盛大洗车权益下单响应参数Vo
* @author LinYong 
* @date 2016-6-17 下午03:56:12 
*
 */
public class ShengdaWashingOrderRes extends ShengdaOrderRes{
	/**
	 * 盛大串码，消费时输入串码进行消费
	 */
	private String encryptCode;

	public String getEncryptCode() {
		return encryptCode;
	}

	public void setEncryptCode(String encryptCode) {
		this.encryptCode = encryptCode;
	}
}
