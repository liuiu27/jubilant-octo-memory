package com.cupdata.voucher.util.obtainvoucher;

import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderVoucher;
import org.apache.log4j.Logger;

/**
 * @Auth: LinYong
 * @Description: 获取券码基本工具类
 * @Date: 14:16 2017/12/22
 */
public interface BaseVoucherObtainUtil {
    /**
     * 日志
     */
    public static Logger log = Logger.getLogger(BaseVoucherObtainUtil.class);

    /**
     * 业务执行逻辑
     * @param order 主订单
     * @param voucherOrder 券码订单
     */
    public void execute(ServiceOrder order, ServiceOrderVoucher voucherOrder);
}
