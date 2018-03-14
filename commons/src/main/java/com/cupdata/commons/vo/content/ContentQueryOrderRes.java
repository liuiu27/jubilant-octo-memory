package com.cupdata.commons.vo.content;

import com.cupdata.commons.vo.BaseData;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午2:28:11
*/
@Data
public class ContentQueryOrderRes extends BaseData{
	/**
	 * 返回标记记  0未 支付/退货  1支付/退货 失败  2支付/退货 成功
	 */
	private String resultCode;
	
	/**
	 * 交易类型
	 */
	private String tranType;
	
	/**
	 * 供应商订单号
	 */
	private String supOrderNo;
	
	/**
	 * 平台订单号
	 */
	private String sipOrderNo;
	
}
