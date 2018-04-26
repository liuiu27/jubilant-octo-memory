package com.cupdata.content.vo.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月12日 下午4:02:00
*/
@Getter
@Setter
public class SupLoginReqVo {
	
	/**
	 * 流水号
	 */
	@NotBlank
	private String sipTranNo;
	
	/**
	 * 供应商回跳地址
	 */
	@NotBlank
	private String callBackUrl;
	
}
