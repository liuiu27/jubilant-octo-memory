package com.cupdata.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.notify.INotifyController;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.notify.biz.NotifyBiz;

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
	
	/**
	 * 通知
	 */
	@Override
	public void notifyToOrg(@RequestBody OrderNotifyWait orderNotifyWait) {
		log.info("NotifyController notifyToOrg is begin.......");
		notifyBiz.notifyToOrg(orderNotifyWait);
	}
    
}
