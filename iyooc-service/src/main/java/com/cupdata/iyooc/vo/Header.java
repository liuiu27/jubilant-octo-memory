package com.cupdata.iyooc.vo;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 14:06 2018/4/24
 */
@Data
public class Header {

    /**
     * 	请求时间 (YYYYMMDDHHmmss)	*	必填
     */
    private String reqTime;

    /**
     * 请求流水号					* 必填
     */
    private String reqSeqNo;

    /**
     * Mac值							* 必填
     */
    private String mac;

    /**
     * 响应报文时间必填 			* 请求报文中可不填(YYYYMMDDHHmmss)
     */
    private String respTime;

    /**
     * 请求渠道						* 请求报文中必填
     */
    private String reqChannel;

    /**
     * 用户token					* 绑定车辆，解绑车辆，查询停车费，缴费通知必填项
     */
    private String tokenId;

    /**
     *	版本号							* 固定填 1.0
     */
    private String version;

    /**
     * 备用字段1
     */
    private String rsv1;

    /**
     * 备用字段2
     */
    private String rsv2;

    /**
     * 备用字段3
     */
    private String rsv3;
}
