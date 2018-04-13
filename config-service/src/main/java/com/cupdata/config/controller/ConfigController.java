package com.cupdata.config.controller;

import com.cupdata.commons.model.SysConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.config.IConfigController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.sysconfig.SysConfigListVo;
import com.cupdata.config.biz.ConfigBiz;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description: 配置信息的业务逻辑
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
		try {
			BaseResponse<SysConfigListVo> sysConfigListVoRes = new BaseResponse<SysConfigListVo>();
			SysConfigListVo configListVo = new SysConfigListVo();
			configListVo.setSysConfigList(configBiz.selectAll(null));
			sysConfigListVoRes.setData(configListVo);
			return sysConfigListVoRes;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	/**
	 * 获取系统配置信息
	 * @return
	 */
	public SysConfig getSysConfig(String bankCode, String paraName) {
		if(StringUtils.isBlank(paraName)) {
			log.error("paraName is null");
			return null;
		}
		List<SysConfig> list = (List<SysConfig>) getCache(CacheConstants.CACHE_TYPE_SYS_CONFIG);
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
