package com.cupdata.content.vo;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午2:08:25
*/
@Data
public class ContentQueryOrderReq extends BaseRequest{
	/**
	 * 供应商订单号
	 */
	private String supOrderNo;
}
