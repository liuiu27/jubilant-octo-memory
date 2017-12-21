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
     *
     */
    private Long orderId;

    /**
     *
     */
    private String productNo;

    /**
     *
     */
    private String accountNumber;

    /**
     *
     */
    private Long openDuration;

    /**
     *
     */
    private Long rechargeAmt;

    /**
     *
     */
    private Long rechargeTraffic;

    /**
     *
     */
    private Long rechargeNumber;

    /**
     *
     */
    private String gameRegion;

    /**
     *
     */
    private String gameService;
}
