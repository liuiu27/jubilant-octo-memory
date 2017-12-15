package com.cupdata.commapi.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
* @ClassName: BaseModel 
* @Description: 基础模型，定义了主键ID，创建时间，更新时间
* @author LinYong 
* @date 2016年8月6日 下午4:11:07 
*
 */
public abstract class BaseModel implements Serializable{

	/**
	 * 主键ID
	 */
	protected Long id;

	/**
     * 创建者
	 */
	private String createBy;

	/**
	 * 创建日期
	 */
    private Date createDate;

	/**
	 * 更新者
	 */
	private String updateBy;

    /**
     * 更新日期
     */
    private Timestamp updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
}
