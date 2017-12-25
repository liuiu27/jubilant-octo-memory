package com.cupdata.voucher.constant;

import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.voucher.util.obtainvoucher.BaseVoucherObtainUtil;
import com.cupdata.voucher.util.obtainvoucher.TrvokVoucherObtainUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description: 获取券码工具Map
 * @Date: 14:22 2017/12/22
 */
public class VoucherObtainUtilMap {
    /**
     * 日志
     */
    protected static Logger log = Logger.getLogger(VoucherObtainUtilMap.class);

    private static Map<String, Class<? extends BaseVoucherObtainUtil>> UTIL_MAP = new HashMap<String, Class<? extends BaseVoucherObtainUtil>>();

    static{
        UTIL_MAP.put(getKey(ModelConstants.SUPPLIER_FLAG_TRVOK), TrvokVoucherObtainUtil.class);//空港易行券码
    }

    public static Class<? extends BaseVoucherObtainUtil> getNotifyUtilClass(ServiceOrder order){
        if(null == UTIL_MAP || !UTIL_MAP.containsKey(getKey(order.getSupplierFlag()))){
            log.info("根据key值" + getKey(order.getSupplierFlag()) + "，从获取供应商券码方法map（UTIL_MAP）中获取的执行方法类为NULL");
            return null;
        }

        return UTIL_MAP.get(getKey(order.getSupplierFlag()));
    }

    /**
     * 获取key
     * @param supplierFlag 商户标识
     * @return
     */
    private static String getKey(String supplierFlag){
        return supplierFlag;
    }
}
