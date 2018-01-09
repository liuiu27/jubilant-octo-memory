package com.cupdata.commons.vo.voucher;

import com.cupdata.commons.vo.BaseRequest;
import lombok.Data;

/**
 * @author liwei
 * @Description:
 * @Date: 14:39 2017/12/29
 */

@Data
public class DisableVoucherReq extends BaseRequest {
    /**
     *机构订单唯一性标识
     */
    private String orgOrderNo;
    
    /**
     * 服务产品编号
     */
    private String productNo;
    
    /**
     * 券码
     */
    private String voucherCode;
    
    /**
     * 禁用描述
     */
    private String disableDesc;
}
