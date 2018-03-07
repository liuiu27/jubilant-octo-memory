package com.cupdata.commons.vo.voucher;

import com.cupdata.commons.vo.BaseData;
import lombok.Data;

/**
 * @Auth: LinYong
 * @Description: 获取券码响应数据
 * @Date: 14:41 2017/12/19
 */

@Data
public class GetVoucherRes extends BaseData {
    /**
     * 平台订单号
     */
    private String orderNo;

    /**
     * 机构订单唯一性标识
     */
    private String orgOrderNo;

    /**
     * 券码号
     */
    private String voucherCode;

    /**
     * 卡密
     */
    private String voucherPassword;

    /**
     * 二维码链接URL
     */
    private String qrCodeUrl;
    
    /**
     *  券码生效期
     *  时间格式：yyyyMMdd
     */
    private String startDate;

    /**
     * 券码有效期
     * 时间格式：yyyyMMdd
     */
    private String expire;

    /**
     * 券码列表id
     */
    private Long voucherLibId;
}
