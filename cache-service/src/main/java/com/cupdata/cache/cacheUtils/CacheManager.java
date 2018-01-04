package com.cupdata.cache.cacheUtils;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cupdata.cache.biz.ConfigBiz;
import com.cupdata.cache.utils.SpringUtil;
import com.cupdata.commons.model.SysConfig;


/**
 * 
 * @ClassName: CacheManager
 * @Description: 缓存管理
 * @author LinYong
 * @date 2016年9月5日 下午8:07:27
 * 
 */
public class CacheManager {
	/**
	 * 日志
	 */
	protected static Logger log = Logger.getLogger(CacheManager.class);

	private static Cache CACHE = CacheContainer.getInstance();
	
	private static Long refreshMillis = System.currentTimeMillis();

	private static Long REFRESH_TIME_LIMIT = 20 * 1000L;
	
	private static ConfigBiz configBiz = (ConfigBiz) SpringUtil.getBean("configBiz");
	
//	/**
//	 * 构造缓存
//	 */
//	private CacheManager() {
//		super();
//		CACHE = CacheContainer.getInstance();
//	}

	/**
	 * 添加缓存
	 * 
	 * @param key
	 * @param obj
	 */
	public static void addCache(String key, Object obj) {
		if (CACHE != null) {
			CACHE.addCacheData(key, obj);
		}
	}

	/**
	 * 清除缓存,如果id为null,清除整个列表 如果id不为null,则删除列表中id匹配的对象
	 * 
	 * @param key
	 */
	public static void removeCache(String key) {
		if (CACHE != null) {
			CACHE.removeCacheData(key);
		}
	}

	/**
	 * 获得缓存对象
	 * 
	 * @param key 缓存key
	 * @return Object
	 */
	public static Object getCache(String key) {
		Object objs = null;
		if (CACHE != null) {
			objs = CACHE.getCacheData(key);
		}
		return objs;
	}

	/**
	 * 获得缓存该数据的时间
	 * 
	 * @param key
	 * @return
	 */
	public static Long getCacheTime(String key) {
		if (CACHE != null) {
			return CACHE.getCacheTime(key);
		}
		return null;
	}

	/**
	 * 刷新单个缓存
	 * 
	 * @param key
	 */
	public static void refreshCache(String key, Object data) {
		if (CACHE != null) {
			CACHE.refreshCacheData(key, data);
		} else {
			return;
		}
	}

	/**
	 * 刷新全部缓存
	 */
	public static void refreshAllCache() {
		if (CACHE != null) {
			//缓存系统配置参数
			log.info("缓存所有系统配置参数....");
			CACHE.refreshCacheData(CacheConstants.CACHE_TYPE_SYS_CONFIG,configBiz.selectAll(null));
			log.info("缓存所有银行数据信息");
			
			
			
		} else {
			log.info("CACHE为空！");
			return;
		}
	}

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isExistKeyCache(String key) {
		return CACHE == null ? false : CACHE.isExistKeyForCache(key);
	}

	/**
	 * 获取系统配置信息
	 * @return
	 */
	public static String getSysConfig(String bankCode, String paraNameEn) {
		List<SysConfig> list = (List<SysConfig>) getCache(CacheConstants.CACHE_TYPE_SYS_CONFIG);
		String outString = null;
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysConfig config : list) {
				if (bankCode.equals(config.getBankCode())) {
					outString = config.getParaValue();
				}
			}
		}
		return null;
	}
	
	

	/**
	 * 获取ConfigBiz
	 * @return
	 */
//	private static ConfigBiz getConfigService() {
//		
//	}
	
//	public static void main(String[] args) {
//		Map<String, Long> map = new HashMap<String, Long>();
//		Long start = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i ++)
//		{
//			map.put("test" + i,  1000L);
//		}
//		System.out.println(System.currentTimeMillis() - start);
//		configBiz.queryAllConfig();
//	}
}
