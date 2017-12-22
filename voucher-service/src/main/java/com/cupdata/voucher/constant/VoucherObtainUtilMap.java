package com.cupdata.voucher.constant;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:22 2017/12/22
 */
public class VoucherObtainUtilMap {
    /**
     * 日志
     */
    protected static Logger log = Logger.getLogger(PayNotifyUtilMap.class);

    private static Map<String, Class<? extends BaseNotifyUtil>> UTIL_MAP = new HashMap<String, Class<? extends BaseNotifyUtil>>();

    static{
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_TENCENT, ModelConstants.ORDER_TYPE_SOCIAL), QQOpenNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_TENCENT, ModelConstants.ORDER_TYPE_MUSIC), QQOpenNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_TENCENT, ModelConstants.ORDER_TYPE_VIDEO), QQOpenNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IQIYI, ModelConstants.ORDER_TYPE_VIDEO), IqiyiOpenNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_RUOWEI, ModelConstants.ORDER_TYPE_TRAFFIC), TrafficRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_RUOWEI, ModelConstants.ORDER_TYPE_RECHARGE), PhoneRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IYOOC, ModelConstants.ORDER_TYPE_PARKING), ParkingPayNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_ECOMMALL, ModelConstants.ORDER_TYPE_ELECTRONIC_VOUCHER), VoucherExchangeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_TRAFFIC), IhuyiTrafficRechageNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_RECHARGE), IhuyiPhoneRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_ELECTRONIC_VOUCHER), IhuyiVoucherExchangeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_VIDEO), IhuyiVirtualRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_SOCIAL), IhuyiVirtualRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_MUSIC), IhuyiVirtualRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_GAME), IhuyiVirtualRechargeNotifyUtil.class);
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_IHUYI, ModelConstants.ORDER_TYPE_BOOK), IhuyiVirtualRechargeNotifyUtil.class);//互亿-阅读
        UTIL_MAP.put(getKey(ModelConstants.ORDER_MERCHANT_FLAG_LAKALA, ModelConstants.ORDER_TYPE_ELECTRONIC_VOUCHER), LakalaVoucherExchangeNotifyUtil.class);//拉卡拉券码
    }

    /**
     * 获取key
     * @param merchantFlag 商户标识
     * @param orderType 订单类型
     * @return
     */
    private static String getKey(String merchantFlag, Character orderType){
        return merchantFlag + "_" + String.valueOf(orderType);
    }

    public static Class<? extends BaseNotifyUtil> getNotifyUtilClass(UppMainOrder order){
        if(null == UTIL_MAP || !UTIL_MAP.containsKey(getKey(order.getMerchantFlag(), order.getOrderType()))){
            log.info("根据key值" + getKey(order.getMerchantFlag(), order.getOrderType()) + "，从异步通知方法map（UTIL_MAP）中获取的执行方法类为NULL");
            return null;
        }

        return UTIL_MAP.get(getKey(order.getMerchantFlag(), order.getOrderType()));
    }
}
