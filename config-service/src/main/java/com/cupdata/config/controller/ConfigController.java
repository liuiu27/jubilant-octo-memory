package com.cupdata.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.config.IConfigController;
import com.cupdata.commons.model.SysConfig;
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
	
	@Autowired 
	private ConfigBiz configBiz;

	@Override
	public List<SysConfig> selectAll() {
		log.info("ConfigController selectAll is begin...");
		return  configBiz.selectAll(null);
	}
}
