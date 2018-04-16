package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "elec_voucher_category")
public class ElecVoucherCategory implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 供应商ID
     */
    @Column(name = "SUPPLIER_ID")
    private Long supplierId;

    /**
     * 券码类别名称
     */
    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    /**
     * 券码状态
     */
    @Column(name = "VALID_STATUS")
    private String validStatus;

    /**
     * 库存量
     */
    @Column(name = "STOCK_WARNING")
    private String stockWarning;

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
     * 获取供应商ID
     *
     * @return SUPPLIER_ID - 供应商ID
     */
    public Long getSupplierId() {
        return supplierId;
    }

    /**
     * 设置供应商ID
     *
     * @param supplierId 供应商ID
     */
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * 获取券码类别名称
     *
     * @return CATEGORY_NAME - 券码类别名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置券码类别名称
     *
     * @param categoryName 券码类别名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取券码状态
     *
     * @return VALID_STATUS - 券码状态
     */
    public String getValidStatus() {
        return validStatus;
    }

    /**
     * 设置券码状态
     *
     * @param validStatus 券码状态
     */
    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    /**
     * 获取库存量
     *
     * @return STOCK_WARNING - 库存量
     */
    public String getStockWarning() {
        return stockWarning;
    }

    /**
     * 设置库存量
     *
     * @param stockWarning 库存量
     */
    public void setStockWarning(String stockWarning) {
        this.stockWarning = stockWarning;
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