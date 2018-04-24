package com.cupdata.content.vo.request;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午2:08:25
*/
@Getter
@Setter
public class ContentQueryOrderReqVo{
	/**
	 * 供应商订单号
	 */
	@NotBlank
	private String supOrderNo;

	/**
	 * 交易类型 ： 0 支付  1 退货
	 */
	@NotBlank
	String tranType;
}
