package com.cupdata.commons.vo.content;

import com.cupdata.commons.vo.BaseRequest;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月12日 下午4:02:00
*/
@Data
public class ContentLoginReq extends BaseRequest{
	
	
	/**
	 * 流水号
	 */
	private String sipTranNo;
	
	/**
	 * 供应商回跳地址
	 */
	private String callBackUrl;

}
