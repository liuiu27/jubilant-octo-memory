package com.cupdata.order.entity;

import com.cupdata.commons.model.BaseModel;

/**
 *服务产品主订单表
 */
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
	private Character orderType;

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

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrgOrderNo() {
		return orgOrderNo;
	}

	public void setOrgOrderNo(String orgOrderNo) {
		this.orgOrderNo = orgOrderNo;
	}

	public String getSupplierOrderNo() {
		return supplierOrderNo;
	}

	public void setSupplierOrderNo(String supplierOrderNo) {
		this.supplierOrderNo = supplierOrderNo;
	}

	public Long getOrgPrice() {
		return orgPrice;
	}

	public void setOrgPrice(Long orgPrice) {
		this.orgPrice = orgPrice;
	}

	public Long getSupplierPrice() {
		return supplierPrice;
	}

	public void setSupplierPrice(Long supplierPrice) {
		this.supplierPrice = supplierPrice;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public Character getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Character getOrderType() {
		return orderType;
	}

	public void setOrderType(Character orderType) {
		this.orderType = orderType;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getOrderFailDesc() {
		return orderFailDesc;
	}

	public void setOrderFailDesc(String orderFailDesc) {
		this.orderFailDesc = orderFailDesc;
	}

	public Character getIsNotify() {
		return isNotify;
	}

	public void setIsNotify(Character isNotify) {
		this.isNotify = isNotify;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
