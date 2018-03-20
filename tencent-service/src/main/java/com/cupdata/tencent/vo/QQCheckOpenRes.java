package com.cupdata.tencent.vo;

/**
 * 
* @ClassName: QQCheckOpenRes 
* @Description: QQ会员开通鉴权接口
* @author LinYong 
* @date 2017年6月29日 上午11:08:03 
*
 */
public class QQCheckOpenRes {
	/**
	 * 错误码
	 */
	private String result;
	
	/**
	 * 错误描述
	 */
	private String desc;
	
	/**
	 * 腾讯自定义字段
	 * 当错误码为0时有值，在开通请求消息中需要透传字段。
	 */
	private String txparam;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTxparam() {
		return txparam;
	}

	public void setTxparam(String txparam) {
		this.txparam = txparam;
	}
}
