package com.cupdata.commons.api.config;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: LinYong
 * @Description:获取系统配置
 * @Date: 13:07 2017/12/14
 */
@RequestMapping("/config")
public interface IConfigController {
	public String getConfig(String bankCode,String paraName);
}
