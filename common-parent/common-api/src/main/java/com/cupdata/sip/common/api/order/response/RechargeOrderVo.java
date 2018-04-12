package com.cupdata.sip.common.api.order.response;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/11
 */
@Data
public class RechargeOrderVo {

    /**
     * 主订单ID
     */
    private Long orderId;

    /**
     * 服务产品编号
     */
    private String productNo;

    /**
     * 充值账号
     */
    private String accountNumber;

    /**
     * 开通时长
     */
    private Long openDuration;

    /**
     * 充值话费
     */
    private Long rechargeAmt;

    /**
     * 充值流量
     */
    private Long rechargeTraffic;

    /**
     * 充值话费
     */
    private Long rechargeNumber;

    /**
     * 游戏大区
     */
    private String gameRegion;

    /**
     * 游戏服务器名称
     */
    private String gameServer;

    /**
     *
     * 主订单信息
     */
    private OrderInfoVo orderInfoVo;

}
