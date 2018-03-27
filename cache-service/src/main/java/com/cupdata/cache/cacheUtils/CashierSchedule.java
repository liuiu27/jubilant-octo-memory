package com.cupdata.cache.cacheUtils;

import com.cupdata.cache.utils.SpringContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

	//10分钟
	private final static String refreshCacheCron ="0 */10 * * * ?";




	/**
	 * 每十分钟刷新系统缓存     测试环境每小时刷新一次缓存
	 */
<<<<<<< HEAD
	@Scheduled(cron = "*0 0/60 * * * ?")
=======
	@Scheduled(cron = refreshCacheCron)
>>>>>>> 4ccd8b3a64956b40d5e35ca1484aa019c2953a13
	public void scheduleRefreshAllCache(){
		log.info("--每十分钟刷新系统缓存...开始--");
//		CacheManager cacheManager = springUtil.getBean(CacheManager.class);
		cacheManager.refreshAllCache();
		log.info("--每十分钟刷新系统缓存....结束--");
	}
}
