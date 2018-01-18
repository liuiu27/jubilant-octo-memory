package com.cupdata.notify.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.notify.biz.NotifyBiz;
import com.cupdata.notify.feign.OrderFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: NotifySchedule
* @Description: 定时任务
* @author liwei
* @date 2018/1/11 17:17
*
 */
@Slf4j
@Component
public class NotifySchedule {
	
	@Autowired
	private NotifyBiz notifyBiz;
	
	@Autowired
	private OrderFeignClient orderFeignClient;
	/**
	 * 每十分钟执行通知
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void notifyToOrgTask(){
		log.info("-------------------   start   notifyToOrgTask   "+ DateTimeUtil.getCurrentTime() +" ------------------- ");
		//获取当前节点
		String nodeName = "node"; //TODO 获取节点
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeName", nodeName);
		//查询所有失败的通知

		List<OrderNotifyWait> orderNotifyWaitList = notifyBiz.selectAll(paramMap);
        if(null != orderNotifyWaitList && orderNotifyWaitList.size()>0){
        	for (OrderNotifyWait orderNotifyWait : orderNotifyWaitList) { 
        		//判断通知次数是否>9
        		if(orderNotifyWait.getNotifyTimes() >= 9) {
        			//初始化  orderNotifyComplete 
        			OrderNotifyComplete orderNotifyComplete	= NotifyUtil.copyOrderNotifyComplete(orderNotifyWait,ModelConstants.NOTIFY_STATUS_FAIL);
        			//移动到  orderNotifyComplete
        			notifyBiz.orderNotifyCompleteInsert(orderNotifyComplete);
        			//删除 orderNotifyWait 
        			notifyBiz.delete(Integer.parseInt(orderNotifyWait.getId().toString()));
        		}else {
        			//发送通知
        			BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByOrderNo(orderNotifyWait.getOrderNo());
        			if(!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
        				log.error("getVoucherOrderByOrderNo result is null orderNO is" + orderNotifyWait.getOrderNo());
        				return;
        			}
        			String reqStr = NotifyUtil.httpToOrg(voucherOrderVo.getData());
        			if(StringUtils.isBlank(reqStr)) {
        				log.error("FAIL   notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is  " + orderNotifyWait.getNotifyTimes());
        				//通知失败  通知失败次数 +1  下次通知时间修改
        				orderNotifyWait.setNotifyTimes(orderNotifyWait.getNotifyTimes()+1);
        				//通知时间修改
        				orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(orderNotifyWait.getNotifyTimes(), orderNotifyWait.getNextNotifyDate()));
        				notifyBiz.update(orderNotifyWait);
        			}else {
        				log.info("SUCCESS  notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is" + orderNotifyWait.getNotifyTimes());
        				 //通知成功  删除 wait表 
        				notifyBiz.delete(orderNotifyWait.getId());
        				//初始化  orderNotifyComplete 
            			OrderNotifyComplete orderNotifyComplete	= NotifyUtil.copyOrderNotifyComplete(orderNotifyWait,ModelConstants.NOTIFY_STATUS_SUCCESS);
            			//移动到  orderNotifyComplete
            			notifyBiz.orderNotifyCompleteInsert(orderNotifyComplete);
        			}
        		}
        	}
        }
        log.info("-------------------   END  notifyToOrgTask  "+ DateTimeUtil.getCurrentTime() +" ------------------- ");
	}
}
