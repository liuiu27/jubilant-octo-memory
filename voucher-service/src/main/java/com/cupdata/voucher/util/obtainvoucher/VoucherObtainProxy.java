package com.cupdata.voucher.util.obtainvoucher;

import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.voucher.constant.VoucherObtainUtilMap;
import org.apache.log4j.Logger;

/**
 * @Auth: LinYong
 * @Description: 获取券码代理类
 * @Date: 14:15 2017/12/22
 */
public class VoucherObtainProxy {
    protected static Logger log = Logger.getLogger(VoucherObtainProxy.class);

    /**
     * 执行方法
     */
    private BaseVoucherObtainUtil obtainVoucherUtil;

    public VoucherObtainProxy(ServiceOrder order, ServiceOrderVoucher voucherOrder){
        try {
            obtainVoucherUtil = (BaseVoucherObtainUtil)Class.forName(VoucherObtainUtilMap.getNotifyUtilClass(order).getName()).newInstance();
        } catch (InstantiationException e) {
            log.error("", e);
        } catch (IllegalAccessException e) {
            log.error("", e);
        } catch (ClassNotFoundException e) {
            log.error("", e);
        }
    }

    public void execute(ServiceOrder order, ServiceOrderVoucher voucherOrder){
        if(null != order && null != voucherOrder){
            log.info("服务整合平台券码服务中，执行获取供应商券码，主订单编号：" + order.getOrderNo());
            obtainVoucherUtil.execute(order, voucherOrder);
        }else{
            log.info("服务整合平台券码服务中，订单记录或者券码订单记录为空，忽略后续逻辑");
        }
    }
}
