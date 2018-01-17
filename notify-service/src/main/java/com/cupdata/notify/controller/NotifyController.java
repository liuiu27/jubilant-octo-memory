package com.cupdata.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.notify.INotifyController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.notify.biz.NotifyBiz;
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
	/**
	 * 通知
	 */
	@Override
	public void notifyToOrg3Times(@PathVariable("orderNo") String orderNo) {
		log.info("NotifyController notifyToOrg is begin.......orderNo is" +  orderNo);
		BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByOrderNo(orderNo);
		if(!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
			log.error("getVoucherOrderByOrderNo result is null orderNO is" + orderNo);
			return;
		}
		notifyBiz.notifyToOrg3Times(voucherOrderVo.getData());
	}
    
}
