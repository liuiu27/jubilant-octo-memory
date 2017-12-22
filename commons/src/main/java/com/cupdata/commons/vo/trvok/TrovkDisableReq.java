package com.cupdata.commons.vo.trvok;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;

@Data
public class TrovkDisableReq extends BaseRequest{
	/**
	 * 券码
	 */
	private String verifyCode;
}
