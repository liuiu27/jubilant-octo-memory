package com.cupdata.shengda.vo;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月30日 下午2:41:30
*/
@Data
public class ShengdaOrderReq extends BaseRequest{
	/**
	 * 渠道号
	 */
	private String source;
	
	/**
	 * 机构渠道标识
	 */
	private String orgSource;
}
