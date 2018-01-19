package com.cupdata.notify.utils;


import java.sql.Date;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.vo.notify.NotifyToOrgVo;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.commons.vo.product.VoucherOrderVo;

public class NotifyUtil {
	/**
	 * 发送通知
	 * @param orderNotifyWait
	 * @return
	 */
	public static String httpToOrg(VoucherOrderVo voucherOrderVo){
		String resStr = "";
		//TODO  根据 机构 不同，通知的方式可能不同
		NotifyToOrgVo notifyToOrgVo = new NotifyToOrgVo();
		notifyToOrgVo.setOrderNo(voucherOrderVo.getOrder().getOrderNo());
		notifyToOrgVo.setUserMobileNo(voucherOrderVo.getVoucherOrder().getUserMobileNo());
		notifyToOrgVo.setUserName(voucherOrderVo.getVoucherOrder().getUserName());
		notifyToOrgVo.setUserPalce(voucherOrderVo.getVoucherOrder().getUserPalce());
		notifyToOrgVo.setUseTime(voucherOrderVo.getVoucherOrder().getUseTime());
		String params = JSONObject.toJSONString(notifyToOrgVo);
		resStr = HttpUtil.doPost(voucherOrderVo.getOrder().getNotifyUrl(), params);//TODO 通知参数待定，可能需要加密
		return resStr;
	}
	
	/**
	 * 初始化  OrderNotifyWait
	 * @param orderNotifyWait
	 */
	public static OrderNotifyWait initOrderNotifyWait(String orderNo,String notifyUrl) {
		OrderNotifyWait orderNotifyWait = new OrderNotifyWait();
		orderNotifyWait.setOrderNo(orderNo);
		orderNotifyWait.setNotifyUrl(notifyUrl);
		orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(1,DateTimeUtil.getCurrentTime()));
		orderNotifyWait.setNotifyTimes(1);
		orderNotifyWait.setNotifyStatus(ModelConstants.NOTIFY_STATUS_FAIL);
		orderNotifyWait.setNodeName("node");//TODO节点名称
		return orderNotifyWait;
	}
	
	/**
	 * 初始化  OrderNotifyComplete
	 * @param orderNotifyWait
	 */
	public static OrderNotifyComplete initOrderNotifyComplete(String orderNo,String notifyUrl) {
		OrderNotifyComplete orderNotifyComplete = new OrderNotifyComplete();
		orderNotifyComplete.setOrderNo(orderNo);
		orderNotifyComplete.setNotifyUrl(notifyUrl);
		orderNotifyComplete.setCompleteDate(NotifyNextTime.GetNextTime(1,DateTimeUtil.getCurrentTime()));
		orderNotifyComplete.setNotifyTimes(1);
		orderNotifyComplete.setNotifyStatus(ModelConstants.NOTIFY_STATUS_SUCCESS);
		orderNotifyComplete.setNodeName("node");//TODO节点名称
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
		orderNotifyComplete.setNotifyTimes(orderNotifyWait.getNotifyTimes());
		orderNotifyComplete.setNotifyStatus(notifyStatus);
		orderNotifyComplete.setNodeName(orderNotifyWait.getNodeName());
		return orderNotifyComplete;
	}
}
