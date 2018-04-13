package com.cupdata.sip.common.api.config;

import com.cupdata.commons.model.SysConfig;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.config.response.SysConfigListVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: LinYong
 * @Description:获取系统配置
 * @Date: 13:07 2017/12/14
 */
@RequestMapping("/config")
public interface IConfigController {

    /**
     * 获取所有的配置信息
     * @return
     */
	@GetMapping("/selectAll")
	public BaseResponse<SysConfigListVo> selectAll();

    /**
     * 根据银行号和参数名获取系统配置信息
     * @param bankCode
     * @param paraName
     * @return
     */
	@GetMapping("/getSysConfig")
	SysConfig getSysConfig(@PathVariable("bankCode") String bankCode, @PathVariable("paraName") String paraName);
}
