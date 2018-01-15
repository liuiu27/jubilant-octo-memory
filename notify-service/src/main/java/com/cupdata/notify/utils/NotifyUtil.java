package com.cupdata.notify.utils;

import java.util.Date;

import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;

public class NotifyUtil {
	/**
	 * 发送通知
	 * @param orderNotifyWait
	 * @return
	 */
	public static String httpToOrg(OrderNotifyWait orderNotifyWait){
		String resStr = "";
		//TODO  根据 机构 不同，通知的方式可能不同
		resStr = HttpUtil.doPost(orderNotifyWait.getNotifyUrl(), "");//TODO 通知参数待定，可能需要加密
		return resStr;
	}
	
	/**
	 * 初始化  OrderNotifyWait
	 * @param orderNotifyWait
	 */
	public static OrderNotifyWait initOrderNotifyWait(OrderNotifyWait orderNotifyWait) {
		orderNotifyWait.setNextNotifyDate(new Date());//TODO 下次通知时间待定
		orderNotifyWait.setNotifyTimes(1);
		orderNotifyWait.setNotifyStatus(ModelConstants.NOTIFY_STATUS_FAIL);
		orderNotifyWait.setNodeName("nodeName");//TODO节点名称
		return orderNotifyWait;
	}
	
	/**
	 * 初始化  OrderNotifyComplete
	 * @param orderNotifyWait
	 */
	public static OrderNotifyComplete initOrderNotifyComplete(OrderNotifyWait orderNotifyWait) {
		OrderNotifyComplete orderNotifyComplete = new OrderNotifyComplete();
		orderNotifyComplete.setOrderNo(orderNotifyWait.getOrderNo());
		orderNotifyComplete.setNotifyUrl(orderNotifyWait.getNotifyUrl());
		orderNotifyComplete.setCompleteDate(DateTimeUtil.getCurrentTime());
		orderNotifyComplete.setNotifyTimes(1);
		orderNotifyComplete.setNotifyStatus(ModelConstants.NOTIFY_STATUS_SUCCESS);
		orderNotifyComplete.setNodeName("nodeName");//TODO节点名称
		return orderNotifyComplete;
	}
	
	/**
	 * 复制wait表到Complete
	 * @param orderNotifyWait 
	 * @return
	 */
	public static OrderNotifyComplete copyOrderNotifyComplete(OrderNotifyWait orderNotifyWait,Character notifyStatus) {
		OrderNotifyComplete orderNotifyComplete = new OrderNotifyComplete();
		orderNotifyComplete.setOrderNo(orderNotifyWait.getOrderNo());
		orderNotifyComplete.setNotifyUrl(orderNotifyWait.getNotifyUrl());
		orderNotifyComplete.setCompleteDate(orderNotifyWait.getNextNotifyDate());
		orderNotifyComplete.setNotifyTimes(orderNotifyWait.getNotifyTimes() + 1);
		orderNotifyComplete.setNotifyStatus(notifyStatus);
		orderNotifyComplete.setNodeName(orderNotifyWait.getNodeName());
		return orderNotifyComplete;
	}
}
