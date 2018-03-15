package com.cupdata.commons.vo.content;

import com.cupdata.commons.model.BaseModel;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月9日 下午2:46:18
*/
@Data
public class ServiceOrderContent extends BaseModel{
	/**
	 * 主订单id
	 */
	private Long orderId;
	
	/**
	 * 产品编号
	 */
	private String productNo;
	
	/**
	 * 机构编号
	 */
	private String orgNo;
	
	/**
	 * 商户编号
	 */
	private String supNo;
	
	/**
	 * 手机号
	 */
	private String mobileNo;
	
	/**
	 * 渠道用户id
	 */
	private String userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 订单时间
	 */
	private String orderTime;
	
	/**
	 * 订单标题
	 */
	private String orderTitle;
	
	/**
	 * 订单详细
	 */
	private String orderInfo;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 订单金额
	 */
	private int orderAmt;
	
	/**
	 * 订单积分
	 */
	private int orderBonus;
	
	/**
	 * 实际支付积分
	 */
	private int payAmt;
	
	/**
	 * 实际支付积分
	 */
	private int payBonus;
	
	/**
	 * 实际支付权益
	 */
	private int payRight;
}
