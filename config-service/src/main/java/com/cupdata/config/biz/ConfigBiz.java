package com.cupdata.config.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.config.dao.ConfigDao;

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
		String paraValue = configDao.getConfig(bankCode,paraName);
		if(!CommonUtils.isTrimEmpty(paraValue)) {
			return paraValue;
		}
		log.error("getConfig result is null");
		return null;
	}
	
}
