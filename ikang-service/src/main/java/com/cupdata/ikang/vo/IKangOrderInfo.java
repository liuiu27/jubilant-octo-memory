package com.cupdata.ikang.vo;

public class IKangOrderInfo {

	private String cardnumber;//本地没有爱康卡号，所以默认为null
	private String regdate;//默认为当前日期
	private String packagecode;//
	private String hospid;
	private String name;
	private String sex;//0为女1为男
	private String married;//0为未婚1为已婚
	private String contacttel;
	private String idnumber;
	private String reportaddress;
	private String thirdnum;//本地订单号
	private String note;
	private String bankCode;
	private Long userid;
	private String purseId;
	private Double supplyerCostPrice;
	private Double cupdCostPrice;
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getPackagecode() {
		return packagecode;
	}
	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}
	public String getHospid() {
		return hospid;
	}
	public void setHospid(String hospid) {
		this.hospid = hospid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getContacttel() {
		return contacttel;
	}
	public void setContacttel(String contacttel) {
		this.contacttel = contacttel;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getReportaddress() {
		return reportaddress;
	}
	public void setReportaddress(String reportaddress) {
		this.reportaddress = reportaddress;
	}
	public String getThirdnum() {
		return thirdnum;
	}
	public void setThirdnum(String thirdnum) {
		this.thirdnum = thirdnum;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getPurseId() {
		return purseId;
	}
	public void setPurseId(String purseId) {
		this.purseId = purseId;
	}
	public Double getSupplyerCostPrice() {
		return supplyerCostPrice;
	}
	public void setSupplyerCostPrice(Double supplyerCostPrice) {
		this.supplyerCostPrice = supplyerCostPrice;
	}
	public Double getCupdCostPrice() {
		return cupdCostPrice;
	}
	public void setCupdCostPrice(Double cupdCostPrice) {
		this.cupdCostPrice = cupdCostPrice;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
