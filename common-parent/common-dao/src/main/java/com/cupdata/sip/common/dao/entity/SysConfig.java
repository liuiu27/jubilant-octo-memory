package com.cupdata.sip.common.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_config")
public class SysConfig implements Serializable {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "PARA_NAME_EN")
    private String paraNameEn;

    @Column(name = "PARA_NAME_CN")
    private String paraNameCn;

    @Column(name = "PARA_VALUE")
    private String paraValue;

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
     * 更新时间
     */
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
     * @return BANK_CODE
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * @return PARA_NAME_EN
     */
    public String getParaNameEn() {
        return paraNameEn;
    }

    /**
     * @param paraNameEn
     */
    public void setParaNameEn(String paraNameEn) {
        this.paraNameEn = paraNameEn;
    }

    /**
     * @return PARA_NAME_CN
     */
    public String getParaNameCn() {
        return paraNameCn;
    }

    /**
     * @param paraNameCn
     */
    public void setParaNameCn(String paraNameCn) {
        this.paraNameCn = paraNameCn;
    }

    /**
     * @return PARA_VALUE
     */
    public String getParaValue() {
        return paraValue;
    }

    /**
     * @param paraValue
     */
    public void setParaValue(String paraValue) {
        this.paraValue = paraValue;
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
     * 获取更新时间
     *
     * @return UPDATE_DATE - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}