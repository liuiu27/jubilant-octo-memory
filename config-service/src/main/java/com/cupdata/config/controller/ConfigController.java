package com.cupdata.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.config.IConfigController;
import com.cupdata.config.biz.ConfigBiz;
import com.cupdata.config.feign.CacheFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 10:34 2017/12/21
 */
@Slf4j
@RestController
public class ConfigController implements IConfigController{
	
	@Autowired 
	private ConfigBiz configBiz;
	
	@Autowired 
	private CacheFeignClient cacheFeignClient;
	
	public String getConfig(@PathVariable("bankCode") String bankCode, @PathVariable("paraName") String paraName) {
		log.info("ConfigController getConfig is begin params bankNo  is " + bankCode + "paraName is" + paraName);
		return cacheFeignClient.getSysConfig(bankCode,paraName);
	}
}
