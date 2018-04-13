package com.cupdata.sip.common.api.config.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysConfigVO implements Serializable{
	/**
	 * 
	 */
	private String bankCode;
	/**
	 * 
	 */
	private String paraNameEn;
	/**
	 * 
	 */
	private String paraNameCn;
	/**
	 * 
	 */
	private String paraValue;

}
