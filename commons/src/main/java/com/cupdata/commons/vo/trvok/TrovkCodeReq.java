package com.cupdata.commons.vo.trvok;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;

@Data
public class TrovkCodeReq extends BaseRequest{
	
	/**
	 * 订单编号
	 */
	private String outTradeNo;
	
	/**
	 * 产品标识符
	 */
	private String sku;
}
