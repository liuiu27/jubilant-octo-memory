package com.cupdata.commons.vo.trvok;

import lombok.Data;

@Data
public class TrovkActivatReq extends TrvokBaseVo{
	/**
	 * 券码
	 */
	private String verifyCode;
	/**
	 * 有效期 YYYY-MM-DD
	 */
	private String expire;
}
