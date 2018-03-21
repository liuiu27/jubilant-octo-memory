package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午2:08:25
*/
@Getter
@Setter
public class ContentQueryOrderReq extends BaseRequest{
	/**
	 * 供应商订单号
	 */
	@NotBlank
	private String supOrderNo;
}
