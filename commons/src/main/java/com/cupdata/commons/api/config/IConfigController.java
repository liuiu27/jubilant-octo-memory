package com.cupdata.commons.api.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.api.voucher.IVoucherApi;

/**
 * @Auth: LinYong
 * @Description:获取系统配置
 * @Date: 13:07 2017/12/14
 */
@RequestMapping("/config")
public interface IConfigController{
	@GetMapping("/getConfig/{bankCode}/{paraName}")
	public String getConfig(@PathVariable("bankCode")String bankCode,@PathVariable("paraName")String paraName);
}
