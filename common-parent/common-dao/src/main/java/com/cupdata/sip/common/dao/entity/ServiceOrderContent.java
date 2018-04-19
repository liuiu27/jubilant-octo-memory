package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "service_order_content")
public class ServiceOrderContent implements Serializable {
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 主订单id
     */
    @Column(name = "ORDER_ID")
    private Long orderId;

    /**
     * 产品编号
     */
    @Column(name = "PRODUCT_NO")
    private String productNo;

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
     * 手机号
     */
    @Column(name = "MOBILE_NO")
    private String mobileNo;

    /**
     * 渠道用户id
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 用户名
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * 订单时间
     */
    @Column(name = "ORDER_TIME")
    private String orderTime;

    /**
     * 订单标题
     */
    @Column(name = "ORDER_TITLE")
    private String orderTitle;

    /**
     * 订单详细
     */
    @Column(name = "ORDER_INFO")
    private String orderInfo;

    /**
     * 订单金额
     */
    @Column(name = "ORDER_AMT")
    private Integer orderAmt;

    /**
     * 订单积分
     */
    @Column(name = "ORDER_BONUS")
    private Integer orderBonus;

    /**
     * 实际支付积分
     */
    @Column(name = "PAY_AMT")
    private Integer payAmt;

    /**
     * 实际支付积分
     */
    @Column(name = "PAY_BONUS")
    private Integer payBonus;

    /**
     * 实际支付权益
     */
    @Column(name = "PAY_RIGHT")
    private Integer payRight;

    /**
     * 商品数量
     */
    @Column(name = "PRODUCT_NUM")
    private Integer productNum;

    /**
     * 和供应商协商组装好字符串用于订单里面显示信息，每个值用#,#隔开
     */
    @Column(name = "ORDER_SHOW")
    private String orderShow;

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
     * 获取主订单id
     *
     * @return ORDER_ID - 主订单id
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置主订单id
     *
     * @param orderId 主订单id
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取产品编号
     *
     * @return PRODUCT_NO - 产品编号
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * 设置产品编号
     *
     * @param productNo 产品编号
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
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
     * 获取渠道用户id
     *
     * @return USER_ID - 渠道用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置渠道用户id
     *
     * @param userId 渠道用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名
     *
     * @return USER_NAME - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取订单时间
     *
     * @return ORDER_TIME - 订单时间
     */
    public String getOrderTime() {
        return orderTime;
    }

    /**
     * 设置订单时间
     *
     * @param orderTime 订单时间
     */
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * 获取订单标题
     *
     * @return ORDER_TITLE - 订单标题
     */
    public String getOrderTitle() {
        return orderTitle;
    }

    /**
     * 设置订单标题
     *
     * @param orderTitle 订单标题
     */
    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    /**
     * 获取订单详细
     *
     * @return ORDER_INFO - 订单详细
     */
    public String getOrderInfo() {
        return orderInfo;
    }

    /**
     * 设置订单详细
     *
     * @param orderInfo 订单详细
     */
    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }


    /**
     * 获取订单金额
     *
     * @return ORDER_AMT - 订单金额
     */
    public Integer getOrderAmt() {
        return orderAmt;
    }

    /**
     * 设置订单金额
     *
     * @param orderAmt 订单金额
     */
    public void setOrderAmt(Integer orderAmt) {
        this.orderAmt = orderAmt;
    }

    /**
     * 获取订单积分
     *
     * @return ORDER_BONUS - 订单积分
     */
    public Integer getOrderBonus() {
        return orderBonus;
    }

    /**
     * 设置订单积分
     *
     * @param orderBonus 订单积分
     */
    public void setOrderBonus(Integer orderBonus) {
        this.orderBonus = orderBonus;
    }

    /**
     * 获取实际支付积分
     *
     * @return PAY_AMT - 实际支付积分
     */
    public Integer getPayAmt() {
        return payAmt;
    }

    /**
     * 设置实际支付积分
     *
     * @param payAmt 实际支付积分
     */
    public void setPayAmt(Integer payAmt) {
        this.payAmt = payAmt;
    }

    /**
     * 获取实际支付积分
     *
     * @return PAY_BONUS - 实际支付积分
     */
    public Integer getPayBonus() {
        return payBonus;
    }

    /**
     * 设置实际支付积分
     *
     * @param payBonus 实际支付积分
     */
    public void setPayBonus(Integer payBonus) {
        this.payBonus = payBonus;
    }

    /**
     * 获取实际支付权益
     *
     * @return PAY_RIGHT - 实际支付权益
     */
    public Integer getPayRight() {
        return payRight;
    }

    /**
     * 设置实际支付权益
     *
     * @param payRight 实际支付权益
     */
    public void setPayRight(Integer payRight) {
        this.payRight = payRight;
    }

    /**
     * 获取商品数量
     *
     * @return PRODUCT_NUM - 商品数量
     */
    public Integer getProductNum() {
        return productNum;
    }

    /**
     * 设置商品数量
     *
     * @param productNum 商品数量
     */
    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    /**
     * 获取和供应商协商组装好字符串用于订单里面显示信息，每个值用#,#隔开
     *
     * @return ORDER_SHOW - 和供应商协商组装好字符串用于订单里面显示信息，每个值用#,#隔开
     */
    public String getOrderShow() {
        return orderShow;
    }

    /**
     * 设置和供应商协商组装好字符串用于订单里面显示信息，每个值用#,#隔开
     *
     * @param orderShow 和供应商协商组装好字符串用于订单里面显示信息，每个值用#,#隔开
     */
    public void setOrderShow(String orderShow) {
        this.orderShow = orderShow;
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