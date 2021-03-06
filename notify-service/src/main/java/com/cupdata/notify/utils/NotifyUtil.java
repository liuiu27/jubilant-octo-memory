package com.cupdata.notify.utils;


import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.TimeConstants;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.notify.NotifyToOrgVo;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.commons.vo.notify.RechargeNotifyToOrgVo;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyUtil {

	/**
	 * 充值业务发送通知
	 * @param orgInfVo
	 * @param serviceOrder
	 * @return
	 */
	public static Boolean rechargeHttpToOrg(ServiceOrder serviceOrder, OrgInfVo orgInfVo){
		Boolean bl = false;
		try {
		    //封装充值业务通知vo
            RechargeNotifyToOrgVo rechargeNotifyToOrgVo = new RechargeNotifyToOrgVo();
            rechargeNotifyToOrgVo.setOrderNo(serviceOrder.getOrderNo());            //平台订单号
            rechargeNotifyToOrgVo.setOrgOrderNo(serviceOrder.getOrgOrderNo());      //机构唯一订单号
            rechargeNotifyToOrgVo.setRechargeStatus(serviceOrder.getOrderStatus()); //订单状态
			String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), TimeConstants.DATE_PATTERN_5) + CommonUtils.getCharAndNum(8);
            rechargeNotifyToOrgVo.setTimestamp(timestamp);    //时间戳

            //获取秘钥 加密 请求参数
			String pubKeyStr = orgInfVo.getOrgInf().getOrgPubKey();
			PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
			String reqStr = JSONObject.toJSONString(rechargeNotifyToOrgVo);
			String reqData = RSAUtils.encrypt(reqStr, uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
			reqData = URLEncoder.encode(reqData);
			String merchantPriKeyStr = orgInfVo.getOrgInf().getSipPriKey();
			PrivateKey merchantPriKey = RSAUtils.getPrivateKeyFromString(merchantPriKeyStr);
			String authReqSign = RSAUtils.sign(reqStr, merchantPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
			authReqSign = URLEncoder.encode(authReqSign);

			//发送请求
			log.info("dopost request url is " + serviceOrder.getNotifyUrl() + "data is  " +
					reqData + "sign is " + authReqSign);
			String resStr = HttpUtil.doPost(serviceOrder.getNotifyUrl(), "data=" + reqData + "&sign=" + authReqSign ,
					"application/x-www-form-urlencoded;charset=UTF-8");
			if("SUCCESS".equals(resStr)) {
				bl = true;
			}
		} catch (Exception e) {
			log.error("httpToOrg is error " + e.getMessage());
		}
		return bl;
	}

	/**
	 * 券码业务发送通知
	 * @param orgInfVo 
	 * @param voucherOrderVo
	 * @return
	 */
	public static Boolean httpToOrg(VoucherOrderVo voucherOrderVo, OrgInfVo orgInfVo){
		Boolean bl = false;
		try {
			NotifyToOrgVo notifyToOrgVo = new NotifyToOrgVo();
			notifyToOrgVo.setOrderNo(voucherOrderVo.getOrder().getOrderNo());
			notifyToOrgVo.setOrgOrderNo(voucherOrderVo.getOrder().getOrgOrderNo());
			notifyToOrgVo.setVoucherCode(voucherOrderVo.getVoucherOrder().getVoucherCode());
			notifyToOrgVo.setUserMobileNo(voucherOrderVo.getVoucherOrder().getUserMobileNo());
			notifyToOrgVo.setUserName(voucherOrderVo.getVoucherOrder().getUserName());
			notifyToOrgVo.setUsePalce(voucherOrderVo.getVoucherOrder().getUsePlace());
			notifyToOrgVo.setUseTime(voucherOrderVo.getVoucherOrder().getUseTime());
			String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), TimeConstants.DATE_PATTERN_5) + CommonUtils.getCharAndNum(8);
			notifyToOrgVo.setTimestamp(timestamp);
			//获取秘钥 加密 请求参数
			String pubKeyStr = orgInfVo.getOrgInf().getOrgPubKey();
			PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
			String reqStr = JSONObject.toJSONString(notifyToOrgVo);
			String reqData = RSAUtils.encrypt(reqStr, uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
			reqData = URLEncoder.encode(reqData,"utf-8");
			String merchantPriKeyStr = orgInfVo.getOrgInf().getSipPriKey();
			PrivateKey merchantPriKey = RSAUtils.getPrivateKeyFromString(merchantPriKeyStr);
			String authReqSign = RSAUtils.sign(reqStr, merchantPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
			authReqSign = URLEncoder.encode(authReqSign,"utf-8");
			// 发送请求
			log.info("dopost request url is " + voucherOrderVo.getOrder().getNotifyUrl() + "data is  " +  
					reqData + "sign is " + authReqSign);
			String resStr = HttpUtil.doPost(voucherOrderVo.getOrder().getNotifyUrl(), "data=" + reqData + "&sign=" + authReqSign ,
					"application/x-www-form-urlencoded;charset=UTF-8");
			if("SUCCESS".equals(resStr)) {
				bl = true;
			}
			
		} catch (Exception e) {
			log.error("httpToOrg is error " + e.getMessage());
		}
		return bl;
	}
	
	/**
	 * 初始化  OrderNotifyWait
	 * @param orderNo
	 */
	public static OrderNotifyWait initOrderNotifyWait(String orderNo,String notifyUrl) {
		OrderNotifyWait orderNotifyWait = new OrderNotifyWait();
		orderNotifyWait.setOrderNo(orderNo);
		orderNotifyWait.setNotifyUrl(notifyUrl);
		orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(1,DateTimeUtil.getCurrentTime()));
		orderNotifyWait.setNotifyTimes(1);
		orderNotifyWait.setNotifyStatus(ModelConstants.NOTIFY_STATUS_FAIL);
		orderNotifyWait.setNodeName(getNodeName());
		return orderNotifyWait;
	}
	
	/**
	 * 初始化  OrderNotifyComplete
	 * @param orderNo
	 */
	public static OrderNotifyComplete initOrderNotifyComplete(String orderNo,String notifyUrl) {
		OrderNotifyComplete orderNotifyComplete = new OrderNotifyComplete();
		orderNotifyComplete.setOrderNo(orderNo);
		orderNotifyComplete.setNotifyUrl(notifyUrl);
		orderNotifyComplete.setCompleteDate(NotifyNextTime.GetNextTime(1,DateTimeUtil.getCurrentTime()));
		orderNotifyComplete.setNotifyTimes(1);
		orderNotifyComplete.setNotifyStatus(ModelConstants.NOTIFY_STATUS_SUCCESS);
		orderNotifyComplete.setNodeName(getNodeName());
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
	
	/**
	 * 获取nodeName IP 加 端口号
	 * @return
	 */
	public static String getNodeName() {
		String ip = CommonUtils.getHostAddress();
		String port = ServerPort.getPort();
		String NodeName = ip + ":" + port;
		return NodeName;
	}
	
}
