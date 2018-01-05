package com.cupdata.trvok.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.trvok.ITrvokController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.trvok.TrovkActivatReq;
import com.cupdata.commons.vo.trvok.TrovkActivatRes;
import com.cupdata.commons.vo.trvok.TrovkCodeReq;
import com.cupdata.commons.vo.trvok.TrovkCodeRes;
import com.cupdata.commons.vo.trvok.TrovkDisableReq;
import com.cupdata.commons.vo.trvok.TrovkDisableRes;
import com.cupdata.commons.vo.trvok.TrvokAirportReq;
import com.cupdata.commons.vo.trvok.TrvokAirportRes;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.trvok.TrvokAreaRes;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;
import com.cupdata.commons.vo.voucher.DisableVoucherRes;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.commons.vo.voucher.WriteOffVoucherReq;
import com.cupdata.commons.vo.voucher.WriteOffVoucherRes;
import com.cupdata.trvok.feign.OrderFeignClient;
import com.cupdata.trvok.feign.ProductFeignClient;
import com.cupdata.trvok.service.TripService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:空港易行相关的服务
 * @Date: 19:13 2017/12/19
 */
@Slf4j
@RestController
public class TrvokController implements ITrvokController{
	
	@Autowired 
	private TripService tripService;
	
	@Autowired 
	private OrderFeignClient orderFeignClient;
	
	@Autowired 
	private ProductFeignClient productFeignClient;
	/**
	 * 获取空港区域信息
	 */
    @Override
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq) {
    	log.info("TrvokController getTrvokArea begin.............param areaType is " + areaReq.getAreaType());
    	BaseResponse<TrvokAreaRes>  baseResponse = tripService.getTrvokArea(areaReq);
        return baseResponse;
    }
    
    /**
     * 获取空港机场列表
     */
	@Override
	public BaseResponse<TrvokAirportRes> getTrvokAirportInfo(@RequestBody TrvokAirportReq trvokAirportReq) {
		log.info("TrvokController getTrvokAirportInfo begin.............param areaType is " + trvokAirportReq.getAreaType() + " airportId is " + trvokAirportReq.getAirportId());
		BaseResponse<TrvokAirportRes> baseResponse = tripService.getTrvokAirportInfo(trvokAirportReq);
		return baseResponse;
	}
	
	/**
	 * 空港获取券码  激活券码
	 */
	@Override
	public BaseResponse<TrovkCodeRes> getTrvokVerifyCode(TrovkCodeReq trovkCodeReq) {
		log.info("TrvokController getTrvokVerifyCode begin.............param outTradeNo is " + trovkCodeReq.getOutTradeNo() + " Sku is " + trovkCodeReq.getSku());
		BaseResponse<TrovkCodeRes> baseResponse = tripService.getTrvokVerifyCode(trovkCodeReq);
		return baseResponse;
	}
	
	/**
	 * 空港激活券码
	 */
	@Override
	public BaseResponse<TrovkActivatRes> activationTrvokCode(TrovkActivatReq trovkActivatReq) {
		log.info("TrvokController activationTrvokCode begin.............param verifyCode is " + trovkActivatReq.getVerifyCode() + " expire is " + trovkActivatReq.getExpire());
		BaseResponse<TrovkActivatRes> baseResponse = tripService.activationTrvokCode(trovkActivatReq);
		return baseResponse;
	}
	
	/**
	 * 空港禁用券码
	 */
	@Override
	public BaseResponse<TrovkDisableRes> disableCode(TrovkDisableReq trovkDisableReq) {
		log.info("TrvokController disableCode begin.............param verifyCode is " + trovkDisableReq.getVerifyCode());
		BaseResponse<TrovkDisableRes> baseResponse = tripService.disableCode(trovkDisableReq);
		return baseResponse;
	}
	

	/**
	 * 创建订单   空港获取券码  激活券码
	 */
	@Override
	public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("TrvokController getVoucher begin.............org is " + org);
		BaseResponse<ProductInfVo> productInfo = new BaseResponse<>();
		BaseResponse<GetVoucherRes> res = new BaseResponse<>();
		try {
			//获取供应商产品
			productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
				res.setResponseCode(productInfo.getResponseCode());
				res.setResponseMsg(productInfo.getResponseMsg());
				return res;
			}
			String sku = productInfo.getData().getProduct().getSupplierParam();
			//创建券码订单
	        BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(org, voucherReq.getOrgOrderNo(), voucherReq.getOrderDesc(), voucherReq.getProductNo());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        }
			TrovkCodeReq trovkCodeReq = new TrovkCodeReq();
			trovkCodeReq.setSku(sku);
			trovkCodeReq.setExpire(voucherReq.getExpire());
			trovkCodeReq.setOutTradeNo(voucherOrderRes.getData().getOrder().getOrderNo());
			// 获取券码
			BaseResponse<TrovkCodeRes> baseResponse = tripService.getTrvokVerifyCode(trovkCodeReq);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())) {
				res.setResponseCode(baseResponse.getResponseCode());
				res.setResponseMsg(baseResponse.getResponseMsg());
				return res;
			}
			//TODO  修改订单状态 保存券码
			
			//响应参数
			GetVoucherRes voucherRes = new GetVoucherRes();
			voucherRes.setExpire(voucherReq.getExpire());
			voucherRes.setOrderNo(org);
			voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
			voucherRes.setVoucherCode(baseResponse.getData().getVerifyCode());
			res.setData(voucherRes);
			return res;
		}catch(Exception e){
			log.error("getVoucher error is" + e.getMessage());
			res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return res;
		}
	}

	@Override
	public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
