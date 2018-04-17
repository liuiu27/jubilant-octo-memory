package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

import com.cupdata.commons.model.BaseModel;

/**
 * @author liwei
 * @date   2018/3/8
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentTransaction extends BaseModel{
	
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
