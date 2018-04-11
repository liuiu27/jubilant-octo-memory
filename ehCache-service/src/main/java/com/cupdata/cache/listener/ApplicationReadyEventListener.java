package com.cupdata.cache.listener;

import org.apache.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.stereotype.Component;

import com.cupdata.cache.ehCacheUtils.EhCacheManager;

/**
 *
* @ClassName: ApplicationReadyEventListener
* @Description: 项目启动的监听器
* @author LinYong
* @date 2016年8月16日 下午7:49:58
*
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent>{
	/**
	 * 日志
	 */
	protected Logger log = Logger.getLogger(ApplicationReadyEventListener.class);

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		if (applicationReadyEvent.getApplicationContext().getParent() == null){
			log.info("初始化启动监听器......");
			ConfigurableApplicationContext configurableApplicationContext = applicationReadyEvent.getApplicationContext();
			this.init(configurableApplicationContext);
		}else {
			this.init(applicationReadyEvent.getApplicationContext());
		}
	}
	/**
	 * 项目启动后初始化
	 */
	public void init(ApplicationContext applicationContext){
		//刷新缓存
		log.info("项目启动，初始化缓存....");
		EhCacheManager cacheManager = applicationContext.getBean(EhCacheManager.class);
		cacheManager.init();
	}
}