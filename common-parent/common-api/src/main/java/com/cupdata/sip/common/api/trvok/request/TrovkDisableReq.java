package com.cupdata.sip.common.api.trvok.request;

import lombok.Data;

@Data
public class TrovkDisableReq extends TrvokBaseVo{
	/**
	 * 券码
	 */
	private String verifyCode;
}
