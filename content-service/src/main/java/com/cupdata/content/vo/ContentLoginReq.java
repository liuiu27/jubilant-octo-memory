package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月12日 下午4:02:00
*/
@Getter
@Setter
public class ContentLoginReq {
	/**
	 * 供应商回跳地址
	 */
	@NotBlank
	private String callBackUrl;
}
