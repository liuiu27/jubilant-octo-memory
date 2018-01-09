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
    private Long orderId;

    /**
     * 服务产品编号
     */
    private String productNo;

    /**
     * 券码号
     */
    private String voucherCode;

    /**
     * 卡密
     */
    private String voucherPassword;

    /**
     * 此字段用于保存卡号的二维码链接
     */
    private String qrCodeUrl;
    
    /**
     * 0:未使用 1:已使用
     */
    private Character useStatus;
    
    /**
     * 0:有效  1:禁用
     */
    private Character effStatus;
    
    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;
}
