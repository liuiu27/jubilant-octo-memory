package com.cupdate.order.domain;

import java.io.Serializable;
import java.util.Date;

public class ServiceOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer id;//id
	private String orderNo;//订单编号
	private String bankCode;//银行号
	private String orgNo;//机构号
	private String bankOrderNo;//银行订单号
	private String supplyOrderNo;//供应商订单号
	private String supplyNo;//供应商号
	private String productNo;//产品号
	private Date settleDate;//结算时间
	private String orderStatus;//订单状态
	private String subOrderNo;//子订单号
	private double orgCostPrice;//银行价格
	private double supplyerCostPrice;//供应商价格
	private String orderType;//订单类型
	private String orderDesc;//订单描述
	private Date createDate;//创建时间
	private Date updateDate;//修改时间


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getOrgNo() {
		return orgNo;
	}
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	public String getSupplyOrderNo() {
		return supplyOrderNo;
	}
	public void setSupplyOrderNo(String supplyOrderNo) {
		this.supplyOrderNo = supplyOrderNo;
	}
	public String getSupplyNo() {
		return supplyNo;
	}
	public void setSupplyNo(String supplyNo) {
		this.supplyNo = supplyNo;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public Date getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getSubOrderNo() {
		return subOrderNo;
	}
	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}
	public double getOrgCostPrice() {
		return orgCostPrice;
	}
	public void setOrgCostPrice(double orgCostPrice) {
		this.orgCostPrice = orgCostPrice;
	}
	public double getSupplyerCostPrice() {
		return supplyerCostPrice;
	}
	public void setSupplyerCostPrice(double supplyerCostPrice) {
		this.supplyerCostPrice = supplyerCostPrice;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "ServiceOrder [id=" + id + ", orderNo=" + orderNo
				+ ", bankCode=" + bankCode + ", orgNo=" + orgNo
				+ ", bankOrderNo=" + bankOrderNo + ", supplyOrderNo="
				+ supplyOrderNo + ", supplyNo=" + supplyNo + ", productNo="
				+ productNo + ", settleDate=" + settleDate + ", orderStatus="
				+ orderStatus + ", subOrderNo=" + subOrderNo
				+ ", orgCostPrice=" + orgCostPrice + ", supplyerCostPrice="
				+ supplyerCostPrice + ", orderType=" + orderType
				+ ", orderDesc=" + orderDesc + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + "]";
	}

	
}
