package com.cupdata.order.util;

import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.model.OrgProductRela;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 16:22 2017/12/21
 */
public class OrderUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtils.class);

    /**
     * 初始化订单
     * @param orgNo 机构编号
     * @param OrgOrderNo 机构订单编号
     * @param orderDesc 订单描述
     * @param product 服务商品
     * @param orgProductRela 机构、商品关系记录
     * @return
     */
    public static ServiceOrder initServiceOrder(String orgNo, String OrgOrderNo, String orderDesc, ServiceProduct product, OrgProductRela orgProductRela){
        ServiceOrder order = new ServiceOrder();
        order.setOrgNo(orgNo);
        order.setSupplierNo(product.getSupplierNo());
        order.setOrderNo(generateOrderNo());
        order.setOrgOrderNo(OrgOrderNo);
        order.setSupplierOrderNo(null);//供应商订单号
        order.setOrgPrice(orgProductRela.getOrgPrice());
        order.setSupplierPrice(product.getSupplierPrice());
        order.setSettleDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd"));
        order.setOrderStatus(ModelConstants.ORDER_STATUS_INITIAL);
        order.setOrderType(product.getProductType());
        order.setOrderDesc(orderDesc);
        order.setOrderFailDesc(null);
        if (ModelConstants.PRODUCT_TYPE_VOUCHER.equals(product.getProductType())){//如果是券码商品
            order.setIsNotify(ModelConstants.IS_NOTIFY_NO);
        } else if(ModelConstants.PRODUCT_TYPE_RECHARGE.equals(product.getProductType())){//如果是充值商品
            order.setIsNotify(ModelConstants.IS_NOTIFY_YES);
        }
        order.setNotifyUrl(null);
        order.setNodeName(CommonUtils.getServerFlag());
        return order;
    }

    /**
     * 初始化券码订单
     * @param order 主订单
     * @param productNo 商品编号
     * @return
     */
    public static ServiceOrderVoucher initVoucherOrder(ServiceOrder order, String productNo){
        ServiceOrderVoucher voucherOrder = new ServiceOrderVoucher();
        voucherOrder.setOrderId(order.getId());
        voucherOrder.setProductNo(productNo);
        voucherOrder.setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
        voucherOrder.setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
        return voucherOrder;
    }

    /**
     * 生成订单编号
     * @return
     */
    public static String generateOrderNo(){
        return DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyMMddHHmmss") + CommonUtils.getRandomNum(8);
    }
}
