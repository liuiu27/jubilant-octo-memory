package com.cupdata.sip.common.api.lakala.request;

import lombok.Data;

/**
 * @author LinYong
 * @Description: 核销券码请求参数
 * @Date: 14:39 2017/12/29
 */

@Data
public class WriteOffVoucherReq{
    /**
     * 供应商订单唯一性标识
     */
    private String supplierOrderNo;

    /**
     * 券码号
     */
    private String voucherCode;

    /**
     * 使用者姓名
     */
    private String userName;

    /**
     * 使用者手机号
     */
    private String userMobileNo;

    /**
     * 使用时间
     * 格式：yyyyMMddHHmmss
     */
    private String useTime;

    /**
     * 使用地点
     */
    private String usePlace;

    /**
     * 核销描述
     */
    private String writeOffDesc;
}
