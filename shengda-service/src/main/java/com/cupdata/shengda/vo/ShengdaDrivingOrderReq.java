package com.cupdata.shengda.vo;


import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
* @ClassName: ShengdaDrivingOrderReq 
* @Description: 盛大酒后代驾/道路救援权益下单请求参数Vo
* @author LinYong 
* @date 2016-6-17 下午03:56:35 
*
 */
public class ShengdaDrivingOrderReq extends ShengdaOrderReq{
	/**
	 * 预约类型
	 */
	private String type;
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 用户姓名
	 */
	private String realName;
	
	/**
	 * 预约手机号
	 */
	private String telPhone;
	
	/**
	 * 出发地
	 * [例如：上海市宝山区灵石路1001号]
	 */
	private String startAddress;
	
	/**
	 * 目的地
	 * [例如：上海市浦东区杜鹃路100号]
	 */
	private String endAddress;
	
	/**
	 * 预约时间
	 * [格式为yyyy-MM-dd HH:mm:ss]
	 */
	private String bespeakTime;
	
	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 代驾人数
	 * 酒后代驾（代驾人数1-10）
	 */
	private String remark;
	
	public ShengdaDrivingOrderReq(){
		
	}
	
	/**
	 * 有参构造器
	 * @param source
	 * @param order
	 * @param carType
	 * @param userInfo
	 */
	public ShengdaDrivingOrderReq(String source, String orgSource, String type, String orderId, 
			String realName, String telPhone, String startAddress, String endAddress, String userName, String remark){
		setSource(source);
		setOrgSource(orgSource);
		this.type = type;
		this.orderId = orderId;
		this.realName = realName;//用户姓名
		this.telPhone = telPhone;//手机号
		this.startAddress = startAddress;//出发地
		this.endAddress = endAddress;//目的地
		this.bespeakTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//预约时间
		this.userName = userName;
		this.remark = remark;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public String getBespeakTime() {
		return bespeakTime;
	}

	public void setBespeakTime(String bespeakTime) {
		this.bespeakTime = bespeakTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
