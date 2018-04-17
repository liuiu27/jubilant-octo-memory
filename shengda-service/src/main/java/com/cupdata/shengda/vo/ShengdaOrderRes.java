package com.cupdata.shengda.vo;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月30日 下午2:53:37
*/
@Data
public class ShengdaOrderRes {
	/**
	 * 返回码
	 */
	private String resultCode;
	
	/**
	 * 返回描述
	 */
	private String resultDesc;

	/**
	 * 盛大订单
	 */
	private String order;
}
