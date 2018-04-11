package com.cupdata.commons.vo.notify;


import java.util.Date;
import com.cupdata.commons.model.BaseModel;

import lombok.Data;

/**
 * @Auth: liwei
 * @Description: 回调等待表
 * @Date: 14:43 2018/1/11
 */
@Data
public class OrderNotifyWait extends BaseModel{
	/**
	 * 订单编号
	 */
	private String orderNo;
	
	/**
	 * 异步通知路径
	 */
	private String notifyUrl;
	
	/**
	 * 下次通知时间
	 */
	private Date nextNotifyDate;
	
	/**
	 * 通知次数 默认0，到达9次后移动到ORDER_NOTIFY_COMPLETE
	 */
	private int notifyTimes;
	
	/**
	 * 通知状态 0：未通知 1：通知失败 2：通知成功
	 */
	private Character notifyStatus;
	
	/**
	 * 节点名称
	 */
	private String nodeName;
}
