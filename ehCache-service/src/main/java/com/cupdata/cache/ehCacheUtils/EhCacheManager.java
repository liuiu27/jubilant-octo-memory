package com.cupdata.cache.ehCacheUtils;

import java.net.URL;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cupdata.cache.fegin.ConfigFeignClient;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.model.SysConfig;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.sysconfig.SysConfigListVo;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月10日 上午9:47:14
*/
@Slf4j
@Component
public class EhCacheManager {
	
	@Autowired
	private ConfigFeignClient configFeignClient;
	
	static Cache<String, String> config;
	
	CacheManager myCacheManager = null;
	
	@Test
	public void  init() {
        URL myUrl = getClass().getResource("/ehcache.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
        refreshAllCache();
//      myCacheManager.close();
	}
	
	void refreshAllCache() {
		log.info("缓存所有系统配置参数...");
		BaseResponse<SysConfigListVo> sysConfigListVoRes =  configFeignClient.selectAll();
		if (ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigListVoRes.getResponseCode())){
			config = myCacheManager.getCache("foo",String.class,String.class);
			config.put("config", "66");
		}else {
			log.error("调用config-service获取系统配置出现错误，响应码为" + sysConfigListVoRes.getResponseCode());
		}
	}
	
	public static SysConfig getSysConfig(String bankCode, String paraName) {
		if(StringUtils.isBlank(paraName)) {
			log.error("paraName is null");
			return null;
		}
		String a = config.get("config");
		List<SysConfig> list = null;
		if (CollectionUtils.isNotEmpty(list)) {
			if (StringUtils.isBlank(bankCode)) {
				bankCode = "CUPD";
			}
			for (SysConfig config : list) {
				if (bankCode.equals(config.getBankCode()) && paraName.equals(config.getParaNameEn())) {
					return config;
				}
			}
		}
		log.error("getSysConfig result is null  bankCode is " + bankCode + "paraName is " + paraName);
		return null;
	}
	
	
}
