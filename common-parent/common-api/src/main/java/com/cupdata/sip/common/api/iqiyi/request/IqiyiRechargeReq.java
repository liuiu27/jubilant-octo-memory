package com.cupdata.sip.common.api.iqiyi.request;

import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 爱奇艺充值请求vo
 * @CreateDate: 2018/2/6 14:52
 */
@Data
public class IqiyiRechargeReq {

    /**
     * 爱奇艺账号
     */
    private String userAccount;

    /**
     * 充值激活码
     */
    private String cardCode;

    /**
     * 充值商家商户号
     */
    private String partnerNo;

    /**
     * 签名
     */
    private String sign;

}
