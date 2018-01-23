package com.cupdata.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.config.IConfigController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.sysconfig.SysConfigListVo;
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
	public BaseResponse<SysConfigListVo> selectAll() {
		log.info("ConfigController selectAll is begin...");
		BaseResponse<SysConfigListVo> sysConfigListVoRes = new BaseResponse<SysConfigListVo>();
		SysConfigListVo configListVo = new SysConfigListVo();
		configListVo.setSysConfigList(configBiz.selectAll(null));
		sysConfigListVoRes.setData(configListVo);
		return sysConfigListVoRes;
	}
}
