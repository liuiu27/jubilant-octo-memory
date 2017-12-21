package com.cupdata.commons.vo.trvok;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;

@Data
public class TrovkActivatReq extends BaseRequest{
	/**
	 * 券码
	 */
	private String verifyCode;
	/**
	 * 有效期 YYYY-MM-DD
	 */
	private String expire;
}
