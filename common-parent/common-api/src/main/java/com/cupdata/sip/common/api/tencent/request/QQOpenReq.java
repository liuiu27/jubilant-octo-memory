package com.cupdata.sip.common.api.tencent.request;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description: QQ会员开通请求参数类
 * @@Date: Created in 13:47 2018/4/18
 */
@Data
public class QQOpenReq {

    /**
     * 手机号码
     */
    private String servernum;

    /**
     * 腾讯产品ID
     */
    private String serviceid;

    /**
     * 用户QQ号
     */
    private String uin;

    /**
     * 开通服务时长
     * 单位为月： 1表示开通1个月服务
     */
    private String amount;

    /**
     * 腾讯自定义字段
     * 当错误码为0时有值，在开通请求消息中需要透传字段。
     */
    private String txparam;

    /**
     * 扣费金额
     * 单位为分；只在开通时有效
     */
    private String price;

    /**
     * 命令字
     * 1：开通
     */
    private String command;

    /**
     * 时间戳
     * 格式：YYYYMMDDHHMMSS
     */
    private String timestamp;

    /**
     * 渠道来源
     * 1：PC；2：wap专页；3：短厅
     */
    private String source;

    /**
     * 支付类型
     * 1： 积分；2： 电子券；3：话费；4：现金
     */
    private String paytype;

    /**
     * 签名
     * MD5(servernum+serviceid+uin+amount+txparam+price+command+timestamp+source+paytype+key)
     */
    private String sign;
}
