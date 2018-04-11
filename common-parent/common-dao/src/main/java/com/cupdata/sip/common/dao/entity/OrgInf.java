package com.cupdata.sip.common.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "org_inf")
public class OrgInf implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 机构号
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 机构名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;

    /**
     * 机构描述
     */
    @Column(name = "ORG_DESC")
    private String orgDesc;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 联系人
     */
    @Column(name = "CONTACTS")
    private String contacts;

    /**
     * 手机号
     */
    @Column(name = "MOBILE_NO")
    private String mobileNo;

    /**
     * CUPD:银数机构；BANK：银行机构
     */
    @Column(name = "ORG_TYPE")
    private String orgType;

    /**
     * 只有当ORG_TYPE为BANK时，此字段非空表示该机构所属的银行
     */
    @Column(name = "BANK_CODE")
    private String bankCode;

    /**
     * 机构公钥
     */
    @Column(name = "ORG_PUB_KEY")
    private String orgPubKey;

    /**
     * 平台公钥
     */
    @Column(name = "SIP_PUB_KEY")
    private String sipPubKey;

    /**
     * 平台私钥
     */
    @Column(name = "SIP_PRI_KEY")
    private String sipPriKey;

    /**
     * IP白名单
     */
    @Column(name = "WHITE_IPS")
    private String whiteIps;

    /**
     * 0：无效；1：有效。
     */
    @Column(name = "VALID_STATUS")
    private String validStatus;

    /**
     * 内容引入机构退款地址
     */
    @Column(name = "CONTENT_ORDER_REFUND_URL")
    private String contentOrderRefundUrl;

    /**
     * 内容引入机构订单查询地址
     */
    @Column(name = "CONTENT_ORDER_QUERY_URL")
    private String contentOrderQueryUrl;

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
     * 获取机构号
     *
     * @return ORG_NO - 机构号
     */
    public String getOrgNo() {
        return orgNo;
    }

    /**
     * 设置机构号
     *
     * @param orgNo 机构号
     */
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    /**
     * 获取机构名称
     *
     * @return ORG_NAME - 机构名称
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置机构名称
     *
     * @param orgName 机构名称
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取机构描述
     *
     * @return ORG_DESC - 机构描述
     */
    public String getOrgDesc() {
        return orgDesc;
    }

    /**
     * 设置机构描述
     *
     * @param orgDesc 机构描述
     */
    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    /**
     * 获取地址
     *
     * @return ADDRESS - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取联系人
     *
     * @return CONTACTS - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * 获取手机号
     *
     * @return MOBILE_NO - 手机号
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * 设置手机号
     *
     * @param mobileNo 手机号
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * 获取CUPD:银数机构；BANK：银行机构
     *
     * @return ORG_TYPE - CUPD:银数机构；BANK：银行机构
     */
    public String getOrgType() {
        return orgType;
    }

    /**
     * 设置CUPD:银数机构；BANK：银行机构
     *
     * @param orgType CUPD:银数机构；BANK：银行机构
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * 获取只有当ORG_TYPE为BANK时，此字段非空表示该机构所属的银行
     *
     * @return BANK_CODE - 只有当ORG_TYPE为BANK时，此字段非空表示该机构所属的银行
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 设置只有当ORG_TYPE为BANK时，此字段非空表示该机构所属的银行
     *
     * @param bankCode 只有当ORG_TYPE为BANK时，此字段非空表示该机构所属的银行
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * 获取机构公钥
     *
     * @return ORG_PUB_KEY - 机构公钥
     */
    public String getOrgPubKey() {
        return orgPubKey;
    }

    /**
     * 设置机构公钥
     *
     * @param orgPubKey 机构公钥
     */
    public void setOrgPubKey(String orgPubKey) {
        this.orgPubKey = orgPubKey;
    }

    /**
     * 获取平台公钥
     *
     * @return SIP_PUB_KEY - 平台公钥
     */
    public String getSipPubKey() {
        return sipPubKey;
    }

    /**
     * 设置平台公钥
     *
     * @param sipPubKey 平台公钥
     */
    public void setSipPubKey(String sipPubKey) {
        this.sipPubKey = sipPubKey;
    }

    /**
     * 获取平台私钥
     *
     * @return SIP_PRI_KEY - 平台私钥
     */
    public String getSipPriKey() {
        return sipPriKey;
    }

    /**
     * 设置平台私钥
     *
     * @param sipPriKey 平台私钥
     */
    public void setSipPriKey(String sipPriKey) {
        this.sipPriKey = sipPriKey;
    }

    /**
     * 获取IP白名单
     *
     * @return WHITE_IPS - IP白名单
     */
    public String getWhiteIps() {
        return whiteIps;
    }

    /**
     * 设置IP白名单
     *
     * @param whiteIps IP白名单
     */
    public void setWhiteIps(String whiteIps) {
        this.whiteIps = whiteIps;
    }

    /**
     * 获取0：无效；1：有效。
     *
     * @return VALID_STATUS - 0：无效；1：有效。
     */
    public String getValidStatus() {
        return validStatus;
    }

    /**
     * 设置0：无效；1：有效。
     *
     * @param validStatus 0：无效；1：有效。
     */
    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    /**
     * 获取内容引入机构退款地址
     *
     * @return CONTENT_ORDER_REFUND_URL - 内容引入机构退款地址
     */
    public String getContentOrderRefundUrl() {
        return contentOrderRefundUrl;
    }

    /**
     * 设置内容引入机构退款地址
     *
     * @param contentOrderRefundUrl 内容引入机构退款地址
     */
    public void setContentOrderRefundUrl(String contentOrderRefundUrl) {
        this.contentOrderRefundUrl = contentOrderRefundUrl;
    }

    /**
     * 获取内容引入机构订单查询地址
     *
     * @return CONTENT_ORDER_QUERY_URL - 内容引入机构订单查询地址
     */
    public String getContentOrderQueryUrl() {
        return contentOrderQueryUrl;
    }

    /**
     * 设置内容引入机构订单查询地址
     *
     * @param contentOrderQueryUrl 内容引入机构订单查询地址
     */
    public void setContentOrderQueryUrl(String contentOrderQueryUrl) {
        this.contentOrderQueryUrl = contentOrderQueryUrl;
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
}