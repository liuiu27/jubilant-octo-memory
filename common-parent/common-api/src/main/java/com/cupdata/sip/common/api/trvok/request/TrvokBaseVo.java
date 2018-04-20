package com.cupdata.sip.common.api.trvok.request;


import lombok.Data;

@Data
public class TrvokBaseVo{
	
 	/**
 	 * 获取空港区域秘钥
 	 */
	private String  areaSignKey;
	

 	/**
 	 * 空港请求区域信息URL
 	 */
	private String requstUrl;
	

 	/**
 	 * 图片路径
 	 */
	private String imgUrl;
	

 	/**
 	 * 合作方ID
 	 */
	private String partner;
	

 	/**
 	 * WC秘钥
 	 */
	private String wcfSignKey;
}
