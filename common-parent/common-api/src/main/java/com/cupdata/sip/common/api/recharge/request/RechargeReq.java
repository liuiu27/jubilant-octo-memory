package com.cupdata.sip.common.api.recharge.request;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 10:32 2018/4/18
 */
@Data
public class RechargeReq {

    private String timestamp;


    /**
     *机构订单唯一性标识
     */
    private String orgOrderNo;

    /**
     * 服务产品编号
     */
    private String productNo;

    /**
     *订单描述
     */
    private String orderDesc;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     *充值账号
     */
    private String account;

    /**
     *游戏大区名称
     */
    private String gameRegion;

    /**
     * 游戏服务名称
     */
    private String gameServer;

    /**
     *服务异步通知地址
     */
    private String notifyUrl;

    /**
     * 券码类型
     */
    private Long category;

    /**
     * 充值话费
     */
    private Long rechargeAmt;

    /**
     * 充值流量
     */
    private Long rechargeTraffic;

    /**
     * 充值件数
     */
    private Long rechargeNumber;
}
