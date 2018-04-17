package com.cupdata.sip.common.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "service_content_transaction_log")
public class ServiceContentTransactionLog implements Serializable {
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 交易流水编号
     */
    @Column(name = "TRAN_NO")
    private String tranNo;

    /**
     * 商品编号
     */
    @Column(name = "PRODUCT_NO")
    private String productNo;

    /**
     * 交易类型
     */
    @Column(name = "TRAN_TYPE")
    private String tranType;

    /**
     * 交易描述
     */
    @Column(name = "TRAN_DESC")
    private String tranDesc;

    /**
     * 机构编号
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 商户编号
     */
    @Column(name = "SUP_NO")
    private String supNo;

    /**
     * 请求信息
     */
    @Column(name = "REQUEST_INFO")
    private String requestInfo;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    private static final long serialVersionUID = 1L;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取交易流水编号
     *
     * @return TRAN_NO - 交易流水编号
     */
    public String getTranNo() {
        return tranNo;
    }

    /**
     * 设置交易流水编号
     *
     * @param tranNo 交易流水编号
     */
    public void setTranNo(String tranNo) {
        this.tranNo = tranNo;
    }

    /**
     * 获取商品编号
     *
     * @return PRODUCT_NO - 商品编号
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * 设置商品编号
     *
     * @param productNo 商品编号
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    /**
     * 获取交易类型
     *
     * @return TRAN_TYPE - 交易类型
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * 设置交易类型
     *
     * @param tranType 交易类型
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    /**
     * 获取交易描述
     *
     * @return TRAN_DESC - 交易描述
     */
    public String getTranDesc() {
        return tranDesc;
    }

    /**
     * 设置交易描述
     *
     * @param tranDesc 交易描述
     */
    public void setTranDesc(String tranDesc) {
        this.tranDesc = tranDesc;
    }

    /**
     * 获取机构编号
     *
     * @return ORG_NO - 机构编号
     */
    public String getOrgNo() {
        return orgNo;
    }

    /**
     * 设置机构编号
     *
     * @param orgNo 机构编号
     */
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    /**
     * 获取商户编号
     *
     * @return SUP_NO - 商户编号
     */
    public String getSupNo() {
        return supNo;
    }

    /**
     * 设置商户编号
     *
     * @param supNo 商户编号
     */
    public void setSupNo(String supNo) {
        this.supNo = supNo;
    }

    /**
     * 获取请求信息
     *
     * @return REQUEST_INFO - 请求信息
     */
    public String getRequestInfo() {
        return requestInfo;
    }

    /**
     * 设置请求信息
     *
     * @param requestInfo 请求信息
     */
    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    /**
     * @return CREATE_BY
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * @param createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * @return CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return UPDATE_BY
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * @param updateBy
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * @return UPDATE_DATE
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}