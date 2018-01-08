package com.cupdata.commons.vo.trvok;

import lombok.Data;

@Data
public class TrovkDisableReq extends TrvokBaseVo{
	/**
	 * 券码
	 */
	private String verifyCode;
}
