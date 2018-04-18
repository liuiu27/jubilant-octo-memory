package com.cupdata.sip.common.api.tencent.request;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description: QQ会员充值鉴权接口请求参数
 * @@Date: Created in 13:47 2018/4/18
 */
@Data
public class QQCheckOpenReq {

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
     */
    private String sign;
}
