package com.cupdata.tencent.vo;

/**
 * 
* @ClassName: QQCheckOpenReq 
* @Description: QQ会员充值鉴权接口请求参数
* @author LinYong 
* @date 2017年6月29日 上午10:46:17 
*
 */
public class QQCheckOpenReq {
	/**
	 * 手机号码
	 */
	private String servernum;
	
	/**
	 * 腾讯产品ID
	 */
	private String serviceid;
	
	/**
	 * 用户QQ号
	 */
	private String uin;
	
	/**
	 * 开通服务时长
	 * 单位为月： 1表示开通1个月服务
	 */
	private String amount;
	
	/**
	 * 渠道来源
	 * 1：PC；2：wap专页；3：短厅
	 */
	private String source;
	
	/**
	 * 支付类型
	 * 1： 积分；2： 电子券；3：话费；4：现金
	 */
	private String paytype;

	/**
	 * 签名
	 */
	private String sign;
	
	public String getServernum() {
		return servernum;
	}

	public void setServernum(String servernum) {
		this.servernum = servernum;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}