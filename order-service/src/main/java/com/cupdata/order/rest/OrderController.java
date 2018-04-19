package com.cupdata.order.rest;

import com.cupdata.order.biz.ServiceOrderBiz;
import com.cupdata.order.biz.ServiceOrderRechargeBiz;
import com.cupdata.order.biz.ServiceOrderVoucherBiz;
import com.cupdata.order.feign.ProductFeignClient;
import com.cupdata.order.feign.SupplierFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.IOrderController;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.ErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: DingCong
 * @Description: 订单Controller
 * @@Date: Created in 15:12 2018/4/17
 */
@Slf4j
@RestController
public class OrderController implements IOrderController {

    @Resource
    private SupplierFeignClient supplierClient;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ServiceOrderBiz orderBiz;
    
    @Resource
    private ServiceOrderVoucherBiz orderVoucherBiz;
    
    @Resource
    private ServiceOrderRechargeBiz orderRechargeBiz;

    /**
     * 创建券码订单
     * @param createVoucherOrderVo 创建券码订单参数vo
     * @return
     */
    @Override
    public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody CreateVoucherOrderVo createVoucherOrderVo) {
        log.info("创建订单Controller,ProductNo:"+createVoucherOrderVo.getProductNo()+",OrgNo:"+createVoucherOrderVo.getOrgNo()+",OrderDesc:"+createVoucherOrderVo.getOrderDesc());
        try {
            BaseResponse<VoucherOrderVo> voucherOrderRes = new BaseResponse();
            //根据产品编号,查询服务产品信息
            BaseResponse<ProductInfoVo> productInfRes = productFeignClient.findByProductNo(createVoucherOrderVo.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                    || null == productInfRes.getData()) {// 如果查询失败
                log.error("product-service findByProductNo is error ProductNo is " + createVoucherOrderVo.getProductNo());
                voucherOrderRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return voucherOrderRes;
            }

            //获取商户标识
            BaseResponse<SupplierInfVo>  SupplierInfVo = supplierClient.findSupByNo(productInfRes.getData().getSupplierNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(SupplierInfVo.getResponseCode())
                    || null == SupplierInfVo.getData()){
                voucherOrderRes.setResponseCode(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getMsg());
                return voucherOrderRes;
            }
            String supplierFlag = SupplierInfVo.getData().getSupplierFlag();

            // 查询机构、商品关系记录
            BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(createVoucherOrderVo.getOrgNo(),
                    createVoucherOrderVo.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                    || null == orgProductRelRes.getData()) {// 如果查询失败
                log.error("product-service findRel is error ProductNo is " + createVoucherOrderVo.getProductNo()
                        + "orgNo is" + createVoucherOrderVo.getOrgNo());
                voucherOrderRes.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                return voucherOrderRes;
            }
            //开始创建订单
            VoucherOrderVo voucherOrderVo = orderVoucherBiz.createVoucherOrder(supplierFlag , createVoucherOrderVo.getOrgNo(),
                    createVoucherOrderVo.getOrgOrderNo(), createVoucherOrderVo.getOrderDesc(), productInfRes.getData(), orgProductRelRes.getData());
            voucherOrderRes.setData(voucherOrderVo);
            return voucherOrderRes;
        } catch (Exception e) {
            log.error("error is " + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
    }
    
    /**
     * 更新券码订单
     */
    @Override
    public BaseResponse updateVoucherOrder(@RequestBody VoucherOrderVo voucherOrderVo) {
    	orderVoucherBiz.updateVoucherOrder(voucherOrderVo);
        return new BaseResponse();
    }
    
    /**
     * 根据订单号查询券码订单信息
     */
    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(String orderNo) {
    	BaseResponse<VoucherOrderVo> res = new BaseResponse<VoucherOrderVo>();
    	VoucherOrderVo  voucherOrderVo = orderVoucherBiz.getVoucherOrderByOrderNo(orderNo);
    	res.setData(voucherOrderVo);
        return res;
    }
    
    /**
     * 根据订单号查询充值订单信息
     */
    @Override
    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(String orderNo) {
    	BaseResponse<RechargeOrderVo> res = new BaseResponse<RechargeOrderVo>();
    	RechargeOrderVo rechargeOrderVo = orderRechargeBiz.getRechargeOrderByOrderNo(orderNo);
    	res.setData(rechargeOrderVo);
        return res;
    }
    
    /**
     * 根据机构号和机构订单号 查询 券码订单详情
     */
    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
    	BaseResponse<VoucherOrderVo> res = new BaseResponse<VoucherOrderVo>();
    	VoucherOrderVo  voucherOrderVo = orderVoucherBiz.getVoucherOrderByOrgNoAndOrgOrderNo(orgNo,orgOrderNo);
    	res.setData(voucherOrderVo);
        return res;
    }
    
    
    /**
     * 根据机构号和机构订单号 查询充值订单详情
     */
    @Override
    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
    	BaseResponse<RechargeOrderVo> res = new BaseResponse<RechargeOrderVo>();
    	RechargeOrderVo rechargeOrderVo = orderRechargeBiz.getRechargeOrderByOrgNoAndOrgOrderNo(orgNo,orgOrderNo);
    	res.setData(rechargeOrderVo);
        return res;
    }

    
    /**
     * 根据供应商 供应商订单号 券码 查询 券码订单详情
     */
    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(String sup, String supplierOrderNo, String voucherCode) {
    	BaseResponse<VoucherOrderVo> res = new BaseResponse<VoucherOrderVo>();
    	VoucherOrderVo  voucherOrderVo = orderVoucherBiz.getVoucherOrderByVoucher(sup,supplierOrderNo,voucherCode);
    	res.setData(voucherOrderVo);
        return res;
    }
    
    /**
     * 创建充值订单
     */
    @Override
    public BaseResponse<RechargeOrderVo> createRechargeOrder(CreateRechargeOrderVo createRechargeOrderVo) {
    	
    	 log.info("创建订单Controller,ProductNo:"+createRechargeOrderVo.getProductNo()+",OrgNo:"+createRechargeOrderVo.getOrgNo()+",OrderDesc:"+createRechargeOrderVo.getOrderDesc());
         try {
        	 BaseResponse<RechargeOrderVo> res = new BaseResponse<RechargeOrderVo>();
             //根据产品编号,查询服务产品信息
             BaseResponse<ProductInfoVo> productInfRes = productFeignClient.findByProductNo(createRechargeOrderVo.getProductNo());
             if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                     || null == productInfRes.getData()) {// 如果查询失败
                 log.error("product-service findByProductNo is error ProductNo is " + createRechargeOrderVo.getProductNo());
                 res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                 res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                 return res;
             }

             //获取商户标识
             BaseResponse<SupplierInfVo>  SupplierInfVo = supplierClient.findSupByNo(productInfRes.getData().getSupplierNo());
             if (!ResponseCodeMsg.SUCCESS.getCode().equals(SupplierInfVo.getResponseCode())
                     || null == SupplierInfVo.getData()){
            	 res.setResponseCode(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getCode());
            	 res.setResponseMsg(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getMsg());
                 return res;
             }
             String supplierFlag = SupplierInfVo.getData().getSupplierFlag();

             // 查询机构、商品关系记录
             BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(createRechargeOrderVo.getOrgNo(),
            		 createRechargeOrderVo.getProductNo());
             if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                     || null == orgProductRelRes.getData()) {// 如果查询失败
                 log.error("product-service findRel is error ProductNo is " + createRechargeOrderVo.getProductNo()
                         + "orgNo is" + createRechargeOrderVo.getOrgNo());
                 res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                 res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                 return res;
             }
             //开始创建订单
             RechargeOrderVo rechargeOrderVo = orderRechargeBiz.createRechargeOrder(createRechargeOrderVo.getAccountNumber() ,supplierFlag , createRechargeOrderVo.getOrgNo(),
            		 createRechargeOrderVo.getOrgOrderNo(), createRechargeOrderVo.getOrderDesc(), productInfRes.getData(), orgProductRelRes.getData());
             res.setData(rechargeOrderVo);
             return res;
         } catch (Exception e) {
             log.error("error is " + e.getMessage());
             throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
         }
    }
    
    /**
     * 更新充值订单
     */
    @Override
    public BaseResponse updateRechargeOrder(RechargeOrderVo rechargeOrderVo) {
    	orderRechargeBiz.updateRechargeOrder(rechargeOrderVo);
        return new BaseResponse();
    }
    
    /**
     * 更新主订单
     */
    @Override
    public BaseResponse updateServiceOrder(OrderInfoVo order) {
    	orderBiz.updateServiceOrder(order);
        return new BaseResponse();
    }

    @Override
    public BaseResponse<List<OrderInfoVo>> getServiceOrderListByParam(Character orderStatus, String orderSubType, String supplierFlag) {
        return null;
    }

    @Override
    public List<OrderInfoVo> selectMainOrderList(Character orderStatus, String supplierFlag, String orderSubType) {
        return null;
    }

    @Override
    public BaseResponse queryContentOrder() {
        return null;
    }

    @Override
    public BaseResponse createContentOrder() {
        return null;
    }

    @Override
    public BaseResponse updateContentOrder() {
        return null;
    }
}
