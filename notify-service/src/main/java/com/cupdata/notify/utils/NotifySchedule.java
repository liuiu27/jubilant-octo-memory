package com.cupdata.notify.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cupdata.commons.model.ServiceOrder;
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
	@Scheduled(cron = "0/5 * * * * ?")
	public void notifyToOrgTask(){
		log.info("-------------------   start   notifyToOrgTask   "+ DateTimeUtil.getCurrentTime() +" ------------------- ");
		//获取当前节点
		String nodeName = CommonUtils.getHostAddress()+":"+ServerPort.getPort();
        Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nodeName", nodeName);
		//查询所有失败的通知
		System.out.println(ServerPort.getPort());
		
		List<OrderNotifyWait> orderNotifyWaitList = notifyBiz.selectAll(paramMap);
        if(null != orderNotifyWaitList && orderNotifyWaitList.size()>0){
        	for (OrderNotifyWait orderNotifyWait : orderNotifyWaitList) { 
        		//判断通知次数是否>9
        		if(orderNotifyWait.getNotifyTimes() >= 9) {
        			log.info("初始化  orderNotifyComplete ");
        			OrderNotifyComplete orderNotifyComplete	= NotifyUtil.copyOrderNotifyComplete(orderNotifyWait,ModelConstants.NOTIFY_STATUS_FAIL);
        			log.info("移动到  orderNotifyComplete");
        			notifyBiz.orderNotifyCompleteInsert(orderNotifyComplete);
        			log.info("删除 orderNotifyWait");
        			notifyBiz.delete(Integer.parseInt(orderNotifyWait.getId().toString()));
        		}else {
        			log.info("获取订单信息");
        			BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByOrderNo(orderNotifyWait.getOrderNo());
        			if(!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
        				log.error("getVoucherOrderByOrderNo result is null orderNO is" + orderNotifyWait.getOrderNo());
        				return;
        			}
        			
        			log.info("根据机构号获取机构信息 秘钥");
        			BaseResponse<OrgInfVo> orgInf = cacheFeignClient.getOrgInf(voucherOrderVo.getData().getOrder().getOrgNo());
        			if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
        				log.error("cacher-service getOrgInf result is null orgNo is" + voucherOrderVo.getData().getOrder().getOrgNo());
        			}
        			log.info("发送通知 ");
        			if(!NotifyUtil.httpToOrg(voucherOrderVo.getData(),orgInf.getData())) {
        				log.error("FAIL   notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is  " + orderNotifyWait.getNotifyTimes());
        				log.info("通知失败  通知失败次数 +1  下次通知时间修改   ");
        				orderNotifyWait.setNotifyTimes(orderNotifyWait.getNotifyTimes()+1);
        				log.info("通知时间修改");
        				orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(orderNotifyWait.getNotifyTimes(), orderNotifyWait.getNextNotifyDate()));
        				notifyBiz.update(orderNotifyWait);
        			}else {
        				log.info("SUCCESS  notify url is " + orderNotifyWait.getNotifyUrl() + "notifyTimes is " + orderNotifyWait.getNotifyTimes());
        				log.info("通知成功  删除 wait表"); 
        				notifyBiz.delete(orderNotifyWait.getId());
        				log.info("初始化  orderNotifyComplete");
            			OrderNotifyComplete orderNotifyComplete	= NotifyUtil.copyOrderNotifyComplete(orderNotifyWait,ModelConstants.NOTIFY_STATUS_SUCCESS);
            			log.info("移动到  orderNotifyComplete");
            			notifyBiz.orderNotifyCompleteInsert(orderNotifyComplete);
        			}
        		}
        	}
        }
        log.info("-------------------   END  notifyToOrgTask  "+ DateTimeUtil.getCurrentTime() +" ------------------- ");
	}

    /**
     * 定时通知业务（轮训wait表,轮寻当前服务器节点下的订单,查询出通知订单的结果信息,去通知机构）
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void RechargeNotifyToOrgTask(){
        log.info("开启充值轮寻通知,当前服务器节点:"+CommonUtils.getHostAddress()+",...开始轮寻Notify_waite表");
        //获取当前服务器节点,只轮训当前服务器节点下的订单
        String nodeName = CommonUtils.getHostAddress();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nodeName", nodeName);
        //查询当前服务器下所有失败的通知
        List<OrderNotifyWait> orderNotifyWaitList = notifyBiz.selectAll(paramMap);
        log.info("当前服务器节点失败订单数为："+orderNotifyWaitList.size());
        if(null != orderNotifyWaitList && orderNotifyWaitList.size()>0){
            for (OrderNotifyWait orderNotifyWait : orderNotifyWaitList) {
                //判断通知次数是否大于等于9次
                if(orderNotifyWait.getNotifyTimes() >= 9) {
                    //初始化  orderNotifyComplete
                    log.info("初始化  orderNotifyComplete");
                    OrderNotifyComplete orderNotifyComplete	= NotifyUtil.copyOrderNotifyComplete(orderNotifyWait,ModelConstants.NOTIFY_STATUS_FAIL);

                    //移动到  orderNotifyComplete
                    log.info("移动到  orderNotifyComplete");
                    notifyBiz.orderNotifyCompleteInsert(orderNotifyComplete);

                    //删除 orderNotifyWait
                    log.info("删除 orderNotifyWait");
                    notifyBiz.delete(Long.valueOf(orderNotifyWait.getId()));
                }else {
                    //step1.获取当前订单的订单编号
                    String orderNo = orderNotifyWait.getOrderNo();
                    log.info("当前轮训订单编号:"+orderNo);

                    //step2.查询当前订单是充值订单还是券码订单(判断ORDER_TYPE是券码还是充值)
                    BaseResponse<ServiceOrder> serviceOrderRes = orderFeignClient.getServiceOrderByOrderNo(orderNo);
                    if (!ResponseCodeMsg.SUCCESS.getCode().equals(serviceOrderRes.getResponseCode())){
                        log.error("订单查询失败");
                        return;
                    }
                    log.info("当前订单编号:"+serviceOrderRes.getData().getOrderNo()+",订单描述："+serviceOrderRes.getData().getOrderDesc());

                    //step3.根据订单中的机构号来获取机构信息的公钥
                    BaseResponse<OrgInfVo> orgInf = cacheFeignClient.getOrgInf(serviceOrderRes.getData().getOrgNo());
                    if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
                        log.error("cacher-service getOrgInf result is null orgNo is" + serviceOrderRes.getData().getOrgNo());
                    }

                    //step4.发送通知
                    if(!NotifyUtil.rechargeHttpToOrg(serviceOrderRes.getData(),orgInf.getData())) {
                        log.error("本次通知失败，NotifyUrl：" + orderNotifyWait.getNotifyUrl() + "notifyTimes is： " + orderNotifyWait.getNotifyTimes());

                        //通知失败  通知失败次数 +1  下次通知时间修改
                        log.info("通知次数+1");
                        orderNotifyWait.setNotifyTimes(orderNotifyWait.getNotifyTimes()+1);

                        //通知时间修改
                        log.info("修改通知时间");
                        orderNotifyWait.setNextNotifyDate(NotifyNextTime.GetNextTime(orderNotifyWait.getNotifyTimes(), orderNotifyWait.getNextNotifyDate()));
                        notifyBiz.update(orderNotifyWait);
                    }else {
                        log.info("本次通知成功，NotifyUrl： " + orderNotifyWait.getNotifyUrl() + "notifyTimes is：" + orderNotifyWait.getNotifyTimes());

                        //初始化  orderNotifyComplete
                        log.info("初始化  orderNotifyComplete");
                        OrderNotifyComplete orderNotifyComplete	= NotifyUtil.copyOrderNotifyComplete(orderNotifyWait,ModelConstants.NOTIFY_STATUS_SUCCESS);

                        //移动到  orderNotifyComplete
                        log.info("移动到  orderNotifyComplete");
                        notifyBiz.orderNotifyCompleteInsert(orderNotifyComplete);

                        //通知成功  删除 wait表
                        log.info("删除 orderNotifyWait");
                        notifyBiz.delete(Long.valueOf(orderNotifyWait.getId()));
                    }
                }
            }
        }
    }

}
