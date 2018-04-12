package com.cupdata.order.util;

import com.cupdata.commons.vo.content.CreateContentOrderVo;
import com.cupdata.commons.vo.content.ServiceOrderContent;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.ServiceOrder;
import com.cupdata.sip.common.dao.entity.ServiceOrderRecharge;
import com.cupdata.sip.common.dao.entity.ServiceOrderVoucher;
import com.cupdata.sip.common.lang.CommonUtils;
import com.cupdata.sip.common.lang.DateTimeUtil;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 16:22 2017/12/21
 */
public class OrderUtils {

    /**
     * 初始化订单
     *
     * @param orgNo          机构编号
     * @param OrgOrderNo     机构订单编号
     * @param orderDesc      订单描述
     * @param product        服务商品
     * @param orgProductRela 机构、商品关系记录
     * @return
     */
    public static ServiceOrder initServiceOrder(String supplierFlag, String orgNo, String OrgOrderNo, String orderDesc, ProductInfoVo product, OrgProductRelVo orgProductRela) {
        ServiceOrder order = new ServiceOrder();
        order.setOrgNo(orgNo);
        order.setOrderSubType(product.getProductSubType());
        order.setSupplierNo(product.getSupplierNo());
        order.setOrderNo(generateOrderNo());
        order.setOrgOrderNo(OrgOrderNo);
        order.setSupplierOrderNo(null);//供应商订单号
        order.setOrgPrice(orgProductRela.getOrgPrice());
        order.setSupplierPrice(product.getSupplierPrice());
        order.setSettleDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd"));
        order.setOrderStatus(ModelConstants.ORDER_STATUS_INITIAL.toString());
        order.setOrderType(product.getProductType());
        order.setOrderDesc(orderDesc);
        order.setOrderFailDesc(null);
        order.setSupplierFlag(supplierFlag);
        order.setNodeName(CommonUtils.getHostAddress() + ":" + ServerPort.getPort());
        if (ModelConstants.PRODUCT_TYPE_VOUCHER.equals(product.getProductType())) {//如果是券码商品
            if (StringUtils.isBlank(order.getNotifyUrl())) {
                order.setIsNotify(String.valueOf(ModelConstants.IS_NOTIFY_NO));
            } else {
                order.setIsNotify(String.valueOf(ModelConstants.IS_NOTIFY_YES));
            }
        } else if (ModelConstants.PRODUCT_TYPE_RECHARGE.equals(product.getProductType())) {//如果是充值商品
            order.setIsNotify(String.valueOf(ModelConstants.IS_NOTIFY_YES));
        }
        order.setNotifyUrl(null);
        return order;
    }

    /**
     * 初始化券码订单
     *
     * @param order     主订单
     * @param productNo 商品编号
     * @return
     */
    public static ServiceOrderVoucher initVoucherOrder(ServiceOrder order, String productNo) {
        ServiceOrderVoucher voucherOrder = new ServiceOrderVoucher();
        voucherOrder.setOrderId(order.getId());
        voucherOrder.setProductNo(productNo);
        voucherOrder.setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
        voucherOrder.setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
        return voucherOrder;
    }

    /**
     * 初始化内容引入订单
     *
     * @param order
     * @param createContentOrderVo
     * @return
     */
    public static ServiceOrderContent initContentOrder(ServiceOrder order, CreateContentOrderVo createContentOrderVo) {
        ServiceOrderContent orderContent = new ServiceOrderContent();
        orderContent.setOrderId(order.getId());
        orderContent.setProductNo(createContentOrderVo.getProductNo());
        orderContent.setOrgNo(createContentOrderVo.getOrgNo());
        //TODO  获取供应商编号
        orderContent.setSupNo("");
        orderContent.setMobileNo(createContentOrderVo.getContentJumpReq().getMobileNo());
        orderContent.setUserId(createContentOrderVo.getContentJumpReq().getUserId());
        orderContent.setUserName(createContentOrderVo.getContentJumpReq().getUserName());
        //供应商订单号


        return null;
    }

    /**
     * 初始化充值订单
     *
     * @param order     主订单
     * @param productNo 商品编号
     * @return
     */
    public static ServiceOrderRecharge initRechargeOrder(String accountNumber, ProductInfVo productInfVo, ServiceOrder order, String productNo) {
        ServiceOrderRecharge rechargeOrder = new ServiceOrderRecharge();
        rechargeOrder.setOrderId(order.getId());      //订单id
        rechargeOrder.setAccountNumber(accountNumber);//充值账号
        rechargeOrder.setProductNo(productNo);        //产品编号
        rechargeOrder.setRechargeAmt(productInfVo.getProduct().getRechargeAmt()); //充值金额
        rechargeOrder.setOpenDuration(productInfVo.getProduct().getRechargeDuration());//开通时长
        rechargeOrder.setRechargeTraffic(productInfVo.getProduct().getRechargeTraffic());//充值流量
        rechargeOrder.setRechargeNumber(productInfVo.getProduct().getRechargeNumber());//充值数量
        return rechargeOrder;

    }


    /**
     * 生成订单编号
     *
     * @return
     */
    private static String generateOrderNo() {
        return DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyMMddHHmmss") + CommonUtils.getRandomNum(8);
    }


}
