package com.cupdata.order.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.order.IOrderController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
import com.cupdata.order.biz.ServiceOrderBiz;
import com.cupdata.order.feign.ProductFeignClient;

@RestController
public class OrderController implements IOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private ProductFeignClient productFeignClient;

    @Resource
    private ServiceOrderBiz orderBiz;

    @Override
    public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody  CreateVoucherOrderVo createVoucherOrderVo) {
        BaseResponse<VoucherOrderVo> voucherOrderRes = new BaseResponse();

        //查询服务产品信息
        BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(createVoucherOrderVo.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode()) || null == productInfRes.getData()){//如果查询失败
            voucherOrderRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            voucherOrderRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return voucherOrderRes;
        }

        //查询机构、商品关系记录
        BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(createVoucherOrderVo.getOrgNo(), createVoucherOrderVo.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode()) || null == orgProductRelRes.getData()){//如果查询失败
            voucherOrderRes.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
            return voucherOrderRes;
        }

        ServiceOrderVoucher voucherOrder = orderBiz.createVoucherOrder(createVoucherOrderVo.getOrgNo(), createVoucherOrderVo.getOrgOrderNo(), createVoucherOrderVo.getOrderDesc(), productInfRes.getData().getProduct(), orgProductRelRes.getData().getOrgProductRela());
        if (null == voucherOrder){//创建券码订单失败
            voucherOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
            voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
            return voucherOrderRes;
        }
        ServiceOrder order = orderBiz.select(Integer.parseInt(voucherOrder.getOrderId().toString()));
        if (null == order){
            voucherOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
            voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
            return voucherOrderRes;
        }
        
        VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
        voucherOrderVo.setOrder(order);
        voucherOrderVo.setVoucherOrder(voucherOrder);
        voucherOrderRes.setData(voucherOrderVo);

        return voucherOrderRes;
    }

    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(@PathVariable String orgNo, @PathVariable String orgOrderNo) {
    	BaseResponse<VoucherOrderVo> res = new BaseResponse<>();
    	if(StringUtils.isBlank(orgNo)||StringUtils.isBlank(orgOrderNo)) {
    		res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
			res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
			return res;
    	}
    	res = orderBiz.getVoucherOrderByOrgNoAndOrgOrderNo(orgNo,orgOrderNo);
        return res;
    }

	@Override
	public BaseResponse<VoucherOrderVo> updateVoucherOrder(@RequestBody VoucherOrderVo voucherOrderVo) {
		BaseResponse<VoucherOrderVo> res = new BaseResponse();
		if(null == voucherOrderVo.getVoucherOrder() || null == voucherOrderVo.getOrder()){
			res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
			res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
			return res;
		}
		orderBiz.updateVoucherOrder(voucherOrderVo);
		res.setData(voucherOrderVo);
		return res;
	}
}
