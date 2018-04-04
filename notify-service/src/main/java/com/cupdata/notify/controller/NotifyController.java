package com.cupdata.notify.controller;

import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.notify.utils.ServerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.notify.INotifyController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.notify.biz.NotifyBiz;
import com.cupdata.notify.feign.CacheFeignClient;
import com.cupdata.notify.feign.OrderFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liwei
 * @Description: 通知的服务
 * @create 2018-01-12 18:29
 */
@Slf4j
@RestController
public class NotifyController implements INotifyController{
	
	@Autowired
	private NotifyBiz notifyBiz;
	
	@Autowired
	private OrderFeignClient orderFeignClient;
	
	@Autowired
	private CacheFeignClient cacheFeignClient;
	
	/**
	 * 通知
	 */
	@Override
	public void notifyToOrg3Times(@PathVariable("orderNo") String orderNo) {
		log.info("NotifyController notifyToOrg is begin.......orderNo is" +  orderNo);
		try {
			//根据订单号 查询订单信息 和券码信息
			BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByOrderNo(orderNo);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
				log.error("order-service getVoucherOrderByOrderNo result is null orderNO is" + orderNo);
				return;
			}
			//根据机构号获取机构信息 秘钥
			BaseResponse<OrgInfVo> orgInf = cacheFeignClient.getOrgInf(voucherOrderVo.getData().getOrder().getOrgNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
				log.error("cacher-service getOrgInf result is null orgNo is" + voucherOrderVo.getData().getOrder().getOrgNo());
				return;
			}
			notifyBiz.notifyToOrg3Times(voucherOrderVo.getData(),orgInf.getData());
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}


	/**
	 * 充值通知
	 */
	@Override
	public void rechargeNotifyToOrg3Times(@PathVariable("orderNo") String orderNo) {
		log.info("NotifyController rechargeNotifyToOrg is begin.......orderNo is" +  orderNo);
		try {
			//根据订单号 查询订单信息 和充值信息
			BaseResponse<ServiceOrder> ServiceOrderRes = orderFeignClient.getServiceOrderByOrderNo(orderNo);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(ServiceOrderRes.getResponseCode())) {
				log.error("order-service getRechargeOrderByOrderNo result is null orderNO is" + orderNo);
				return;
			}
			//根据机构号获取机构信息 秘钥
			BaseResponse<OrgInfVo> orgInf = cacheFeignClient.getOrgInf(ServiceOrderRes.getData().getOrgNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
				log.error("cacher-service getOrgInf result is null orgNo is" + ServiceOrderRes.getData().getOrgNo());
				return;
			}
			notifyBiz.rechargeNotifyToOrg3Times(ServiceOrderRes.getData(),orgInf.getData());
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}
}
