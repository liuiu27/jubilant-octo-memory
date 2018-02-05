package com.cupdata.tencent.vo;

/**
 * 
* @ClassName: QQOpenRes 
* @Description: QQ会员充值接口响应参数类
* @author LinYong 
* @date 2017年6月29日 下午1:11:41 
*
 */
public class QQOpenRes {

	/**
	 * 错误码
	 * 0：成功；-1：开通/关闭失败；-2：其他原因失败
	 */
	private String result;
	
	/**
	 * 错误描述
	 */
	private String desc;

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
}
