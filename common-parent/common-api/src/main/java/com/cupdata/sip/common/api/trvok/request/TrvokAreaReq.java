package com.cupdata.sip.common.api.trvok.request;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:空港易行获取区域信息接口请求参数
 * @Date: 14:47 2017/12/19
 */

@Data
public class TrvokAreaReq extends TrvokBaseVo{
    /**
     * 区域类型
     * 1：APP全套；2：APP休息室；3：APP通道；4：APP国内全套；
     * 5：APP国际全套；6：APP国内休息室；7：APP国际休息室；8：APP高铁；
     * 9：APP境外休息室
     */
    private String areaType;
}
