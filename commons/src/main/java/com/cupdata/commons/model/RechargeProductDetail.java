package com.cupdata.commons.model;

import com.cupdata.commons.utils.CommonUtils;

/**
 * 
 * @ClassName: RechargeProductProperty
 * @Description: 充值产品属性表
 * @author LinYong
 * @date 2017年6月30日 下午2:38:43
 * 
 */
public class RechargeProductDetail extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8550366574515821292L;

	/**
	 * 充值产品ID
	 */
	private Long productId;

	/**
	 * 属性标题
	 */
	private String propertyTitle;

	/**
	 * 原始价格 单位：人民币-分
	 */
	private Long originalPrice;

	/**
	 * 花积分价格 单位：人民币-分
	 */
	private Long hjfPrice;

	/**
	 * 商户结算价
	 */
	private Long merchantSettlePrice;

	/**
	 * 开通时长
	 */
	private Long openDuration;

	/**
	 * 充值金额 单位：元
	 */
	private Long rechargeAmt;

	/**
	 * 充值流量 单位：M
	 */
	private Long rechargeTraffic;

	/**
	 * 充值件数
	 */
	private Long rechargeNumber;

	/**
	 * 排序 数值越小，越排列在前
	 */
	private Integer sequence;

	/**
	 * 流量充值运营商 TELECOM：中国电信 MOBILE：中国移动 UNICOM：中国联通
	 */
	private String operator;

	/**
	 * 商户业务参数
	 */
	private String merchantParam;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getPropertyTitle() {
		return propertyTitle;
	}

	public void setPropertyTitle(String propertyTitle) {
		this.propertyTitle = propertyTitle;
	}

	public Long getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Long originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getFormatedOriginalPrice() {
		Long price = originalPrice;
		if (null == price || 0L == price) {
			price = 0L;
		}

		return CommonUtils.formatDouble(price / 100.00d, "###,##0.00");
	}

	public Long getHjfPrice() {
		return hjfPrice;
	}

	public void setHjfPrice(Long hjfPrice) {
		this.hjfPrice = hjfPrice;
	}

	public Long getMerchantSettlePrice() {
		return merchantSettlePrice;
	}

	public void setMerchantSettlePrice(Long merchantSettlePrice) {
		this.merchantSettlePrice = merchantSettlePrice;
	}

	public String getFormatedHjfPrice() {
		return CommonUtils.formatDouble(hjfPrice / 100.00d, "###,##0.00");
	}

	public Long getOpenDuration() {
		return openDuration;
	}

	public void setOpenDuration(Long openDuration) {
		this.openDuration = openDuration;
	}

	public Long getRechargeAmt() {
		return rechargeAmt;
	}

	public void setRechargeAmt(Long rechargeAmt) {
		this.rechargeAmt = rechargeAmt;
	}

	public Long getRechargeTraffic() {
		return rechargeTraffic;
	}

	public void setRechargeTraffic(Long rechargeTraffic) {
		this.rechargeTraffic = rechargeTraffic;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getMerchantParam() {
		return merchantParam;
	}

	public void setMerchantParam(String merchantParam) {
		this.merchantParam = merchantParam;
	}

	public Long getRechargeNumber() {
		return rechargeNumber;
	}

	public void setRechargeNumber(Long rechargeNumber) {
		this.rechargeNumber = rechargeNumber;
	}

}
