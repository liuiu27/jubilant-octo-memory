package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "service_product")
public class ServiceProduct implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * RECHARGE：充值；VOUCHER：券码；
     */
    @Column(name = "PRODUCT_TYPE")
    private String productType;

    /**
     * 供应商编号
     */
    @Column(name = "SUPPLIER_NO")
    private String supplierNo;

    /**
     * 服务产品编号
     */
    @Column(name = "PRODUCT_NO")
    private String productNo;

    /**
     * 服务产品名称
     */
    @Column(name = "PRODUCT_NAME")
    private String productName;

    /**
     * 服务产品描述
     */
    @Column(name = "PRODUCT_DESC")
    private String productDesc;

    /**
     * 供应商价格 单位：人民币-分
     */
    @Column(name = "SUPPLIER_PRICE")
    private Integer supplierPrice;

    /**
     * 针对充值产品；单位：月
     */
    @Column(name = "RECHARGE_DURATION")
    private Integer rechargeDuration;

    /**
     * 针对充值产品；单位：元
     */
    @Column(name = "RECHARGE_AMT")
    private Integer rechargeAmt;

    /**
     * 针对充值产品业务（流量充值）；单位：M兆
     */
    @Column(name = "RECHARGE_TRAFFIC")
    private Integer rechargeTraffic;

    /**
     * 针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     */
    @Column(name = "RECHARGE_NUMBER")
    private Integer rechargeNumber;

    /**
     * 供应商分配，用于识别该业务的唯一标识
     */
    @Column(name = "SUPPLIER_PARAM")
    private String supplierParam;

    /**
     * 创建人
     */
    @Column(name = "CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 修改人
     */
    @Column(name = "UPDATE_BY")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "SERVICE_APPLICATION_PATH")
    private String serviceApplicationPath;

    @Column(name = "PRODUCT_SUB_TYPE")
    private String productSubType;

    /**
     * 商品首页，使用页面
     */
    @Column(name = "CONTENT_INDEX_URL")
    private String contentIndexUrl;

    private static final long serialVersionUID = 1L;

    /**
     * 获取ID
     *
     * @return ID - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取RECHARGE：充值；VOUCHER：券码；
     *
     * @return PRODUCT_TYPE - RECHARGE：充值；VOUCHER：券码；
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 设置RECHARGE：充值；VOUCHER：券码；
     *
     * @param productType RECHARGE：充值；VOUCHER：券码；
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * 获取供应商编号
     *
     * @return SUPPLIER_NO - 供应商编号
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 设置供应商编号
     *
     * @param supplierNo 供应商编号
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 获取服务产品编号
     *
     * @return PRODUCT_NO - 服务产品编号
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * 设置服务产品编号
     *
     * @param productNo 服务产品编号
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    /**
     * 获取服务产品名称
     *
     * @return PRODUCT_NAME - 服务产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置服务产品名称
     *
     * @param productName 服务产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取服务产品描述
     *
     * @return PRODUCT_DESC - 服务产品描述
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * 设置服务产品描述
     *
     * @param productDesc 服务产品描述
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * 获取供应商价格 单位：人民币-分
     *
     * @return SUPPLIER_PRICE - 供应商价格 单位：人民币-分
     */
    public Integer getSupplierPrice() {
        return supplierPrice;
    }

    /**
     * 设置供应商价格 单位：人民币-分
     *
     * @param supplierPrice 供应商价格 单位：人民币-分
     */
    public void setSupplierPrice(Integer supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    /**
     * 获取针对充值产品；单位：月
     *
     * @return RECHARGE_DURATION - 针对充值产品；单位：月
     */
    public Integer getRechargeDuration() {
        return rechargeDuration;
    }

    /**
     * 设置针对充值产品；单位：月
     *
     * @param rechargeDuration 针对充值产品；单位：月
     */
    public void setRechargeDuration(Integer rechargeDuration) {
        this.rechargeDuration = rechargeDuration;
    }

    /**
     * 获取针对充值产品；单位：元
     *
     * @return RECHARGE_AMT - 针对充值产品；单位：元
     */
    public Integer getRechargeAmt() {
        return rechargeAmt;
    }

    /**
     * 设置针对充值产品；单位：元
     *
     * @param rechargeAmt 针对充值产品；单位：元
     */
    public void setRechargeAmt(Integer rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
    }

    /**
     * 获取针对充值产品业务（流量充值）；单位：M兆
     *
     * @return RECHARGE_TRAFFIC - 针对充值产品业务（流量充值）；单位：M兆
     */
    public Integer getRechargeTraffic() {
        return rechargeTraffic;
    }

    /**
     * 设置针对充值产品业务（流量充值）；单位：M兆
     *
     * @param rechargeTraffic 针对充值产品业务（流量充值）；单位：M兆
     */
    public void setRechargeTraffic(Integer rechargeTraffic) {
        this.rechargeTraffic = rechargeTraffic;
    }

    /**
     * 获取针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     *
     * @return RECHARGE_NUMBER - 针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     */
    public Integer getRechargeNumber() {
        return rechargeNumber;
    }

    /**
     * 设置针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     *
     * @param rechargeNumber 针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     */
    public void setRechargeNumber(Integer rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    /**
     * 获取供应商分配，用于识别该业务的唯一标识
     *
     * @return SUPPLIER_PARAM - 供应商分配，用于识别该业务的唯一标识
     */
    public String getSupplierParam() {
        return supplierParam;
    }

    /**
     * 设置供应商分配，用于识别该业务的唯一标识
     *
     * @param supplierParam 供应商分配，用于识别该业务的唯一标识
     */
    public void setSupplierParam(String supplierParam) {
        this.supplierParam = supplierParam;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_BY - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_DATE - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改人
     *
     * @return UPDATE_BY - 修改人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置修改人
     *
     * @param updateBy 修改人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取修改时间
     *
     * @return UPDATE_DATE - 修改时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改时间
     *
     * @param updateDate 修改时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return SERVICE_APPLICATION_PATH
     */
    public String getServiceApplicationPath() {
        return serviceApplicationPath;
    }

    /**
     * @param serviceApplicationPath
     */
    public void setServiceApplicationPath(String serviceApplicationPath) {
        this.serviceApplicationPath = serviceApplicationPath;
    }

    /**
     * @return PRODUCT_SUB_TYPE
     */
    public String getProductSubType() {
        return productSubType;
    }

    /**
     * @param productSubType
     */
    public void setProductSubType(String productSubType) {
        this.productSubType = productSubType;
    }

    /**
     * 获取商品首页，使用页面
     *
     * @return CONTENT_INDEX_URL - 商品首页，使用页面
     */
    public String getContentIndexUrl() {
        return contentIndexUrl;
    }

    /**
     * 设置商品首页，使用页面
     *
     * @param contentIndexUrl 商品首页，使用页面
     */
    public void setContentIndexUrl(String contentIndexUrl) {
        this.contentIndexUrl = contentIndexUrl;
    }
}