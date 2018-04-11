package com.cupdata.sip.common.api.order.request;

import lombok.Data;

/**
 * @Description: 创建充值订单参数vo
 * @Author: DingCong
 * @CreateDate: 2018/2/1 10:30
 */
@Data
public class CreateRechargeOrderVo {

    /**
     * 机构编号
     */
    private String orgNo;

    /**
     * 机构订单号
     */
    private String orgOrderNo;

    /**
     * 产品编号
     */
    private String productNo;

    /**
     * 订单描述
     */
    private String orderDesc;

    /**
     *游戏大区名称
     */
    private String gameRegion;

    /**
     * 游戏服务名称
     */
    private String gameServer;

    /**
     *充值账号
     */
    private String accountNumber;

}

