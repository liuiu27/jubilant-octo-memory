package com.cupdata.cache.cacheUtils;

public interface Cache {
	/**
	 * 刷新缓存
	 */
	public void refreshCacheData(String key, Object data);
	
	/**
	 * 添加缓存
	 * @param key
	 * @param data
	 */
    public void addCacheData(String key, Object data);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public Object getCacheData(String key);

    /**
     * 获取缓存所有key名称
     * @return
     */
    public Object[] getCacheNames();

    /**
     * 删除所有缓存
     */
    public void removeAllCacheData();

    /**
     * 删除指定key的缓存
     * @param key
     */
    public void removeCacheData(String key);
    
    public boolean isExistKeyForCache(String key);

    public Long getCacheTime(String key);

}
