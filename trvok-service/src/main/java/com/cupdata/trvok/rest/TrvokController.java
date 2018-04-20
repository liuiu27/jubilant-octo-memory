package com.cupdata.trvok.rest;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.config.response.SysConfigVO;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.trvok.ITrvokController;
import com.cupdata.sip.common.api.trvok.request.TrovkCodeReq;
import com.cupdata.sip.common.api.trvok.request.TrovkDisableReq;
import com.cupdata.sip.common.api.trvok.request.TrvokAirportReq;
import com.cupdata.sip.common.api.trvok.request.TrvokAreaReq;
import com.cupdata.sip.common.api.trvok.response.TrovkCodeRes;
import com.cupdata.sip.common.api.trvok.response.TrovkDisableRes;
import com.cupdata.sip.common.api.trvok.response.TrvokAirportRes;
import com.cupdata.sip.common.api.trvok.response.TrvokAreaRes;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.ErrorException;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.trvok.feign.ConfigFeignClient;
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
	private ConfigFeignClient configFeignClient;
	
    private static String TRVOK_AREA_SIGN_KEY = "TRVOK_AREA_SIGN_KEY";//获取空港区域秘钥
 	private static String TRVOK_REQUST_URL = "TRVOK_REQUST_URL";//空港请求区域信息URL
 	private static String TRVOK_IMG_URL = "TRVOK_IMG_URL"; //图片路径
 	private static String TRVOK_PARTNER = "TRVOK_PARTNER";	//合作方ID
 	private static String TRVOK_WCF_SIGN_KEY = "TRVOK_WCF_SIGN_KEY";//WC秘钥
 	private static String TRVOK_VOUCHER_EXPIRE = "TRVOK_VOUCHER_EXPIRE";//券码有效期时间
 	
	
	/**
	 * 获取空港区域信息
	 */
    @Override
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq) {
    	log.info("TrvokController getTrvokArea begin.............param areaType is " + areaReq.getAreaType());
    	
    	BaseResponse<TrvokAreaRes>  res = new  BaseResponse<TrvokAreaRes>();
    	try {
//    		SysConfigVO sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_AREA_SIGN_KEY);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_AREA_SIGN_KEY);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient  TRVOK_AREA_SIGN_KEY  SUCCESS");
//	    	}
//	    	areaReq.setAreaSignKey(sysConfigVo.getData().getParaValue());
//	    	
//	    	sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient  TRVOK_REQUST_URL  SUCCESS");
//	    	}
//	    	
//	    	areaReq.setRequstUrl(sysConfigVo.getData().getParaValue());
	    	
//	    	res = tripService.getTrvokArea(areaReq);
    	}catch(Exception e){
			log.error("getTrvokArea error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
        return res;
    }
    
    /**
     * 获取空港机场列表
     */
	@Override
	public BaseResponse<TrvokAirportRes> getTrvokAirportInfo(@RequestBody TrvokAirportReq trvokAirportReq) {
		log.info("TrvokController getTrvokAirportInfo begin.............param areaType is " + trvokAirportReq.getAreaType() + " airportId is " + trvokAirportReq.getAirportId());
		BaseResponse<TrvokAirportRes> res = new  BaseResponse<TrvokAirportRes>();
		try {
//			BaseResponse<SysConfigVO> sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_AREA_SIGN_KEY);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_AREA_SIGN_KEY);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient  TRVOK_AREA_SIGN_KEY  SUCCESS");
//	    	}
//			trvokAirportReq.setAreaSignKey(sysConfigVo.getData().getParaValue());
//			
//			sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient  TRVOK_REQUST_URL  SUCCESS");
//	    	}
//			trvokAirportReq.setRequstUrl(sysConfigVo.getData().getParaValue());
//			
//			sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_IMG_URL);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_IMG_URL);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient  TRVOK_IMG_URL  SUCCESS");
//	    	}
//			trvokAirportReq.setImgUrl(sysConfigVo.getData().getParaValue());
//			
//			res = tripService.getTrvokAirportInfo(trvokAirportReq);
		}catch(Exception e){
			log.error("getTrvokAirportInfo error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
		return res;
	}
	
	

	/**
	 * 创建订单   空港获取券码  激活券码
	 */
	@Override
	public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value="org", required=true) String org,@RequestBody GetVoucherReq voucherReq) {
		log.info("TrvokController getVoucher begin.............org is " + org);
		BaseResponse<ProductInfoVo> productInfo = new BaseResponse<>();
		BaseResponse<GetVoucherRes> res = new BaseResponse<>();
		try {
			//获取供应商产品
			productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
				res.setResponseCode(productInfo.getResponseCode());
				res.setResponseMsg(productInfo.getResponseMsg());
				return res;
			}
			String sku = productInfo.getData().getSupplierParam();
			//创建券码订单
			CreateVoucherOrderVo createVoucherOrderVo = new CreateVoucherOrderVo();
			createVoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
			createVoucherOrderVo.setOrgNo(org);
			createVoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
			createVoucherOrderVo.setProductNo(voucherReq.getProductNo());
			createVoucherOrderVo.setNotifyUrl(voucherReq.getNotifyUrl());
			log.info("createVoucherOrderVo : "+ JSON.toJSONString(createVoucherOrderVo));
	        BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createVoucherOrderVo);
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrderInfoVo()){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        }else {
	    		log.info("orderFeignClient createVoucherOrder    SUCCESS");
	    	}
			TrovkCodeReq trovkCodeReq = new TrovkCodeReq();
			trovkCodeReq.setSku(sku);
			if(StringUtils.isBlank(voucherReq.getExpire())) {
				//从缓存中获取券码有效期时间
				SysConfigVO sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_VOUCHER_EXPIRE);
//		    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//		    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_VOUCHER_EXPIRE);
//		    		res.setResponseCode(sysConfigVo.getResponseCode());
//		    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//		    		return res;
//		    	}
		    	//获取当前时间  加上默认的有效期时间
		    	Date time = DateTimeUtil.addDay(DateTimeUtil.getCurrentTime(), Integer.valueOf(sysConfigVo.getParaValue()));
		    	voucherReq.setExpire(DateTimeUtil.getFormatDate(time, "yyyyMMdd"));
			}
			trovkCodeReq.setExpire(voucherReq.getExpire());
			trovkCodeReq.setOutTradeNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
			//从缓存中获取秘钥及请求URL
			SysConfigVO sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_PARTNER);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_PARTNER);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}
			trovkCodeReq.setPartner(sysConfigVo.getParaValue());
			
			sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_WCF_SIGN_KEY);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_WCF_SIGN_KEY);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}
			trovkCodeReq.setWcfSignKey(sysConfigVo.getParaValue());
			
			sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}
			trovkCodeReq.setRequstUrl(sysConfigVo.getParaValue());
			
			// 获取券码
			BaseResponse<TrovkCodeRes> trovkCodeRes = tripService.getTrvokVerifyCode(trovkCodeReq);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(trovkCodeRes.getResponseCode())) {
				res.setResponseCode(trovkCodeRes.getResponseCode());
				res.setResponseMsg(trovkCodeRes.getResponseMsg());
				return res;
			}
			
			//修改订单状态 保存券码
			voucherOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
			if(StringUtils.isNoneBlank(voucherReq.getNotifyUrl())) {
				voucherOrderRes.getData().getOrderInfoVo().setNotifyUrl(voucherReq.getNotifyUrl());
				voucherOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_YES);
			}
			voucherOrderRes.getData().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
			voucherOrderRes.getData().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
			voucherOrderRes.getData().setVoucherCode(trovkCodeRes.getData().getVerifyCode());
			voucherOrderRes.getData().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd"));
			voucherOrderRes.getData().setEndDate(voucherReq.getExpire());
			BaseResponse baseResponse = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
	        if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())){
	            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
	            return res;
	        }else {
	    		log.info("orderFeignClient updateVoucherOrder    SUCCESS");
	    	} 
			//响应参数
			GetVoucherRes voucherRes = new GetVoucherRes();
			voucherRes.setExpire(voucherReq.getExpire());
			voucherRes.setOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
			voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
			voucherRes.setVoucherCode(trovkCodeRes.getData().getVerifyCode());
			voucherRes.setStartDate(voucherOrderRes.getData().getStartDate());
			res.setData(voucherRes);
		}catch(Exception e){
			log.error("getVoucher error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
		return res;
	}

	@Override
	public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value="org", required=true) String org, @RequestBody DisableVoucherReq disableVoucherReq) {
		log.info("TrvokController disableVoucher begin.............");
		BaseResponse<DisableVoucherRes> res = new BaseResponse<>();
//		try {
			//调用空港接口禁用券码
//			TrovkDisableReq trovkDisableReq = new TrovkDisableReq();
//			BaseResponse<SysConfigVO> sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_PARTNER);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_PARTNER);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient TRVOK_PARTNER    SUCCESS");
//	    	}
//	    	trovkDisableReq.setPartner(sysConfigVo.getData().getParaValue());
//	    	
//	    	sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_WCF_SIGN_KEY);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_WCF_SIGN_KEY);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient TRVOK_WCF_SIGN_KEY    SUCCESS");
//	    	}
//	    	trovkDisableReq.setWcfSignKey(sysConfigVo.getData().getParaValue());
//			
//	    	sysConfigVo = configFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
//	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
//	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
//	    		res.setResponseCode(sysConfigVo.getResponseCode());
//	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
//	    		return res;
//	    	}else {
//	    		log.info("cacheFeignClient TRVOK_REQUST_URL    SUCCESS");
//	    	}
//			trovkDisableReq.setRequstUrl(sysConfigVo.getData().getParaValue());
//			trovkDisableReq.setVerifyCode(disableVoucherReq.getVoucherCode());
//			BaseResponse<TrovkDisableRes> baseResponse = tripService.disableCode(trovkDisableReq);
//			if(!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())) {
//				res.setResponseCode(baseResponse.getResponseCode());
//				res.setResponseMsg(baseResponse.getResponseMsg());
//				return res;
//			}
//		}catch(Exception e){
//			log.error("disableVoucher error is" + e.getMessage());
//			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
//		}
		return res;
	}
	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher( String sup,WriteOffVoucherReq writeOffVoucherReq) {
		return null;
	}

	
}
