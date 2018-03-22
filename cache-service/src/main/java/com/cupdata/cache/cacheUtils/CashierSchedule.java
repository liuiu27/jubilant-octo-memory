package com.cupdata.cache.cacheUtils;

import com.cupdata.cache.utils.SpringContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



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

	@Autowired
	private SpringContext springUtil;

	@Autowired
	private CacheManager cacheManager;

	@Value("${refreshCacheCron:* * 0/1 * * ? }")
	String refreshCacheCron;

	/**
	 * 每十分钟刷新系统缓存
	 */
	@Scheduled(cron = "* 0/10 * * * ?")
	public void scheduleRefreshAllCache(){
		log.info("--每十分钟刷新系统缓存...开始--");
//		CacheManager cacheManager = springUtil.getBean(CacheManager.class);
		cacheManager.refreshAllCache();
		log.info("--每十分钟刷新系统缓存....结束--");
	}
}
