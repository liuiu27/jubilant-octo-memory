package com.cupdata.content.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author liwei
 * @date 2018/3/8
 */

@Getter
@Setter
public class ContentTransactionVO {
	
	/**
	 * 交易流水编号
	 */
	@NotBlank
	private String tranNo;
	
	/**
	 * 商品编号
	 */
	@NotBlank
	private String productNo;
	
	/**
	 * 交易类型
	 */
	@NotBlank
	private String tranType;
	
	/**
	 * 交易描述
	 */
	@NotBlank
	private String tranDesc;
	
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
	 * 请求信息
	 */
	@NotBlank
	private String requestInfo;

	
}
