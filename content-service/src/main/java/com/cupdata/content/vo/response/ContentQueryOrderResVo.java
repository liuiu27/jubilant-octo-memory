package com.cupdata.content.vo.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午2:28:11
*/
@Getter
@Setter
public class ContentQueryOrderResVo{
	/**
	 * 返回标记记  0未 支付/退货  1支付/退货 失败  2支付/退货 成功
	 */
	@JSONField(serialize = false)
	private String resultCode;

	@JSONField(serialize = false)
	private String resultMsg;
	/**
	 * 平台订单号
	 */
	private String sipOrderNo ;
	
	/**
	 * 供应商订单号
	 */
	private String supOrderNo;
	
	/**
	 * 订单金额
	 */
	private String orderAmt;

	/**
	 * 机构订单号
	 */
	@JSONField(serialize = false)
	private String orgOrderNo;

}
