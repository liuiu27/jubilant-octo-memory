package com.cupdata.shengda.vo;

/**
 * 
* @ClassName: ShengdaDrivingOrderReq 
* @Description: 盛大洗车权益下单请求参数Vo
* @author LinYong 
* @date 2016-6-17 下午03:56:35 
*
 */
public class ShengdaWashingOrderReq extends ShengdaOrderReq{
	/**
	 * 订单号
	 */
	private String order;
	
	/**
	 * 随机码
	 */
	private String randStr;
	
	/**
	 * 大小车类型
	 * 小车：01;
	 * 大车：02。
	 */
	private String carType;

	/**
	 * 用户信息标识
	 * 以用户信息（手机号）作为此次下单的订单的标识，不可与订单号同时为空
	 */
	private String userInfo;
	
	/**
	 * 订单标识类型
	 * order表示该订单以订单号作为唯一标识；
	 * telphone表示该订单以用户信息作为唯一标识。
	 */
	private String userOrderType;
	
	/**
	 * 订单生成规则
	 * 01：直接以该订单的指定标识作为唯一标识，直接创建订单。
	 * 02：以该订单的指定标识进行校验，如果该订单已存在则不创建新订单，若校验该订单不存在则创建新订单
	 */
	private String generationRule;
	
	/**
	 * 字段1
	 */
	private String field1;
	
	/**
	 * 字段2
	 */
	private String field2;
	
	/**
	 * 字段3
	 */
	private String field3;
	
	/**
	 * 字段4
	 */
	private String field4;
	
	/**
	 * 字段5
	 */
	private String field5;
	
	public ShengdaWashingOrderReq(){
		
	}
	
	/**
	 * 有参构造器
	 * @param source
	 * @param order
	 * @param carType
	 * @param userInfo
	 */
	public ShengdaWashingOrderReq(String source, String orgSource, String order, String carType, String userInfo){
		setSource(source);
		setOrgSource(orgSource);
		this.order = order;
		this.randStr = "666";//TODO 随机值
		this.carType = carType;
		this.userInfo = userInfo;
		this.userOrderType = "order";
		this.generationRule = "01";
		this.field1 = "";
		this.field2 = "";
		this.field3 = "";
		this.field4 = "";
		this.field5 = "";
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getRandStr() {
		return randStr;
	}

	public void setRandStr(String randStr) {
		this.randStr = randStr;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getUserOrderType() {
		return userOrderType;
	}

	public void setUserOrderType(String userOrderType) {
		this.userOrderType = userOrderType;
	}

	public String getGenerationRule() {
		return generationRule;
	}

	public void setGenerationRule(String generationRule) {
		this.generationRule = generationRule;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}
}
