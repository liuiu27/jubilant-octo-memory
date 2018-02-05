package com.cupdata.commons.vo.notify;
/**
 * @Auth: liwei
 * @Description: 通知供应商参数Vo
 * @Date: 14:43 2018/1/17
 */

import java.util.Date;

import lombok.Data;

@Data
public class NotifyToOrgVo {
	/**
	 * 订单编号
	 */
	private String orderNo;
	
	/**
	 * 机构订单号
	 */
	private String orgOrderNo;
	
	/**
	 * 券码
	 */
	private String voucherCode;
	
	/**
	 * 用户姓名
	 */
	private String userName;
	
	/**
	 * 用户手机号
	 */
	private String userMobileNo;
	
	/**
	 * 使用时间
	 */
	private Date useTime;
	
	/**
	 * 使用地点
	 */
	private String usePalce;
}
