package com.cupdata.sysConfig.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.sysConfig.dao.SysConfigDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysConfigBiz extends BaseBiz<SysConfig> {
	
	@Autowired SysConfigDao sysConfigDao;
	
	@Override
	public BaseDao<SysConfig> getBaseDao() {
		return sysConfigDao;
	}
	
	public String getSysConfig(String bankCode, String paraName) {
		String paraValue = sysConfigDao.getSysConfig(bankCode,paraName);
		if(!CommonUtils.isTrimEmpty(paraValue)) {
			return paraValue;
		}
		log.error("getSysConfig result is null");
		return null;
	}
	
}
