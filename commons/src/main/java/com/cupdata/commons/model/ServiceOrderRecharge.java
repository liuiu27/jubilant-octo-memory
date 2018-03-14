package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description: 充值产品订单实体类
 * @Date: 21:16 2017/12/20
 */
@Data
public class ServiceOrderRecharge extends BaseModel{
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
}
