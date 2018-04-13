package com.cupdata.sip.common.api.config;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.config.response.SysConfigVO;

import java.util.List;

/**
 * @author junliang
 * @date 2018/04/13
 */
public class ConfigControllerFallback implements IConfigController {
    @Override
    public BaseResponse<List<SysConfigVO>> selectAll() {
        return null;
    }

    @Override
    public SysConfigVO getSysConfig(String paraName, String bankCode) {
        return null;
    }
}
