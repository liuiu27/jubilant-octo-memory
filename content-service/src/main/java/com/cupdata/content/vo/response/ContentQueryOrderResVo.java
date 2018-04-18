package com.cupdata.content.vo.response;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午2:28:11
*/
@Getter
@Setter
public class ContentQueryOrderResVo{
	/**
	 * 返回标记记  0未 支付/退货  1支付/退货 失败  2支付/退货 成功
	 */
	@NotBlank
	private String resultCode;
	
	/**
	 * 交易类型
	 */
	@NotBlank
	private String tranType;
	
	/**
	 * 供应商订单号
	 */
	@NotBlank
	private String supOrderNo;
	
	/**
	 * 平台订单号
	 */
	@NotBlank
	private String sipOrderNo;
	
}
