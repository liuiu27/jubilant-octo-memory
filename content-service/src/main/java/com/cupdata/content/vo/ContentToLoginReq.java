package com.cupdata.content.vo;

import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月13日 下午2:03:38
*/
@Getter
@Setter
public class ContentToLoginReq {
	/**
	 * 流水号
	 */
	private String sipTranNo;
	
	/**
	 * 产品编号
	 */
	private String productNo;
}
