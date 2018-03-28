package com.cupdata.notify.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.vo.product.RechargeOrderVo;
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
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.notify.biz.NotifyBiz;
import com.cupdata.notify.feign.CacheFeignClient;
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
	
	@Autowired
	private CacheFeignClient cacheFeignClient;
	/**
	 * 每分钟执行通知
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void notifyToOrgTask(){
		log.info("-------------------   start   notifyToOrgTask   "+ DateTimeUtil.getCurrentTime() +" ------------------- ");
		//获取当前节点
		String nodeName = "node"; //TODO 获取节点
        Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeName", nodeName);
		//查询所有失败的通知
		System.out.println(ServerPort.getPort());
		
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
        			//获取订单信息
        			BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByOrderNo(orderNotifyWait.getOrderNo());
        			if(!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
        				log.error("getVoucherOrderByOrderNo result is null orderNO is" + orderNotifyWait.getOrderNo());
        				return;
        			}
        			
        			//根据机构号获取机构信息 秘钥
        			BaseResponse<OrgInfVo> orgInf = cacheFeignClient.getOrgInf(voucherOrderVo.getData().getOrder().getOrgNo());
        			if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
        				log.error("cacher-service getOrgInf result is null orgNo is" + voucherOrderVo.getData().getOrder().getOrgNo());
        			}
        			//发送通知
        			if(!NotifyUtil.httpToOrg(voucherOrderVo.getData(),orgInf.getData())) {
        				log.error("FAIL   notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is  " + orderNotifyWait.getNotifyTimes());
        				//通知失败  通知失败次数 +1  下次通知时间修改
        				orderNotifyWait.setNotifyTimes(orderNotifyWait.getNotifyTimes()+1);
        				//通知时间修改
        				orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(orderNotifyWait.getNotifyTimes(), orderNotifyWait.getNextNotifyDate()));
        				notifyBiz.update(orderNotifyWait);
        			}else {
        				log.info("SUCCESS  notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is " + orderNotifyWait.getNotifyTimes());
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

    /**
     * 充值定时通知业务
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void RechargeNotifyToOrgTask(){
        log.info("开启充值轮训通知,当前服务器节点:"+CommonUtils.getServerFlag()+",...开始轮训Notify_waite表");
        //获取当前服务器节点,只轮训当前服务器节点下的订单
        String nodeName = CommonUtils.getServerFlag();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nodeName", nodeName);
        //查询当前服务器下所有失败的通知
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
                    //获取订单信息
                    BaseResponse<RechargeOrderVo> rechargeOrderVo = orderFeignClient.getRechargeOrderByOrderNo(orderNotifyWait.getOrderNo());
                    if(!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderVo.getResponseCode())) {
                        log.error("getRechargeOrderByOrderNo result is null orderNO is" + orderNotifyWait.getOrderNo());
                        return;
                    }

                    //根据机构号获取机构信息 秘钥
                    BaseResponse<OrgInfVo> orgInf = cacheFeignClient.getOrgInf(rechargeOrderVo.getData().getOrder().getOrgNo());
                    if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
                        log.error("cacher-service getOrgInf result is null orgNo is" + rechargeOrderVo.getData().getOrder().getOrgNo());
                    }
                    //发送通知
                    if(!NotifyUtil.rechargeHttpToOrg(rechargeOrderVo.getData(),orgInf.getData())) {
                        log.error("FAIL   notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is  " + orderNotifyWait.getNotifyTimes());
                        //通知失败  通知失败次数 +1  下次通知时间修改
                        orderNotifyWait.setNotifyTimes(orderNotifyWait.getNotifyTimes()+1);
                        //通知时间修改
                        orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(orderNotifyWait.getNotifyTimes(), orderNotifyWait.getNextNotifyDate()));
                        notifyBiz.update(orderNotifyWait);
                    }else {
                        log.info("SUCCESS  notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is " + orderNotifyWait.getNotifyTimes());
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
    }

}
