package com.cupdata.cache;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.cupdata.cache.cacheUtils.CacheManager;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class CacheServiceApplication implements ApplicationListener<ContextRefreshedEvent>{

	public static void main(String[] args) {
		SpringApplication.run(CacheServiceApplication.class, args);
	}

	protected Logger log = Logger.getLogger(this.getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("初始化启动监听器......");
		CacheManager.refreshAllCache();
//		this.init();
	}

	/**
	 * 项目启动后初始化
	 */
/*	public void init() {
		// 刷新缓存
		log.info("项目启动，刷新全部缓存....");
		CacheManager cacheManager = new CacheManager();
		cacheManager.refreshAllCache();
	}*/
}
