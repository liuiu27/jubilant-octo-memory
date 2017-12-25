package com.cupdata.commons.model;

/**
 * @Auth: LinYong
 * @Description:服务产品表
 * @Date: 16:58 2017/12/14
 */
public class ServiceProduct extends BaseModel {
    /**
     *服务产品类型
     * RECHARGE：充值；VOUCHER：券码；
     */
    private String productType;

    /**
     *供应商编号
     */
    private String supplierNo;

    /**
     *服务产品编号
     */
    private String productNo;

    /**
     *服务产品名称
     */
    private String productName;

    /**
     *服务产品描述
     */
    private String productDesc;

    /**
     *供应商价格
     * 单位：人民币-分
     */
    private Long supplierPrice;

    /**
     *充值开通时长
     * 针对充值产品；单位：月
     */
    private Long rechargeDuration;

    /**
     *充值金额
     * 针对充值产品；单位：元
     */
    private Long rechargeAmt;

    /**
     *充值流量
     * 针对充值产品业务（流量充值）；单位：M兆
     */
    private Long rechargeTraffic;

    /**
     *充值数量
     * 针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     */
    private Long rechargeNumber;

    /**
     *供应商参数
     */
    private String supplierParam;

    /**
     *服务产品配置ID
     */
    private String configId;

    /**
     *服务供应商标识
     */
    private String supplierFlag;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
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

    public Long getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(Long supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public Long getRechargeDuration() {
        return rechargeDuration;
    }

    public void setRechargeDuration(Long rechargeDuration) {
        this.rechargeDuration = rechargeDuration;
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

    public Long getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(Long rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public String getSupplierParam() {
        return supplierParam;
    }

    public void setSupplierParam(String supplierParam) {
        this.supplierParam = supplierParam;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getSupplierFlag() {
        return supplierFlag;
    }

    public void setSupplierFlag(String supplierFlag) {
        this.supplierFlag = supplierFlag;
    }
}