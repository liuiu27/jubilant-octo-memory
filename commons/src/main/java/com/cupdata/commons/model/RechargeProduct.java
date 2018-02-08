package com.cupdata.commons.model;

/**
 * 
 * @ClassName: RechargeProduct
 * @Description: 花积分充值产品表
 * @author LinYong
 * @date 2017年6月30日 下午2:22:53
 * 
 */
public class RechargeProduct extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3495845053510046982L;

	/**
	 * 商户编号
	 */
	private String merchantCode;

	/**
	 * 商品logo
	 */
	private String productLogo;

	/**
	 * 产品名称
	 */
	private String productName;

	/**
	 * 产品编号
	 */
	private String productNo;

	/**
	 * 产品描述
	 */
	private String productDesc;

	/**
	 * 提示信息
	 */
	private String promptMsg;

	/**
	 * 营销描述
	 */
	private String marketingDesc;

	/**
	 * 产品类型 V：视频会员；S：社交平台充值；M：音乐会员；R：话费充值；T：流量充值；P：停车缴费；G:游戏充值
	 */
	private Character productType;

	/**
	 * 商户标识 TENCENT：腾讯；若为：RUOWEI；爱奇艺：IQIYI
	 */
	private String merchantFlag;

	/**
	 * 商户业务参数
	 */
	private String merchantParam;

	/**
	 * 产品状态 0：无效；1：有效。
	 */
	private Character status;

	/**
	 * 优惠标识
	 */
	private String couponTag;

	/**
	 * 充值成功短信模板
	 */
	private String successSmsTempl;

	/**
	 * 充值失败短信模板
	 */
	private String failSmsTempl;

	/**
	 * 输入框placeholder提示
	 */
	private String inputTips;

	/**
	 * 账号是否需要格式化 0：不需要，1：需要
	 */
	private String accountFormat;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getProductLogo() {
		return productLogo;
	}

	public void setProductLogo(String productLogo) {
		this.productLogo = productLogo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getMarketingDesc() {
		return marketingDesc;
	}

	public void setMarketingDesc(String marketingDesc) {
		this.marketingDesc = marketingDesc;
	}

	public Character getProductType() {
		return productType;
	}

	public void setProductType(Character productType) {
		this.productType = productType;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getMerchantParam() {
		return merchantParam;
	}

	public void setMerchantParam(String merchantParam) {
		this.merchantParam = merchantParam;
	}

	public String getMerchantFlag() {
		return merchantFlag;
	}

	public void setMerchantFlag(String merchantFlag) {
		this.merchantFlag = merchantFlag;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getCouponTag() {
		return couponTag;
	}

	public void setCouponTag(String couponTag) {
		this.couponTag = couponTag;
	}

	public String getSuccessSmsTempl() {
		return successSmsTempl;
	}

	public void setSuccessSmsTempl(String successSmsTempl) {
		this.successSmsTempl = successSmsTempl;
	}

	public String getFailSmsTempl() {
		return failSmsTempl;
	}

	public void setFailSmsTempl(String failSmsTempl) {
		this.failSmsTempl = failSmsTempl;
	}

	public String getPromptMsg() {
		return promptMsg;
	}

	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}

	public String getInputTips() {
		return inputTips;
	}

	public void setInputTips(String inputTips) {
		this.inputTips = inputTips;
	}

	public String getAccountFormat() {
		return accountFormat;
	}

	public void setAccountFormat(String accountFormat) {
		this.accountFormat = accountFormat;
	}

}
