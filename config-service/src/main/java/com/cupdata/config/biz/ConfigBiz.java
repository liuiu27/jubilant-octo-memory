package com.cupdata.config.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
import com.cupdata.config.dao.ConfigDao;

@Service
public class ConfigBiz extends BaseBiz<SysConfig> {
	
	@Autowired ConfigDao configDao;
	
	@Override
	public BaseDao<SysConfig> getBaseDao() {
		return configDao;
	}
} 
