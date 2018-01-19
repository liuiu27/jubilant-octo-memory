package com.cupdata.trvok.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.trvok.ITrvokController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.trvok.TrovkCodeReq;
import com.cupdata.commons.vo.trvok.TrovkCodeRes;
import com.cupdata.commons.vo.trvok.TrovkDisableReq;
import com.cupdata.commons.vo.trvok.TrovkDisableRes;
import com.cupdata.commons.vo.trvok.TrvokAirportReq;
import com.cupdata.commons.vo.trvok.TrvokAirportRes;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.trvok.TrvokAreaRes;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;
import com.cupdata.commons.vo.voucher.DisableVoucherRes;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.commons.vo.voucher.WriteOffVoucherReq;
import com.cupdata.commons.vo.voucher.WriteOffVoucherRes;
import com.cupdata.trvok.feign.CacheFeignClient;
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
//
//    @Autowired
//    private RestTemplate restTemplate;
	
	@Autowired 
	private OrderFeignClient orderFeignClient;
	
	@Autowired 
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private CacheFeignClient cacheFeignClient;
	
    private static String TRVOK_AREA_SIGN_KEY = "TRVOK_AREA_SIGN_KEY";//获取空港区域秘钥
 	private static String TRVOK_REQUST_URL = "TRVOK_REQUST_URL";//空港请求区域信息URL
 	private static String TRVOK_IMG_URL = "TRVOK_IMG_URL"; //图片路径
 	private static String TRVOK_PARTNER = "TRVOK_PARTNER";	//合作方ID
 	private static String TRVOK_WCF_SIGN_KEY = "TRVOK_WCF_SIGN_KEY";//WC秘钥
	
	/**
	 * 获取空港区域信息
	 */
    @Override
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq) {
    	log.info("TrvokController getTrvokArea begin.............param areaType is " + areaReq.getAreaType());
    	areaReq.setAreaSignKey(cacheFeignClient.getSysConfig("CUPD", TRVOK_AREA_SIGN_KEY));
    	areaReq.setRequstUrl(cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL));
    	BaseResponse<TrvokAreaRes>  baseResponse = tripService.getTrvokArea(areaReq);
        return baseResponse;
    }
    
    /**
     * 获取空港机场列表
     */
	@Override
	public BaseResponse<TrvokAirportRes> getTrvokAirportInfo(@RequestBody TrvokAirportReq trvokAirportReq) {
		log.info("TrvokController getTrvokAirportInfo begin.............param areaType is " + trvokAirportReq.getAreaType() + " airportId is " + trvokAirportReq.getAirportId());
		trvokAirportReq.setAreaSignKey(cacheFeignClient.getSysConfig("CUPD", TRVOK_AREA_SIGN_KEY));
		trvokAirportReq.setRequstUrl(cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL));
		trvokAirportReq.setImgUrl(cacheFeignClient.getSysConfig("CUPD", TRVOK_IMG_URL));
		BaseResponse<TrvokAirportRes> baseResponse = tripService.getTrvokAirportInfo(trvokAirportReq);
		return baseResponse;
	}
	
	

	/**
	 * 创建订单   空港获取券码  激活券码
	 */
	@Override
	public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value="org", required=true) String org,@RequestBody GetVoucherReq voucherReq, HttpServletRequest request,
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
			//TODO  负载均衡发送信息
			//创建券码订单
			CreateVoucherOrderVo createVoucherOrderVo = new CreateVoucherOrderVo();
			createVoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
			createVoucherOrderVo.setOrgNo(org);
			createVoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
			createVoucherOrderVo.setProductNo(voucherReq.getProductNo());
	        BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createVoucherOrderVo);
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        } 
			TrovkCodeReq trovkCodeReq = new TrovkCodeReq();
			trovkCodeReq.setSku(sku);
			trovkCodeReq.setExpire(voucherReq.getExpire());
			trovkCodeReq.setOutTradeNo(voucherOrderRes.getData().getOrder().getOrderNo());
			//从缓存中获取秘钥及请求URL
			trovkCodeReq.setPartner(cacheFeignClient.getSysConfig("CUPD", TRVOK_PARTNER));
			trovkCodeReq.setWcfSignKey(cacheFeignClient.getSysConfig("CUPD", TRVOK_WCF_SIGN_KEY));
			trovkCodeReq.setRequstUrl(cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL));
			// 获取券码
			BaseResponse<TrovkCodeRes> baseResponse = tripService.getTrvokVerifyCode(trovkCodeReq);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())) {
				res.setResponseCode(baseResponse.getResponseCode());
				res.setResponseMsg(baseResponse.getResponseMsg());
				return res;
			}
			
			//修改订单状态 保存券码
			voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
			voucherOrderRes.getData().getVoucherOrder().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
			voucherOrderRes.getData().getVoucherOrder().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
			voucherOrderRes.getData().getVoucherOrder().setVoucherCode(baseResponse.getData().getVerifyCode());
			voucherOrderRes.getData().getVoucherOrder().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd"));
			voucherOrderRes.getData().getVoucherOrder().setEndDate(voucherReq.getExpire());
			voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        } 
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
	public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value="org", required=true) String org, @RequestBody DisableVoucherReq disableVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("TrvokController disableVoucher begin.............");
		BaseResponse<DisableVoucherRes> res = new BaseResponse<>();
		try {
			//调用空港接口禁用券码
			TrovkDisableReq trovkDisableReq = new TrovkDisableReq();
			trovkDisableReq.setPartner(cacheFeignClient.getSysConfig("CUPD", TRVOK_PARTNER));
			trovkDisableReq.setWcfSignKey(cacheFeignClient.getSysConfig("CUPD", TRVOK_WCF_SIGN_KEY));
			trovkDisableReq.setRequstUrl(cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL));
			trovkDisableReq.setVerifyCode(disableVoucherReq.getVoucherCode());
			BaseResponse<TrovkDisableRes> baseResponse = tripService.disableCode(trovkDisableReq);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())) {
				res.setResponseCode(baseResponse.getResponseCode());
				res.setResponseMsg(baseResponse.getResponseMsg());
				return res;
			}
			return res;
		}catch(Exception e){
			log.error("getVoucher error is" + e.getMessage());
			res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return res;
		}
	}
	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher( String sup,WriteOffVoucherReq writeOffVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	
}
