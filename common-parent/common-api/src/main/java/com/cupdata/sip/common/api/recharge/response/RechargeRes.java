package com.cupdata.sip.common.api.recharge.response;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description: 虚拟充值结果vo
 * @@Date: Created in 10:31 2018/4/18
 */
@Data
public class RechargeRes {

    /**
     *平台订单号
     */
    private String orderNo;

    /**
     * 充值状态
     */
    private String rechargeStatus;

}
