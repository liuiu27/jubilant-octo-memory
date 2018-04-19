package com.cupdata.sip.common.api.tencent.response;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description: QQ会员开通鉴权接口
 * @@Date: Created in 13:48 2018/4/18
 */
@Data
public class QQCheckOpenRes {

    /**
     * 错误码
     */
    private String result;

    /**
     * 错误描述
     */
    private String desc;

    /**
     * 腾讯自定义字段
     * 当错误码为0时有值，在开通请求消息中需要透传字段。
     */
    private String txparam;

}
