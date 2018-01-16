package com.cupdata.cache.listener;

import org.apache.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import com.cupdata.cache.cacheUtils.CacheManager;
import org.springframework.stereotype.Component;

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
		//在web 项目中（spring mvc），系统会存在两个容器，分别为root application context以及projectName-servlet context（作为root application context的子容器）
		//这种情况下，就会造成onApplicationEvent方法被执行两次。
		//root application context 没有parent
		if (applicationReadyEvent.getApplicationContext().getParent() == null){
			log.info("初始化启动监听器......");
			ConfigurableApplicationContext configurableApplicationContext = applicationReadyEvent.getApplicationContext();
			this.init(configurableApplicationContext);
		}
	}

	/**
	 * 项目启动后初始化
	 */
	public void init(ApplicationContext applicationContext){
		//刷新缓存
		log.info("项目启动，刷新全部缓存....");
        CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
		cacheManager.refreshAllCache();
	}
}