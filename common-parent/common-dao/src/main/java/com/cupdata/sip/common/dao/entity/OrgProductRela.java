package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "org_product_rela")
public class OrgProductRela implements Serializable {
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
     * 服务产品编号
     */
    @Column(name = "PRODUCT_NO")
    private String productNo;

    /**
     * 单位：人民币-分
     */
    @Column(name = "ORG_PRICE")
    private Integer orgPrice;

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
     * 获取单位：人民币-分
     *
     * @return ORG_PRICE - 单位：人民币-分
     */
    public Integer getOrgPrice() {
        return orgPrice;
    }

    /**
     * 设置单位：人民币-分
     *
     * @param orgPrice 单位：人民币-分
     */
    public void setOrgPrice(Integer orgPrice) {
        this.orgPrice = orgPrice;
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