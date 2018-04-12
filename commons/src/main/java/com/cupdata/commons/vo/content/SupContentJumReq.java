package com.cupdata.commons.vo.content;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月11日 下午6:18:41
*/
@Data
public class SupContentJumReq {
	
	/***
	 * 流水号
	 */
	private String sipTranNo;
	
	/**
	 * 时间戳  
	 */
	 private String timestamp;
	
	/**
	 * 手机号码
	 */
	private String mobileNo;
	
	/**
	 * 登录状态
	 */
	private String loginFlag;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 用户名	
	 */
	private String userName;
	
	/**
	 * 登录URL
	 */
	private String loginUrl;
	
	/**
	 * 支付URL
	 */
	private String payUrl;
}
