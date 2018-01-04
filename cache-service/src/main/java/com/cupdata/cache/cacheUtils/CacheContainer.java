package com.cupdata.cache.cacheUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: CacheContainer
 * @Description: 缓存容器
 * @author LinYong
 * @date 2016年9月5日 下午8:43:44
 * 
 */
public class CacheContainer implements Cache {
	/**
	 * 日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	private static CacheContainer SINGLETON = null;

	/**
	 * 存放缓存数据
	 */
	private static Map<String, CacheData> CACHE_MAP;

	/**
	 * 缓存周期
	 */
	private static final long INTERVAL_TIME = 0;

	/**
	 * 访问次数
	 */
	private static final int MAX_VISIT_COUNT = 0;

	private CacheContainer() {
		CACHE_MAP = new HashMap<String, CacheData>();
	}

	static CacheContainer getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new CacheContainer();
		}
		return SINGLETON;
	}

	/**
	 * 刷新缓存
	 */
	@Override
	public synchronized void refreshCacheData(String key, Object data) {
		addCacheData(key, data, false);
	}
	
	/**
	 * 添加数据缓存
	 * 判断是否存在KEY，如果存在相同的KEY，则不再添加
	 * @param key
	 * @param data
	 */
	public synchronized void addCacheData(String key, Object data) {
		addCacheData(key, data, true);
	}

	/**
	 * 添加缓存数据
	 * @param key
	 * @param data
	 * @param check
	 */
	private void addCacheData(String key, Object data, boolean check) {
		if (check && CACHE_MAP.containsKey(key)) {
			log.warn("WEB缓存：key值= " + key + " 在缓存中重复, 本次不缓存！");
			return;
		}
		log.info("刷新缓存Cache！key值为" + key);
		CACHE_MAP.put(key, new CacheData(data));
	}

	/**
	 * 获取缓存数据
	 */
	public Object getCacheData(String key) {
		return getCacheData(key, INTERVAL_TIME, MAX_VISIT_COUNT);
	}

	/**
	 * 取得缓存中的数据 与方法addCacheData(String key, Object data)配合使用
	 * 
	 * @param key 
	 * @param intervalTime 缓存的时间周期，小于等于0时不限制
	 * @param maxVisitCount 访问累积次数，小于等于0时不限制
	 * @return
	 */
	private Object getCacheData(String key, long intervalTime, int maxVisitCount) {
		CacheData cacheData = (CacheData) CACHE_MAP.get(key);
		if (cacheData == null) {
			return null;
		}
		if (intervalTime > 0 && (System.currentTimeMillis() - cacheData.getTime()) > intervalTime) {
			removeCacheData(key);
			return null;
		}
		if (maxVisitCount > 0 && (maxVisitCount - cacheData.getCount()) <= 0) {
			removeCacheData(key);
			return null;
		} else {
			cacheData.addCount();
		}
		return cacheData.getData();
	}

	/**
	 * 获取缓存时间
	 */
	public Long getCacheTime(String key) {
		CacheData cacheData = (CacheData) CACHE_MAP.get(key);
		if (cacheData != null) {
			return cacheData.getTime();
		}
		return null;
	}

	/**
	 * 移除所有缓存中的数据
	 */
	public void removeAllCacheData() {
		CACHE_MAP.clear();
	}

	/**
	 * 移除缓存中的数据
	 * 
	 * @param key
	 */
	public void removeCacheData(String key) {
		CACHE_MAP.remove(key);
	}

	/**
	 * 返回缓存名称列表.
	 * @return
	 */
	public Object[] getCacheNames() {
		if (MapUtils.isNotEmpty(CACHE_MAP)) {
			return CACHE_MAP.keySet().toArray();
		} else {
			return null;
		}
	}

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	public boolean isExistKeyForCache(String key) {
		if (key != null && CACHE_MAP.containsKey(key)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Map map = new HashMap<String, String>();
		
		map.put("key", "value1");
		String value1 = (String) map.get("key");
		String value2 = "value2";
		map.put("key", value2);
		System.out.println(map.get("key"));
	}
}
