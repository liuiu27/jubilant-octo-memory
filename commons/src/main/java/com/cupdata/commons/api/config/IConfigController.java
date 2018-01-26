package com.cupdata.commons.api.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.sysconfig.SysConfigListVo;

/**
 * @Auth: LinYong
 * @Description:获取系统配置
 * @Date: 13:07 2017/12/14
 */
@RequestMapping("/config")
public interface IConfigController{
	
	@GetMapping("/selectAll")
	public BaseResponse<SysConfigListVo> selectAll();
}
