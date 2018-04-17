package com.cupdata.commons.vo.content;

import com.cupdata.commons.model.BaseModel;

/**
 * @author liwei
 * @date   2018/3/8
 */
 
import lombok.Data;

@Data
public class ContentTransactionVO{
	
	/**
	 * 时间戳
	 */
	private String timestamp;
	
	/**
	 * 交易流水编号
	 */
	private String sipTranNo;
	
	/**
	 * 商品编号
	 */
	private String productNo;
	
	/**
	 * 交易类型
	 */
	private String tranType;
	
	/**
	 * 交易描述
	 */
	private String tranDesc;
	
	/**
	 * 机构编号
	 */
	private String orgNo;
	
	/**
	 * 商户编号
	 */
	private String supNo;
	
	/**
	 * 请求信息
	 */
	private String requestInfo;

	
}
