package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:券码产品订单实体类
 * @Date: 21:17 2017/12/20
 */
@Data
public class ServiceOrderVoucher extends BaseModel{
    /**
     * 主订单id
     */
    private String orderId;

    /**
     *
     */
    private String productNo;

    /**
     *
     */
    private String voucherCode;

    /**
     *
     */
    private String voucherPassword;

    /**
     *
     */
    private String qrCodeUrl;

    /**
     *
     */
    private String startDate;

    /**
     *
     */
    private String endDate;
}
