package com.cupdata.commons.vo.voucher;

import com.cupdata.commons.vo.BaseRequest;
import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:39 2017/12/19
 */

@Data
public class GetVoucherReq extends BaseRequest {
    /**
     *机构订单唯一性标识
     */
    private String orgOrderNo;

    /**
     * 服务产品编号
     */
    private String productNo;

    /**
     * 券码有效期（仅针对特定券码商品）
     * 格式为yyyyMMdd
     */
    private String expire;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     *订单描述
     */
    private String orderDesc;
}
