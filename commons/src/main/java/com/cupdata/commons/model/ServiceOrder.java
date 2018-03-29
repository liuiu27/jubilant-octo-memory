package com.cupdata.commons.model;

import lombok.Data;

/**
 *服务产品主订单表
 */
@Data
public class ServiceOrder extends BaseModel{
	/**
	 * 机构编号
	 */
	private String orgNo;

	/**
	 * 服务供应商编号
	 */
 	private String supplierNo;

	/**
	 *订单编号
	 */
	private String orderNo;

	/**
	 *机构订单号
	 */
	private String orgOrderNo;

	/**
	 *供应商订单号
	 */
	private String supplierOrderNo;

	/**
	 *机构价格（单位：分）
	 */
	private Long orgPrice;

	/**
	 *服务供应商价格
	 */
	private Long supplierPrice;

	/**
	 *结算时间
	 * 格式为：yyyyMMdd
	 */
	private String settleDate;

	/**
	 *订单状态
	 *0：初始化；1：处理中；S：订单成功；F：订单失败；
	 */
	private Character orderStatus;

	/**
	 *订单类型
	 * 与服务产品类型相同
	 */
	private String orderType;

	/**
	 * 子订单类型
	 * 与服务产品子类型相同
	 */
	private String orderSubType;

	/**
	 *订单描述
	 */
	private String orderDesc;

	/**
	 *订单失败描述
	 */
	private String orderFailDesc;

	/**
	 *是否需要异步通知订单结果给机构
	 *0：不需要；1：需要
	 */
	private Character isNotify;

	/**
	 *异步通知URL
	 */
	private String notifyUrl;

	/**
	 *服务器节点信息
	 */
	private String nodeName;

	/**
	 *服务供应商标识
	 */
	private String supplierFlag;
}
