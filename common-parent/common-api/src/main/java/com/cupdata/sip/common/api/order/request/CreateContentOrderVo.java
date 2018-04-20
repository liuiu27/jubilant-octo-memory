package com.cupdata.sip.common.api.order.request;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月19日 下午3:27:24
*/
@Data
public class CreateContentOrderVo {
	
	/**
	 * 手机号
	 */
	private String mobileNo;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 机构编号
	 */
	private String orgNo;
	
	/**
	 * 供应商编号
	 */
	private String supNo;
	
	/**
	 * 产品编号
	 */
	private String productNo;
	
	/**
	 * 订单金额
	 */
	private Integer orderAmt;
	
	/**
	 * 供应商订单号	
	 */
	private String supOrderNo;
	
	/**
	 * 供应商订单时间
	 */
	private String supOrderTime;
	
	/**
	 * 订单标题
	 */
	private String orderTitle;
	
	/**
	 * 订单信息
	 */
	private String orderInfo;
	
	/**
	 * 产品数量
	 */
	private Integer productNum;
	
	/**
	 * 订单页面需展示信息
	 */
	private String orderShow;
	
	
	/**
	 * 异步通知地址
	 */
	private String notifyUrl;
}
