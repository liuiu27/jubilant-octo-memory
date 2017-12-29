package com.cupdata.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.config.IConfigController;
import com.cupdata.config.biz.ConfigBiz;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 10:34 2017/12/21
 */
@Slf4j
@RestController
public class ConfigController implements IConfigController{
	
	@Autowired ConfigBiz configBiz;
	public String getConfig(String bankNo,String paraName) {
		log.info("ConfigController getConfig is begin params bankNo  is " + bankNo + "paraName is" + paraName);
		return configBiz.getConfig(bankNo,paraName);
	}
}
