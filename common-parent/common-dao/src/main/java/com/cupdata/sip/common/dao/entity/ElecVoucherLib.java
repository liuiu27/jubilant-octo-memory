package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "elec_voucher_lib")
public class ElecVoucherLib implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 导入批次ID
     */
    @Column(name = "BATCH_ID")
    private Long batchId;

    /**
     * 供应商ID
     */
    @Column(name = "SUPPLYER_ID")
    private Long supplyerId;

    /**
     * 券码分类ID
     */
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    /**
     * 可发放机构编号
     */
    @Column(name = "ORG_NOS")
    private String orgNos;

    /**
     * 券码号
     */
    @Column(name = "TICKET_NO")
    private String ticketNo;

    /**
     * 有效起始时间
     */
    @Column(name = "START_DATE")
    private String startDate;

    /**
     * 有效结束时间
     */
    @Column(name = "END_DATE")
    private String endDate;

    /**
     * 券码发送状态
     */
    @Column(name = "SEND_STATUS")
    private String sendStatus;

    /**
     * 最终发放机构
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 机构订单编号
     */
    @Column(name = "ORG_ORDER_NO")
    private String orgOrderNo;

    /**
     * 手机号码
     */
    @Column(name = "MOBIL_NO")
    private String mobilNo;

    /**
     * 发券时间
     */
    @Column(name = "SEND_DATE")
    private Date sendDate;

    /**
     * 回收时间
     */
    @Column(name = "RECOVERY_DATE")
    private Date recoveryDate;

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
     * 获取导入批次ID
     *
     * @return BATCH_ID - 导入批次ID
     */
    public Long getBatchId() {
        return batchId;
    }

    /**
     * 设置导入批次ID
     *
     * @param batchId 导入批次ID
     */
    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    /**
     * 获取供应商ID
     *
     * @return SUPPLYER_ID - 供应商ID
     */
    public Long getSupplyerId() {
        return supplyerId;
    }

    /**
     * 设置供应商ID
     *
     * @param supplyerId 供应商ID
     */
    public void setSupplyerId(Long supplyerId) {
        this.supplyerId = supplyerId;
    }

    /**
     * 获取券码分类ID
     *
     * @return CATEGORY_ID - 券码分类ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置券码分类ID
     *
     * @param categoryId 券码分类ID
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取可发放机构编号
     *
     * @return ORG_NOS - 可发放机构编号
     */
    public String getOrgNos() {
        return orgNos;
    }

    /**
     * 设置可发放机构编号
     *
     * @param orgNos 可发放机构编号
     */
    public void setOrgNos(String orgNos) {
        this.orgNos = orgNos;
    }

    /**
     * 获取券码号
     *
     * @return TICKET_NO - 券码号
     */
    public String getTicketNo() {
        return ticketNo;
    }

    /**
     * 设置券码号
     *
     * @param ticketNo 券码号
     */
    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    /**
     * 获取有效起始时间
     *
     * @return START_DATE - 有效起始时间
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 设置有效起始时间
     *
     * @param startDate 有效起始时间
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取有效结束时间
     *
     * @return END_DATE - 有效结束时间
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 设置有效结束时间
     *
     * @param endDate 有效结束时间
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取券码发送状态
     *
     * @return SEND_STATUS - 券码发送状态
     */
    public String getSendStatus() {
        return sendStatus;
    }

    /**
     * 设置券码发送状态
     *
     * @param sendStatus 券码发送状态
     */
    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    /**
     * 获取最终发放机构
     *
     * @return ORG_NO - 最终发放机构
     */
    public String getOrgNo() {
        return orgNo;
    }

    /**
     * 设置最终发放机构
     *
     * @param orgNo 最终发放机构
     */
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    /**
     * 获取机构订单编号
     *
     * @return ORG_ORDER_NO - 机构订单编号
     */
    public String getOrgOrderNo() {
        return orgOrderNo;
    }

    /**
     * 设置机构订单编号
     *
     * @param orgOrderNo 机构订单编号
     */
    public void setOrgOrderNo(String orgOrderNo) {
        this.orgOrderNo = orgOrderNo;
    }

    /**
     * 获取手机号码
     *
     * @return MOBIL_NO - 手机号码
     */
    public String getMobilNo() {
        return mobilNo;
    }

    /**
     * 设置手机号码
     *
     * @param mobilNo 手机号码
     */
    public void setMobilNo(String mobilNo) {
        this.mobilNo = mobilNo;
    }

    /**
     * 获取发券时间
     *
     * @return SEND_DATE - 发券时间
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * 设置发券时间
     *
     * @param sendDate 发券时间
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * 获取回收时间
     *
     * @return RECOVERY_DATE - 回收时间
     */
    public Date getRecoveryDate() {
        return recoveryDate;
    }

    /**
     * 设置回收时间
     *
     * @param recoveryDate 回收时间
     */
    public void setRecoveryDate(Date recoveryDate) {
        this.recoveryDate = recoveryDate;
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