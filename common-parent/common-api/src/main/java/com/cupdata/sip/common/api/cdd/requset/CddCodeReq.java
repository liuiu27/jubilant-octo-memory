package com.cupdata.sip.common.api.cdd.requset;


/**
 * @author liwei
 * @date   2018/1/29
 */
 
import lombok.Data;

@Data
public class CddCodeReq{
	/**
	 * 车点点平台分配给第三方商户的身份标识
	 */
	private String  apiKey; 
	
	/**
	 * 将“手机号”做 Aes加密Aes加密Key请使用 ApiSecret 
	 */
	private String mobile;
	
	/**
	 * 10位时间戳（20分钟内有效）
	 */
	private String  apiST;
	
	/**
	 * 签名MD5(ApiKey + ApiSecret  + ApiST)
	 */
	private String  apiSign;
	
	/**
	 * 订单号，作为唯一标识，50位以内
	 */
	private String sn;
	
	/**
	 * 礼包ID(双方约定好，由车点点提供)
	 */
	private String packageID;
	
	/**
	 * 城市机构代码
	 */
	private String openCode;
	
	/**
	 * 礼包申请数量，固定填1
	 */
	private String num;
	
	/**
	 * 机构ID，由车点点平台分配给第三方商户
	 */
	private String agencyID;
	
	/**
	 * 默认传0
	 */
	private String  orderType;
}
