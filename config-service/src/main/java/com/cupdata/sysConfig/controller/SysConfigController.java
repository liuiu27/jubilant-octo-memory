package com.cupdata.sysConfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.sysConfig.ISysConfigController;
import com.cupdata.sysConfig.biz.SysConfigBiz;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 10:34 2017/12/21
 */
@Slf4j
@RestController
public class SysConfigController implements ISysConfigController{
	
	@Autowired SysConfigBiz sysConfigBiz;
	public String getSysConfig(String bankNo,String paraName) {
		log.info("SysConfigController getSysConfig is begin params bankNo  is " + bankNo + "paraName is" + paraName);
		return sysConfigBiz.getSysConfig(bankNo,paraName);
	}
}
