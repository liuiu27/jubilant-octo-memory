package com.cupdata.sip.common.api.tencent.response;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description: QQ会员充值接口响应参数类
 * @@Date: Created in 13:48 2018/4/18
 */
@Data
public class QQOpenRes {
    /**
     * 错误码
     * 0：成功；-1：开通/关闭失败；-2：其他原因失败
     */
    private String result;

    /**
     * 错误描述
     */
    private String desc;
}
