package com.cupdata.tencent.vo;

/**
 * 
 * @ClassName: QQOpenReq
 * @Description: QQ会员开通请求参数类
 * @author LinYong
 * @date 2017年6月29日 上午11:37:50
 *
 */
public class QQOpenReq {
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
	 * 腾讯自定义字段
	 * 当错误码为0时有值，在开通请求消息中需要透传字段。
	 */
	private String txparam;
	
	/**
	 * 扣费金额
	 * 单位为分；只在开通时有效
	 */
	private String price;
	
	/**
	 * 命令字
	 * 1：开通 
	 */
	private String command;
	
	/**
	 * 时间戳
	 * 格式：YYYYMMDDHHMMSS
	 */
	private String timestamp;
	
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
	 * MD5(servernum+serviceid+uin+amount+txparam+price+command+timestamp+source+paytype+key) 
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

	public String getTxparam() {
		return txparam;
	}

	public void setTxparam(String txparam) {
		this.txparam = txparam;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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
