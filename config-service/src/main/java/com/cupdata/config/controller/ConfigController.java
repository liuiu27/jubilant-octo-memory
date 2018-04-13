package com.cupdata.config.controller;


import com.cupdata.config.biz.ConfigBiz;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.config.IConfigController;
import com.cupdata.sip.common.api.config.response.SysConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description: 配置信息的业务逻辑
 * @Date: 10:34 2017/12/21
 */
@Slf4j
@RestController
public class ConfigController implements IConfigController {

    @Autowired
    private ConfigBiz configBiz;

    @Override
    public BaseResponse<List<SysConfigVO>> selectAll() {
        log.info("ConfigController selectAll is begin...");
        BaseResponse<List<SysConfigVO>> sysConfigListVoRes = new BaseResponse<>();

        List<SysConfigVO> sysConfigVOS = configBiz.selectAll();

        sysConfigListVoRes.setData(sysConfigVOS);

        return sysConfigListVoRes;
    }

    /**
     * 获取系统配置信息
     *
     * @return
     */
    public SysConfigVO getSysConfig(@NotBlank @PathVariable("paraName") String paraName, @PathVariable("bankCode") String bankCode) {

        bankCode = StringUtils.isBlank(bankCode) ? "CUPD" : bankCode;

        return configBiz.getSysConfig(paraName, bankCode);
    }
}
