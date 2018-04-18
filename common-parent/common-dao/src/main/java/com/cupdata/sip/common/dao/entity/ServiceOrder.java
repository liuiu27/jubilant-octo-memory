package com.cupdata.sip.common.dao.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "service_order")
public class ServiceOrder implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "ID")
    private Long id;

    /**
     * 机构号
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 服务供应商编号
     */
    @Column(name = "SUPPLIER_NO")
    private String supplierNo;

    /**
     * 订单编号
     */
    @Column(name = "ORDER_NO")
    private String orderNo;

    /**
     * 机构订单号
     */
    @Column(name = "ORG_ORDER_NO")
    private String orgOrderNo;

    /**
     * 供应商订单号
     */
    @Column(name = "SUPPLIER_ORDER_NO")
    private String supplierOrderNo;

    /**
     * 机构价格
     */
    @Column(name = "ORG_PRICE")
    private Integer orgPrice;

    /**
     * 服务供应商价格
     */
    @Column(name = "SUPPLIER_PRICE")
    private Integer supplierPrice;

    /**
     * 格式为：yyyyMMdd
     */
    @Column(name = "SETTLE_DATE")
    private String settleDate;

    /**
     * 0：初始化；1：处理中；S：订单成功；F：订单失败；
     */
    @Column(name = "ORDER_STATUS")
    private String orderStatus;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    /**
     * 订单描述
     */
    @Column(name = "ORDER_DESC")
    private String orderDesc;

    /**
     * 用于记录调用供应商接口失败的原因信息
     */
    @Column(name = "ORDER_FAIL_DESC")
    private String orderFailDesc;

    /**
     * 0：不需要；1：需要
     */
    @Column(name = "IS_NOTIFY")
    private String isNotify;

    /**
     * 异步通知给机构订单结果需要调用的URL
     */
    @Column(name = "NOTIFY_URL")
    private String notifyUrl;

    /**
     * 用于记录处理该笔订单所在的服务器节点名称，便于查找问题
     */
    @Column(name = "NODE_NAME")
    private String nodeName;

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

    @Column(name = "ORDER_SUB_TYPE")
    private String orderSubType;

    @Column(name = "SUPPLIER_FLAG")
    private String supplierFlag;

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
     * 获取服务供应商编号
     *
     * @return SUPPLIER_NO - 服务供应商编号
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 设置服务供应商编号
     *
     * @param supplierNo 服务供应商编号
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 获取订单编号
     *
     * @return ORDER_NO - 订单编号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单编号
     *
     * @param orderNo 订单编号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取机构订单号
     *
     * @return ORG_ORDER_NO - 机构订单号
     */
    public String getOrgOrderNo() {
        return orgOrderNo;
    }

    /**
     * 设置机构订单号
     *
     * @param orgOrderNo 机构订单号
     */
    public void setOrgOrderNo(String orgOrderNo) {
        this.orgOrderNo = orgOrderNo;
    }

    /**
     * 获取供应商订单号
     *
     * @return SUPPLIER_ORDER_NO - 供应商订单号
     */
    public String getSupplierOrderNo() {
        return supplierOrderNo;
    }

    /**
     * 设置供应商订单号
     *
     * @param supplierOrderNo 供应商订单号
     */
    public void setSupplierOrderNo(String supplierOrderNo) {
        this.supplierOrderNo = supplierOrderNo;
    }

    /**
     * 获取机构价格
     *
     * @return ORG_PRICE - 机构价格
     */
    public Integer getOrgPrice() {
        return orgPrice;
    }

    /**
     * 设置机构价格
     *
     * @param orgPrice 机构价格
     */
    public void setOrgPrice(Integer orgPrice) {
        this.orgPrice = orgPrice;
    }

    /**
     * 获取服务供应商价格
     *
     * @return SUPPLIER_PRICE - 服务供应商价格
     */
    public Integer getSupplierPrice() {
        return supplierPrice;
    }

    /**
     * 设置服务供应商价格
     *
     * @param supplierPrice 服务供应商价格
     */
    public void setSupplierPrice(Integer supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    /**
     * 获取格式为：yyyyMMdd
     *
     * @return SETTLE_DATE - 格式为：yyyyMMdd
     */
    public String getSettleDate() {
        return settleDate;
    }

    /**
     * 设置格式为：yyyyMMdd
     *
     * @param settleDate 格式为：yyyyMMdd
     */
    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    /**
     * 获取0：初始化；1：处理中；S：订单成功；F：订单失败；
     *
     * @return ORDER_STATUS - 0：初始化；1：处理中；S：订单成功；F：订单失败；
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置0：初始化；1：处理中；S：订单成功；F：订单失败；
     *
     * @param orderStatus 0：初始化；1：处理中；S：订单成功；F：订单失败；
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return ORDER_TYPE
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * @param orderType
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取订单描述
     *
     * @return ORDER_DESC - 订单描述
     */
    public String getOrderDesc() {
        return orderDesc;
    }

    /**
     * 设置订单描述
     *
     * @param orderDesc 订单描述
     */
    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    /**
     * 获取用于记录调用供应商接口失败的原因信息
     *
     * @return ORDER_FAIL_DESC - 用于记录调用供应商接口失败的原因信息
     */
    public String getOrderFailDesc() {
        return orderFailDesc;
    }

    /**
     * 设置用于记录调用供应商接口失败的原因信息
     *
     * @param orderFailDesc 用于记录调用供应商接口失败的原因信息
     */
    public void setOrderFailDesc(String orderFailDesc) {
        this.orderFailDesc = orderFailDesc;
    }

    /**
     * 获取0：不需要；1：需要
     *
     * @return IS_NOTIFY - 0：不需要；1：需要
     */
    public String getIsNotify() {
        return isNotify;
    }

    /**
     * 设置0：不需要；1：需要
     *
     * @param isNotify 0：不需要；1：需要
     */
    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    /**
     * 获取异步通知给机构订单结果需要调用的URL
     *
     * @return NOTIFY_URL - 异步通知给机构订单结果需要调用的URL
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * 设置异步通知给机构订单结果需要调用的URL
     *
     * @param notifyUrl 异步通知给机构订单结果需要调用的URL
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * 获取用于记录处理该笔订单所在的服务器节点名称，便于查找问题
     *
     * @return NODE_NAME - 用于记录处理该笔订单所在的服务器节点名称，便于查找问题
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * 设置用于记录处理该笔订单所在的服务器节点名称，便于查找问题
     *
     * @param nodeName 用于记录处理该笔订单所在的服务器节点名称，便于查找问题
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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
     * @return ORDER_SUB_TYPE
     */
    public String getOrderSubType() {
        return orderSubType;
    }

    /**
     * @param orderSubType
     */
    public void setOrderSubType(String orderSubType) {
        this.orderSubType = orderSubType;
    }

    /**
     * @return SUPPLIER_FLAG
     */
    public String getSupplierFlag() {
        return supplierFlag;
    }

    /**
     * @param supplierFlag
     */
    public void setSupplierFlag(String supplierFlag) {
        this.supplierFlag = supplierFlag;
    }
}