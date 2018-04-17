package com.cupdata.shengda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cupdata.commons.api.shengda.IShengdaConller;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;
import com.cupdata.commons.vo.voucher.DisableVoucherRes;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.commons.vo.voucher.WriteOffVoucherReq;
import com.cupdata.commons.vo.voucher.WriteOffVoucherRes;
import com.cupdata.shengda.biz.ShengdaBiz;
import com.cupdata.shengda.feign.CacheFeignClient;
import com.cupdata.shengda.feign.OrderFeignClient;
import com.cupdata.shengda.feign.ProductFeignClient;
import com.cupdata.shengda.vo.ShengdaOrderRes;
import com.cupdata.shengda.vo.ShengdaWashingOrderReq;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月2日 下午2:08:36
*/
@Slf4j
@RestController
public class ShengdaConller implements IShengdaConller{
	
	@Autowired
	private ShengdaBiz shengdaBiz;
	
	@Autowired 
	private OrderFeignClient orderFeignClient;
	
	@Autowired 
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private CacheFeignClient cacheFeignClient;
	
	@Override
	public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq) {
		log.info("ShengdaConller getVoucher is begin params is " + voucherReq.toString());
		BaseResponse<GetVoucherRes> res = new BaseResponse<>();
		BaseResponse<ProductInfVo> productInfo = new BaseResponse<>();
		try {
			//获取供应商产品
			productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
				res.setResponseCode(productInfo.getResponseCode());
				res.setResponseMsg(productInfo.getResponseMsg());
				return res;
			}
			//创建券码订单
			CreateVoucherOrderVo createVoucherOrderVo = new CreateVoucherOrderVo();
			createVoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
			createVoucherOrderVo.setOrgNo(org);
			createVoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
			createVoucherOrderVo.setProductNo(voucherReq.getProductNo());
			log.info("createVoucherOrderVo : "+ JSON.toJSONString(createVoucherOrderVo));
	        BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createVoucherOrderVo);
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        }else {
	    		log.info("orderFeignClient createVoucherOrder    SUCCESS");
	    	}
	        ShengdaWashingOrderReq washingOrderReq = new ShengdaWashingOrderReq("WZYH", "WZYH", "223fdsfsf1232131", "03", "15857128524");
			//调用盛大创建订单接口
			ShengdaOrderRes shengdaRes = shengdaBiz.createSupplierOrder(washingOrderReq);
			System.out.println(shengdaRes.getResultCode() + "   " + shengdaRes.getOrder());
			//修改订单状态 保存券码
			voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
			voucherOrderRes.getData().getVoucherOrder().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
			voucherOrderRes.getData().getVoucherOrder().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
			//voucherOrderRes.getData().getVoucherOrder().setVoucherCode(baseResponse.getData().getVerifyCode());
			voucherOrderRes.getData().getVoucherOrder().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd"));
			voucherOrderRes.getData().getVoucherOrder().setEndDate(voucherReq.getExpire());
			voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        }else {
	    		log.info("orderFeignClient updateVoucherOrder    SUCCESS");
	    	} 
			//响应参数
			GetVoucherRes voucherRes = new GetVoucherRes();
			voucherRes.setExpire(voucherReq.getExpire());
			voucherRes.setOrderNo(voucherOrderRes.getData().getOrder().getOrderNo());
			voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
			//voucherRes.setVoucherCode(baseResponse.getData().getVerifyCode());
			voucherRes.setStartDate(voucherOrderRes.getData().getVoucherOrder().getStartDate());
			res.setData(voucherRes);
			return null;
		}catch (Exception e) {
			log.error("getVoucher error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	@Override
	public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
		// TODO Auto-generated method stub
		return null;
	}
		
}
