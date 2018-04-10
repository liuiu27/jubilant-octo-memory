package com.cupdata.sip.common.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class Test implements Serializable {

    @Id
    private Integer id;

    @Column(name = "createBy")
    private String createby;

    @Column(name = "createDate")
    private Date createdate;

    @Column(name = "updateBy")
    private String updateby;

    @Column(name = "updateDate")
    private Date updatedate;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return createBy
     */
    public String getCreateby() {
        return createby;
    }

    /**
     * @param createby
     */
    public void setCreateby(String createby) {
        this.createby = createby;
    }

    /**
     * @return createDate
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * @return updateBy
     */
    public String getUpdateby() {
        return updateby;
    }

    /**
     * @param updateby
     */
    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    /**
     * @return updateDate
     */
    public Date getUpdatedate() {
        return updatedate;
    }

    /**
     * @param updatedate
     */
    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }
}