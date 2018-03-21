package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

import com.cupdata.commons.vo.BaseRequest;

/**
 * @author liwei
 * @date   2018/3/8
 */
 
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentJumpReq extends BaseRequest{
	
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
