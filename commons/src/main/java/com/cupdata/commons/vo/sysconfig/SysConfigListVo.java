package com.cupdata.commons.vo.sysconfig;

import com.cupdata.commons.model.SysConfig;
import com.cupdata.commons.vo.BaseData;
import lombok.Data;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 13:25 2017/12/21
 */
@Data
public class SysConfigListVo extends BaseData{
    /**
     *
     */
    private List<SysConfig> sysConfigList;
}
