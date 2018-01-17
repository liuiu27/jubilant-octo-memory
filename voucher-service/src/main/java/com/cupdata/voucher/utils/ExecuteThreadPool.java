package com.cupdata.voucher.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 
* @ClassName: ExecuteThreadPool 
* @Description: 线程池
* @author vince.xu
* @date 2017-10-16 下午04:38:38 
*
 */
@Component
public class ExecuteThreadPool {
	/**
	 * 线程池
	 */
	private static ExecutorService EXECUTE_THREAD_POOL;
	
	private static int THREAD_POOL_NUM = 30;
	
	protected Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ExecuteThreadPool() {
		super();
		init();
	}
	/**
	 * 初始化线程池
	 */
	private void init() {
		/**
		 * 固定大小线程池
		 */
		EXECUTE_THREAD_POOL = Executors.newFixedThreadPool(THREAD_POOL_NUM);
	}
	/**
	 * 向线程池添加处理业务逻辑的任务
	 * @param mainOrder 主订单
	 * 
	 */
	public void addNotifyTaskToPool( final String orderNo) {
		EXECUTE_THREAD_POOL.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("Start executing threads...");
				try{
					 String url = "http://notify-service/notify/notifyToOrg3Times/" + orderNo;
		             restTemplate.getForEntity(url, String.class);
				}catch (Exception e) {
					logger.error(" executing threads error ..." , e);
				}
			}
		});
	}
}
