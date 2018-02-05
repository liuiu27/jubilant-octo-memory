package com.cupdata.commons.vo.recharge;

import lombok.Data;

/**
 * @Description: 创建充值订单参数vo
 * @Author: Dcein
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
}

