package com.cupdata.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.api.order.IOrderController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderRecharge;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;
import com.cupdata.commons.vo.order.ServiceOrderList;
import com.cupdata.commons.vo.orgsupplier.SupplierInfVo;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.recharge.CreateRechargeOrderVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
import com.cupdata.order.biz.ServiceOrderBiz;
import com.cupdata.order.biz.ServiceOrderContentBiz;
import com.cupdata.order.feign.ProductFeignClient;
import com.cupdata.order.feign.SupplierFeignClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OrderController implements IOrderController {

	@Autowired
	private ProductFeignClient productFeignClient;

	@Resource
	private ServiceOrderBiz orderBiz;

	@Autowired
	private ServiceOrderContentBiz contentBiz;

	@Autowired
	private SupplierFeignClient supplierClient;

	/**
	 * 根据订单编号获取主订单
	 * @param orderNo
	 * @return
	 */
	@Override
	public BaseResponse<ServiceOrder> getServiceOrderByOrderNo(@PathVariable String orderNo) {
	    log.info("根据订单编号获取主订单,订单编号:"+orderNo);
        BaseResponse<ServiceOrder> serviceOrderRes = new BaseResponse();
	    try {
	        if (StringUtils.isBlank(orderNo)){
	        	log.info("订单号为空");
                serviceOrderRes.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                serviceOrderRes.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return serviceOrderRes;
            }
            Map<String,Object> map = new  HashMap();
	        map.put("orderNo",orderNo);
            ServiceOrder order = orderBiz.selectSingle(map);
            log.info("查询主订单信息为:OrderNo:"+order.getOrderNo()+",OrderStatus:"+order.getOrderStatus()+",OrgOrderNo:"+order.getOrgOrderNo());
            serviceOrderRes.setData(order);
	    }catch (Exception e){
	        log.info("根据订单号查询主订单出现异常,异常信息:"+e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
		return serviceOrderRes;
	}

	@Override
	public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody CreateVoucherOrderVo createVoucherOrderVo) {
		log.info("OrderController createVoucherOrder is begin params is" + createVoucherOrderVo.toString());
		try {
			BaseResponse<VoucherOrderVo> voucherOrderRes = new BaseResponse();
			// 根据产品编号,查询服务产品信息
			BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(createVoucherOrderVo.getProductNo());
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
					|| null == productInfRes.getData()) {// 如果查询失败
				log.error("product-service findByProductNo is error ProductNo is " + createVoucherOrderVo.getProductNo());
				voucherOrderRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				voucherOrderRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
				return voucherOrderRes;
			}

			//获取商户标识
			BaseResponse<SupplierInfVo>  SupplierInfVo = supplierClient.findSupByNo(productInfRes.getData().getProduct().getSupplierNo());
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(SupplierInfVo.getResponseCode())
					|| null == SupplierInfVo.getData()){
				log.error("找不到商户标识,请在系统字典中配置商户标识");
				voucherOrderRes.setResponseCode(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getCode());
				voucherOrderRes.setResponseMsg(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getMsg());
				return voucherOrderRes;
			}
			String supplierFlag = SupplierInfVo.getData().getSuppliersInf().getSupplierFlag();

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
	
			ServiceOrderVoucher voucherOrder = orderBiz.createVoucherOrder(supplierFlag , createVoucherOrderVo.getOrgNo(),
					createVoucherOrderVo.getOrgOrderNo(), createVoucherOrderVo.getOrderDesc(),
					productInfRes.getData().getProduct(), orgProductRelRes.getData().getOrgProductRela());
			if (null == voucherOrder) {// 创建券码订单失败
				log.error("order-service createVoucherOrder is error findRel is " + createVoucherOrderVo.getProductNo()
				+ "orgNo is" + createVoucherOrderVo.getOrgNo());
				voucherOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
				voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
				return voucherOrderRes;
			}
			ServiceOrder order = orderBiz.select(Integer.parseInt(voucherOrder.getOrderId().toString()));
			if (null == order) {
				log.error("order-service select is error id is" + voucherOrder.getOrderId());
				voucherOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
				voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
				return voucherOrderRes;
			}
			VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
			voucherOrderVo.setOrder(order);
			voucherOrderVo.setVoucherOrder(voucherOrder);
			voucherOrderRes.setData(voucherOrderVo);
			return voucherOrderRes;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	@Override
	public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(@PathVariable String orgNo,
			@PathVariable String orgOrderNo) {
		log.info("OrderController getVoucherOrderByOrgNoAndOrgOrderNo is begin orgNo is" + orgNo + "orgOrderNo is" + orgOrderNo);
		try {
			BaseResponse<VoucherOrderVo> res = new BaseResponse<>();
			if (StringUtils.isBlank(orgNo) || StringUtils.isBlank(orgOrderNo)) {
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			res = orderBiz.getVoucherOrderByOrgNoAndOrgOrderNo(orgNo, orgOrderNo);
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}	
	}

	@Override
	public BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(@PathVariable String orgNo, @PathVariable String orgOrderNo) {
		log.info("OrderController getRechargeOrderByOrgAndOrgNo is begin orgNo is" + orgNo + "orgOrderNo is" + orgOrderNo) ;
		try{
		    BaseResponse<RechargeOrderVo> res = new BaseResponse<>();
		    if(StringUtils.isBlank(orgNo) || StringUtils.isBlank(orgOrderNo)){
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
		        res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
		        return res;
            }
            res = orderBiz.getRechargeOrderByOrgNoAndOrgOrderNo(orgNo,orgOrderNo);
		    return res;

        }catch(Exception e){
		    log.error("getRechargeOrderByOrgAndOrgNo error is" + e.getMessage());
		    throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
	}

	@Override
	public BaseResponse<VoucherOrderVo> updateVoucherOrder(@RequestBody VoucherOrderVo voucherOrderVo) {
		log.info("OrderController updateVoucherOrder is begin params is" +  voucherOrderVo.toString());
		try {
			BaseResponse<VoucherOrderVo> res = new BaseResponse();
			if (null == voucherOrderVo.getVoucherOrder() || null == voucherOrderVo.getOrder()) {
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			orderBiz.updateVoucherOrder(voucherOrderVo);
			res.setData(voucherOrderVo);
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	@Override
	public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(@PathVariable String sup,
			@PathVariable String supplierOrderNo, @PathVariable String voucherCode) {
		log.info("OrderController getVoucherOrderByOrgNoAndOrgOrderNo is begin sup is" + sup + "supplierOrderNo is" + supplierOrderNo
				+ "voucherCode is " + voucherCode);
		try {
			BaseResponse<VoucherOrderVo> res = new BaseResponse();
			if (StringUtils.isBlank(voucherCode) || StringUtils.isBlank(sup)) {
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			res = orderBiz.getVoucherOrderByVoucher(sup, supplierOrderNo, voucherCode);
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	
	}

	@Override
	public BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(@PathVariable String orderNo) {
        log.info("OrderController getRechargeOrderByOrderNo is begin orderNo is" + orderNo);
        try {
            BaseResponse<RechargeOrderVo> res = new BaseResponse<>();
            if (StringUtils.isBlank(orderNo)) {
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return res;
            }
            res = orderBiz.getRechargeOrderByOrderNo(orderNo);
            return res;

        }catch (Exception e){
            log.error("error is " + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());

        }
	}

	@Override
	public BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(@PathVariable String orderNo) {
		log.info("OrderController getVoucherOrderByOrderNo is begin orderNo is" + orderNo);
		try {
			BaseResponse<VoucherOrderVo> res = new BaseResponse<>();
			if (StringUtils.isBlank(orderNo)) {
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			res = orderBiz.getVoucherOrderByOrderNo(orderNo);
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	/**
     * 实现创建充值订单接口，用于创建充值订单
     * @param createRechargeOrderVo 创建充值订单参数vo
     * @return
     */
	@Override
	public BaseResponse<RechargeOrderVo> createRechargeOrder(@RequestBody CreateRechargeOrderVo createRechargeOrderVo) {
		log.info("OrderController createRechargeOrder is begin...OrgOrderNo:" + createRechargeOrderVo.getOrgOrderNo()+",ProductNo:"+createRechargeOrderVo.getProductNo());
		try {
			//设置响应信息
		    BaseResponse<RechargeOrderVo> rechargeOrderRes = new BaseResponse<RechargeOrderVo>();

		    //根据产品编号，查询服务产品信息
	        BaseResponse<ProductInfVo> productInfRes = productFeignClient
	                .findByProductNo(createRechargeOrderVo.getProductNo());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
	                || null == productInfRes.getData()) {// 如果查询失败
	            rechargeOrderRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
	            rechargeOrderRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
	            return rechargeOrderRes;
	        }
	        log.info("get productInfRes...ProductName:"+productInfRes.getData().getProduct().getProductName()+",ProductType:"+productInfRes.getData().getProduct().getProductType()+",ProductDesc:"+productInfRes.getData().getProduct().getProductDesc());

	        //获取商户标识
            BaseResponse<SupplierInfVo>  SupplierInfVo = supplierClient.findSupByNo(productInfRes.getData().getProduct().getSupplierNo());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(SupplierInfVo.getResponseCode())
					|| null == SupplierInfVo.getData()){
				log.error("找不到商户标识,请在系统字典中配置商户标识");
				rechargeOrderRes.setResponseCode(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getCode());
				rechargeOrderRes.setResponseMsg(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getMsg());
				return rechargeOrderRes;
			}
	        String supplierFlag = SupplierInfVo.getData().getSuppliersInf().getSupplierFlag();
            log.info("get supplierFlag from supplierInfVo:"+supplierFlag);

	        // 查询机构、商品关系记录
	        BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(createRechargeOrderVo.getOrgNo(),
	                createRechargeOrderVo.getProductNo());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
	                || null == orgProductRelRes.getData()) {// 如果查询失败
	            rechargeOrderRes.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
	            rechargeOrderRes.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
	            return rechargeOrderRes;
	        }
	
	        //根据订单业务来创建充值订单
	        ServiceOrderRecharge rechargeOrder = orderBiz.createRechargeOrder(createRechargeOrderVo.getAccountNumber(),productInfRes.getData(),supplierFlag,createRechargeOrderVo.getOrgNo(),
	                createRechargeOrderVo.getOrgOrderNo(), createRechargeOrderVo.getOrderDesc(),
	                productInfRes.getData().getProduct(), orgProductRelRes.getData().getOrgProductRela());
	        if (null == rechargeOrder) {// 创建券码订单失败
	            rechargeOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            rechargeOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return rechargeOrderRes;
	        }
	        ServiceOrder order = orderBiz.select(Integer.parseInt(rechargeOrder.getOrderId().toString()));
	        if (null == order) {
	            rechargeOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            rechargeOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return rechargeOrderRes;
	        }
	
	        //写入充值订单数据,给予返回
	        RechargeOrderVo rechargeOrderVo = new RechargeOrderVo();
	        rechargeOrderVo.setOrder(order);
	        rechargeOrderVo.setRechargeOrder(rechargeOrder);
	        rechargeOrderRes.setData(rechargeOrderVo);
	        return rechargeOrderRes;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	/**
	 * 更新充值订单
	 * @param rechargeOrderVo
	 * @return
	 */
	@Override
	public BaseResponse<RechargeOrderVo> updateRechargeOrder(@RequestBody RechargeOrderVo rechargeOrderVo) {
		log.info("OrderController updateRechargeOrder is begin params is" + rechargeOrderVo.toString());
	    try {
	    	//设置响应结果
		    BaseResponse<RechargeOrderVo>  res = new BaseResponse<RechargeOrderVo>();
		    //判断主订单和充值功能订单是否为空
	        if (null == rechargeOrderVo.getRechargeOrder() || null == rechargeOrderVo.getRechargeOrder()){
	            res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
	            return res;
	        }
	        orderBiz.updateRechargeOrder(rechargeOrderVo);
	        res.setData(rechargeOrderVo);
	        return res;
	    } catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}



	/**
	 * 内容引入订单查询
	 * @param contentQueryOrderReq
	 * @return
	 */
	@Override
	public BaseResponse<ContentQueryOrderRes> queryContentOrder(@RequestBody ContentQueryOrderReq contentQueryOrderReq) {
		log.info("OrderController queryContentOrder is begin params is" + contentQueryOrderReq.toString());
		BaseResponse<ContentQueryOrderRes> res = new BaseResponse<ContentQueryOrderRes>();
		try {
			//验证参数是否合法
			if(StringUtils.isBlank(contentQueryOrderReq.getSupOrderNo())) {
				log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			//查询内容引入子订单表
		 ContentQueryOrderRes contentQueryOrderRes = contentBiz.queryContentOrder(contentQueryOrderReq);
		 if(null == contentQueryOrderRes) {
			    log.error("queryContentOrder result is null.......  errorCode is " + ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
				res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
				res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
				return res;
		 }
		 res.setData(contentQueryOrderRes);
	     return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

    /**
     * 更新主订单
     * @param order
     */
    @Override
    public Integer updateServiceOrder(@RequestBody ServiceOrder order) {
        log.info("更新主订单...update serviceOrder");
        //修改订单表状态
        Integer i = orderBiz.updateServiceOrder(order);
        return i;
    }


    /**
	 * 根据参数查询主订单
	 * @param orderStatus
	 * @param orderSubType
	 * @param supplierFlag
	 * @return
	 */
	@Override
	public BaseResponse<ServiceOrderList> getServiceOrderListByParam(@PathVariable Character orderStatus, @PathVariable String orderSubType, @PathVariable String supplierFlag) {
		log.info("getServiceOrderListByParam controller is begin...,orderStatus"+orderStatus+",orderSubType"+orderSubType+",supplierFlag"+supplierFlag);
		try {
			//设置响应结果
			BaseResponse<ServiceOrderList> res = new BaseResponse<ServiceOrderList>();

			//判断参数是否为空
			if(Character.isWhitespace(orderStatus) || StringUtils.isBlank(orderSubType) || StringUtils.isBlank(supplierFlag)){
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				return res;
			}
			res = orderBiz.getServiceOrderListByParam(orderStatus,orderSubType,supplierFlag);
			return res ;
		}catch (Exception e){
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

    @Override
    public List<ServiceOrder> selectMainOrderList(@PathVariable Character orderStatus, @PathVariable String supplierFlag, @PathVariable String orderSubType) {
        List<String> typeList = JSONObject.parseArray(orderSubType,String.class);
        return orderBiz.selectMainOrderList(orderStatus,supplierFlag,typeList);
    }

    private static BaseResponse baseResponse;

	static {
		baseResponse = new BaseResponse();
		baseResponse.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		baseResponse.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	}

	/**
	 * 创建内容引入订单
	 */
	@Override
	public BaseResponse createContentOrder() {

		return baseResponse;
	}

	@Override
	public BaseResponse updateContentOrder() {

		return baseResponse;
	}


}
