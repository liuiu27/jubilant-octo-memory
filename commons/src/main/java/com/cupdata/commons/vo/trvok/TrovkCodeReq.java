package com.cupdata.commons.vo.trvok;

import lombok.Data;

@Data
public class TrovkCodeReq extends TrvokBaseVo{
	
	/**
	 * 订单编号
	 */
	private String outTradeNo;
	
	/**
	 * 产品标识符
	 */
	private String sku;
	
	/**
	 * 有效期 YYYY-MM-DD
	 */
	private String expire;
}
