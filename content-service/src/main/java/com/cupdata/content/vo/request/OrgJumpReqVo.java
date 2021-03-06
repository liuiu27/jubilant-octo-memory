package com.cupdata.content.vo.request;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgJumpReqVo{
	
	/**
	 * 服务产品编号
	 */
	@NotBlank
	private String productNo;
	
	/**
	 * 手机号码
	 */
	@NotBlank
	private String mobileNo;
	
	/**
	 * 登录状态
	 */
	@NotBlank
	private String loginFlag;
	
	/**
	 * 用户ID
	 */
	@NotBlank
	private String userId;
	
	/**
	 * 用户名	
	 */
	@NotBlank
	private String userName;
	
	/**
	 * 登录URL
	 */
	@NotBlank
	private String loginUrl;
	
	/**
	 * 支付URL
	 */
	@NotBlank
	private String payUrl;
	
	/**
	 * 交易流水号
	 */
	@NotBlank
	private String 	sipTranNo;
	
}
