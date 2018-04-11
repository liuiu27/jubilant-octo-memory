package com.cupdata.ikang.vo;


public class CheckDataBackInfo {
	public Long id; // 主键ID
	public String cardnumber; // 爱康卡号
	public Long regdate; // 到检日期
	public String name; // 姓名
	public Long sex; // 性别，0为女，1为男，其它为未知
	public Long married; // 婚否，0为未婚，1为已婚，其它为未知
	public String packagecode; // 套餐CODE
	public String packagename; // 套餐名称
	public String hospid; // 医院ID
	public String idcard; // 身份证号码
	public String contacttel; // 联系方式
	public String workno; // 健检号
	public Long orderid; // 预约单号
	public Long reportstatus; // 报告状态， 1为已生成，其它为未生成
	public String projectid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public Long getRegdate() {
		return regdate;
	}
	public void setRegdate(Long regdate) {
		this.regdate = regdate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSex() {
		return sex;
	}
	public void setSex(Long sex) {
		this.sex = sex;
	}
	public Long getMarried() {
		return married;
	}
	public void setMarried(Long married) {
		this.married = married;
	}
	public String getPackagecode() {
		return packagecode;
	}
	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getHospid() {
		return hospid;
	}
	public void setHospid(String hospid) {
		this.hospid = hospid;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getContacttel() {
		return contacttel;
	}
	public void setContacttel(String contacttel) {
		this.contacttel = contacttel;
	}
	public String getWorkno() {
		return workno;
	}
	public void setWorkno(String workno) {
		this.workno = workno;
	}
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	public Long getReportstatus() {
		return reportstatus;
	}
	public void setReportstatus(Long reportstatus) {
		this.reportstatus = reportstatus;
	}
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	
}
