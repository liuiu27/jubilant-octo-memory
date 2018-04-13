/**   
* @Title: LakalaVoucherReq.java 
* @Package com.cupdata.pointpay.wx.vo.lakala 
* @author LinYong  
* @date 2017年11月15日 下午4:01:42 
* @version V1.0   
*/
package com.cupdata.sip.common.api.lakala.request;

/** 
 * @ClassName: LakalaVoucherReq 
 * @Description: 拉卡拉发券接口请求参数
 * @author LinYong 
 * @date 2017年11月15日 下午4:01:42 
 *  
 */
public class LakalaVoucherReq {
	/**
	 * 合作商户商户
	 */
	private String partner_id;
	
	/**
	 * 接口版本号
	 */
	private String version;
	
	/**
	 * 时间戳
	 */
	private String sign_generate_time;
	
	/**
	 * 合作方不可重复的订单号
	 */
	private String order_id;
	
	/**
	 * 需要发券的券标示（需由积分购配置）
	 */
	private String item_id;
	
	/**
	 * 需要发券的用户手机号
	 */
	private String phone;
	
	/**
	 * 订单描述
	 */
	private String title;
	
	/**
	 * 签名
	 */
	private String sign;

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign_generate_time() {
		return sign_generate_time;
	}

	public void setSign_generate_time(String sign_generate_time) {
		this.sign_generate_time = sign_generate_time;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
