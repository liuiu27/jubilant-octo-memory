package com.cupdata.cache.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.cupdata.cache.cacheUtils.CacheManager;

/**
 * 
* @ClassName: StartupListener 
* @Description: 项目启动的监听器
* @author LinYong 
* @date 2016年8月16日 下午7:49:58 
*
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent>{
	/**
	 * 日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		log.info("初始化启动监听器......");
		this.init();
	}

	/**
	 * 项目启动后初始化
	 */
	public void init(){
		//刷新缓存
		log.info("项目启动，刷新全部缓存....");
		CacheManager.refreshAllCache();
	}
}

