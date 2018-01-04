package com.cupdata.cache.cacheUtils;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cupdata.commons.utils.CommonUtils;



/**
 * 
* @ClassName: MerchantOrderSchedule 
* @Description: 定时任务
* @author LinYong 
* @date 2016年9月27日 下午1:22:14 
*
 */
@Component
public class CashierSchedule {
	/**
	 * 日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
//	@Resource
//	private TradeNotifyWaitService notifyService;
	
	/**
	 * 订单定时任务
	 * 
	 */
	@Scheduled(cron = "0 * * * * ?")
	public void orderScheduleCenter(){
		log.info("--订单定时轮询任务....开始--");
		
		//任务1：定时轮询，通知商户支付状态
		if(!CommonUtils.isWindows()){
			notifyMerchantOrderStatus();
		}
		
		log.info("--订单定时轮询任务....结束--");
	}
	/**
	 * 每十分钟刷新系统缓存
	 */
	@Scheduled(cron = "0 0/10 * * * ?")
	public void scheduleRefreshAllCache(){
		log.info("--每十分钟刷新系统缓存...开始--");
		CacheManager.refreshAllCache();
		log.info("--每十分钟刷新系统缓存....结束--");
	}
	
	/**
	 * 定时处理-通知商户支付状态
	 */
	private void notifyMerchantOrderStatus(){
		try{
			log.info("开始执行通知商户订单支付状态");
//			List<UppNotifyWait> notifyWaitList = notifyService.select100NotifyWaitBelong(CommonUtils.getServerFlag());
//			if(CollectionUtils.isNotEmpty(notifyWaitList)){
//				for(UppNotifyWait notifyWait : notifyWaitList){
//					try{
//						CashierUtils.notifyPayResultIntermittently(notifyWait);
//					}catch(Exception e){
//						log.error("授权ID为" + notifyWait.getAuthId() + "，支付交易记录ID为" + notifyWait.getTradeLogId() + "，通知商户出现异常！", e);
//					}
//				}
//			}else{
//				log.info("查询通知记录表，没有获取到未通知的记录！");
//			}
		}catch(Exception e){
			log.error("", e);
		}
	}
	
}
