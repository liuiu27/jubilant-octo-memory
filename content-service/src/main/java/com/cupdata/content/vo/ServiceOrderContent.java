package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

import com.cupdata.commons.model.BaseModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月9日 下午2:46:18
*/
@Getter
@Setter
public class ServiceOrderContent extends BaseModel{
	/**
	 * 主订单id
	 */
	@NotBlank
	private Long orderId;
	
	/**
	 * 产品编号
	 */
	@NotBlank
	private String productNo;
	
	/**
	 * 机构编号
	 */
	@NotBlank
	private String orgNo;
	
	/**
	 * 商户编号
	 */
	@NotBlank
	private String supNo;
	
	/**
	 * 手机号
	 */
	@NotBlank
	private String mobileNo;
	
	/**
	 * 渠道用户id
	 */
	@NotBlank
	private String userId;
	
	/**
	 * 用户名
	 */
	@NotBlank
	private String userName;
	
	/**
	 * 订单时间
	 */
	@NotBlank
	private String orderTime;
	
	/**
	 * 订单标题
	 */
	@NotBlank
	private String orderTitle;
	
	/**
	 * 订单详细
	 */
	@NotBlank
	private String orderInfo;
	
	/**
	 * 订单状态
	 */
	@NotBlank
	private String orderStatus;
	
	/**
	 * 订单金额
	 */
	@NotBlank
	private int orderAmt;
	
	/**
	 * 订单积分
	 */
	@NotBlank
	private int orderBonus;
	
	/**
	 * 实际支付积分
	 */
	@NotBlank
	private int payAmt;
	
	/**
	 * 实际支付积分
	 */
	@NotBlank
	private int payBonus;
	
	/**
	 * 实际支付权益
	 */
	@NotBlank
	private int payRight;
}
