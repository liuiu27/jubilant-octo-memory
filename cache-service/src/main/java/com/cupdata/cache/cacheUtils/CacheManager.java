package com.cupdata.cache.cacheUtils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cupdata.cache.biz.BankInfBiz;
import com.cupdata.cache.biz.ConfigBiz;
import com.cupdata.cache.biz.OrgInfBiz;
import com.cupdata.cache.biz.ServiceSupplierBiz;
import com.cupdata.cache.utils.SpringUtil;
import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceSupplier;
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
	
	private static BankInfBiz bankInfBiz = (BankInfBiz) SpringUtil.getBean("bankInfBiz");
	
	private static OrgInfBiz orgInfBiz = (OrgInfBiz) SpringUtil.getBean("orgInfBiz");
	
	private static ServiceSupplierBiz serviceSupplierBiz = (ServiceSupplierBiz) SpringUtil.getBean("serviceSupplierBiz");
	
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
			log.info("缓存所有系统配置参数...");
			CACHE.refreshCacheData(CacheConstants.CACHE_TYPE_SYS_CONFIG,configBiz.selectAll(null));
			log.info("缓存所有银行数据信息...");
			CACHE.refreshCacheData(CacheConstants.CACHE_TYPE_BANKINF,bankInfBiz.selectAll(null));
			log.info("缓存所有机构数据信息...");
			CACHE.refreshCacheData(CacheConstants.CACHE_TYPE_ORGINF,orgInfBiz.selectAll(null));
			log.info("缓存所有供应商数据信息...");
			CACHE.refreshCacheData(CacheConstants.CACHE_TYPE_SUPPLIER,serviceSupplierBiz.selectAll(null));
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
	public static String getSysConfig(String bankCode, String paraName) {
		if(StringUtils.isBlank(paraName)) {
			log.error("paraName is null");
			return null;
		}
		List<SysConfig> list = (List<SysConfig>) getCache(CacheConstants.CACHE_TYPE_SYS_CONFIG);
		if (CollectionUtils.isNotEmpty(list)) {
			if (StringUtils.isBlank(bankCode)) {
				bankCode = "CUPD";
			}
			for (SysConfig config : list) {
				if (bankCode.equals(config.getBankCode()) && paraName.equals(config.getParaNameEn())) {
					return config.getParaValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取 银行 信息
	 * @return
	 */
	public static BankInf getBankInf(String bankCode) {
		List<BankInf> list = (List<BankInf>) getCache(CacheConstants.CACHE_TYPE_BANKINF);
		if (CollectionUtils.isNotEmpty(list)) {
			if (StringUtils.isNotBlank(bankCode)) {
				for (BankInf bankInf : list) {
					if (bankCode.equals(bankInf.getBankCode())) {
						return bankInf;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取 机构 信息
	 * @return
	 */
	public static OrgInf getOrgInf(String orgNo) {
		List<OrgInf> list = (List<OrgInf>) getCache(CacheConstants.CACHE_TYPE_ORGINF);
		if (CollectionUtils.isNotEmpty(list)) {
			if (StringUtils.isNotBlank(orgNo)) {
				for (OrgInf orgInf : list) {
					if (orgNo.equals(orgInf.getOrgNo())) {
						return orgInf;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取 供应商  信息
	 * @return
	 */
	public static ServiceSupplier getSupplier(String supplierNo) {
		List<ServiceSupplier> list = (List<ServiceSupplier>) getCache(CacheConstants.CACHE_TYPE_SUPPLIER);
		if (CollectionUtils.isNotEmpty(list)) {
			if (StringUtils.isNotBlank(supplierNo)) {
				for (ServiceSupplier serviceSupplier : list) {
					if (supplierNo.equals(serviceSupplier.getSupplierNo())) {
						return serviceSupplier;
					}
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
