package com.cupdata.cache.cacheUtils;

/**
 * 
* @ClassName: CacheData 
* @Description: 缓存对象
* @author LinYong 
* @date 2016年9月6日 下午7:14:50 
*
 */
public class CacheData {
	/**
	 * 数据
	 */
	private Object data;
	
	/**
	 * 添加到缓存的时间
	 */
	private long time;
	
	/**
	 * 访问次数
	 */
	private int count;

	public CacheData(){
	}

	public CacheData(Object data, long time, int count){
		this.data = data;
		this.time = time;
		this.count = count;
	}

	public CacheData(Object data){
		this.data = data;
		this.time = System.currentTimeMillis();
		this.count = 1;
	}

	public void addCount(){
		count++;
	}

	public int getCount(){
		return count;
	}

	public void setCount(int count){
		this.count = count;
	}

	public Object getData(){
		return data;
	}

	public void setData(Object data){
		this.data = data;
	}

	public long getTime(){
		return time;
	}

	public void setTime(long time){
		this.time = time;
	}
}
