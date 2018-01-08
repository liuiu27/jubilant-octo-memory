package com.cupdata.cache.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.cache.dao.ConfigDao;
import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
import com.cupdata.commons.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConfigBiz extends BaseBiz<SysConfig> {
	
	@Autowired ConfigDao configDao;
	
	@Override
	public BaseDao<SysConfig> getBaseDao() {
		return configDao;
	}
	
	public String getConfig(String bankCode, String paraName) {
		if(StringUtils.isBlank(paraName)){
			log.error("getConfig paraName is null");
			return null;
		}
		if(StringUtils.isBlank(bankCode)) {
			bankCode = "CUPD";
		}
		String paraValue = configDao.getConfig(bankCode,paraName);
		if(!CommonUtils.isTrimEmpty(paraValue)) {
			return paraValue;
		}
		log.error("getConfig result is null");
		return null;
	}
	
	public List<SysConfig> queryAllConfig(){
		return configDao.queryAllConfig();
	}
} 