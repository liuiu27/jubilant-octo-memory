package com.cupdata.sip.common.api.lakala.request;

import lombok.Data;

/**
 * @author LinYong
 * @Description: 创建券码订单参数Vo
 * @create 2018-01-08 10:16
 */
@Data
public class CreateVoucherOrderVo {
    /**
     * 机构编号
     */
    private String orgNo;

    /**
     * 机构订单号
     */
    private String orgOrderNo;

    /**
     * 券码产品编号
     */
    private String productNo;

    /**
     * 订单描述
     */
    private String orderDesc;
}
