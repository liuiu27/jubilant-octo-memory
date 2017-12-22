package com.cupdata.commons.vo.product;

import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.commons.vo.BaseData;
import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:29 2017/12/21
 */
@Data
public class VoucherOrderVo extends BaseData{
    /**
     * 服务主订单
     */
    private ServiceOrder order;

    /**
     * 券码订单
     */
    private ServiceOrderVoucher voucherOrder;
}
