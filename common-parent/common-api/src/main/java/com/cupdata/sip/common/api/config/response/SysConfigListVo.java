package com.cupdata.sip.common.api.config.response;

import com.cupdata.commons.model.SysConfig;
import lombok.Data;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 13:25 2017/12/21
 */
@Data
public class SysConfigListVo{
    /**
     *
     */
    private List<SysConfig> sysConfigList;
}
