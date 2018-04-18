package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "service_order_voucher")
public class ServiceOrderVoucher implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 主订单ID
     */
    @Column(name = "ORDER_ID")
    private Long orderId;

    /**
     * 服务产品编号
     */
    @Column(name = "PRODUCT_NO")
    private String productNo;

    /**
     * 卡号/券码号
     */
    @Column(name = "VOUCHER_CODE")
    private String voucherCode;

    /**
     * 卡密
     */
    @Column(name = "VOUCHER_PASSWORD")
    private String voucherPassword;

    /**
     * 此字段用于保存卡号的二维码链接
     */
    @Column(name = "QR_CODE_URL")
    private String qrCodeUrl;

    /**
     * 使用者姓名
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * 使用者手机
     */
    @Column(name = "USER_MOBILE_NO")
    private String userMobileNo;

    /**
     * 使用时间
     */
    @Column(name = "USE_TIME")
    private Date useTime;

    /**
     * 使用地点
     */
    @Column(name = "USE_PLACE")
    private String usePlace;

    /**
     * 0:未使用 1:已使用
     */
    @Column(name = "USE_STATUS")
    private String useStatus;

    /**
     * 0:有效  1:禁用
     */
    @Column(name = "EFF_STATUS")
    private String effStatus;

    /**
     * 有效开始时间 格式：yyyyMMdd
     */
    @Column(name = "START_DATE")
    private String startDate;

    /**
     * 有效结束时间 格式：yyyyMMdd
     */
    @Column(name = "END_DATE")
    private String endDate;

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
     * 获取主订单ID
     *
     * @return ORDER_ID - 主订单ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置主订单ID
     *
     * @param orderId 主订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
     * 获取卡号/券码号
     *
     * @return VOUCHER_CODE - 卡号/券码号
     */
    public String getVoucherCode() {
        return voucherCode;
    }

    /**
     * 设置卡号/券码号
     *
     * @param voucherCode 卡号/券码号
     */
    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    /**
     * 获取卡密
     *
     * @return VOUCHER_PASSWORD - 卡密
     */
    public String getVoucherPassword() {
        return voucherPassword;
    }

    /**
     * 设置卡密
     *
     * @param voucherPassword 卡密
     */
    public void setVoucherPassword(String voucherPassword) {
        this.voucherPassword = voucherPassword;
    }

    /**
     * 获取此字段用于保存卡号的二维码链接
     *
     * @return QR_CODE_URL - 此字段用于保存卡号的二维码链接
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * 设置此字段用于保存卡号的二维码链接
     *
     * @param qrCodeUrl 此字段用于保存卡号的二维码链接
     */
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    /**
     * 获取使用者姓名
     *
     * @return USER_NAME - 使用者姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置使用者姓名
     *
     * @param userName 使用者姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取使用者手机
     *
     * @return USER_MOBILE_NO - 使用者手机
     */
    public String getUserMobileNo() {
        return userMobileNo;
    }

    /**
     * 设置使用者手机
     *
     * @param userMobileNo 使用者手机
     */
    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    /**
     * 获取使用时间
     *
     * @return USE_TIME - 使用时间
     */
    public Date getUseTime() {
        return useTime;
    }

    /**
     * 设置使用时间
     *
     * @param useTime 使用时间
     */
    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    /**
     * 获取使用地点
     *
     * @return USE_PLACE - 使用地点
     */
    public String getUsePlace() {
        return usePlace;
    }

    /**
     * 设置使用地点
     *
     * @param usePlace 使用地点
     */
    public void setUsePlace(String usePlace) {
        this.usePlace = usePlace;
    }

    /**
     * 获取0:未使用 1:已使用
     *
     * @return USE_STATUS - 0:未使用 1:已使用
     */
    public String getUseStatus() {
        return useStatus;
    }

    /**
     * 设置0:未使用 1:已使用
     *
     * @param useStatus 0:未使用 1:已使用
     */
    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    /**
     * 获取0:有效  1:禁用
     *
     * @return EFF_STATUS - 0:有效  1:禁用
     */
    public String getEffStatus() {
        return effStatus;
    }

    /**
     * 设置0:有效  1:禁用
     *
     * @param effStatus 0:有效  1:禁用
     */
    public void setEffStatus(String effStatus) {
        this.effStatus = effStatus;
    }

    /**
     * 获取有效开始时间 格式：yyyyMMdd
     *
     * @return START_DATE - 有效开始时间 格式：yyyyMMdd
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 设置有效开始时间 格式：yyyyMMdd
     *
     * @param startDate 有效开始时间 格式：yyyyMMdd
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取有效结束时间 格式：yyyyMMdd
     *
     * @return END_DATE - 有效结束时间 格式：yyyyMMdd
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 设置有效结束时间 格式：yyyyMMdd
     *
     * @param endDate 有效结束时间 格式：yyyyMMdd
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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