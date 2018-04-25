package com.cupdata.sip.common.api.recharge.response;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description: 虚拟充值结果查询vo
 * @@Date: Created in 11:31 2018/4/18
 */
@Data
public class RechargeResQuery {

    /**
     * 平台订单
     */
    private String orderNo;

    /**
     * 机构唯一订单号
     */
    private String orgOrderNo;

    /**
     * 服务产品编号
     */
    private String productNo;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 充值账号
     */
    private String account;

    /**
     * 订单描述
     */
    private String orderDesc;

    /**
     * 充值状态
     */
    private String rechargeStatus;
}

