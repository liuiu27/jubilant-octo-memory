package com.cupdata.content.vo;

import com.cupdata.commons.vo.BaseRequest;

/**
 * @author liwei
 * @date   2018/3/8
 */
 
import lombok.Data;

@Data
public class ContentJumpReq extends BaseRequest{
	
	/**
	 * 服务产品编号
	 */
	private String productNo;
	
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
	
	/**
	 * 交易流水号
	 */
	private String 	sipTranNo;
	
}
