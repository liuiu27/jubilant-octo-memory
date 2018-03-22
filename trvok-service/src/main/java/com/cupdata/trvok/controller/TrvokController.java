package com.cupdata.trvok.controller;

import com.alibaba.fastjson.JSON;
import com.cupdata.commons.api.trvok.ITrvokController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.sysconfig.SysConfigVo;
import com.cupdata.commons.vo.trvok.*;
import com.cupdata.commons.vo.voucher.*;
import com.cupdata.trvok.feign.CacheFeignClient;
import com.cupdata.trvok.feign.OrderFeignClient;
import com.cupdata.trvok.feign.ProductFeignClient;
import com.cupdata.trvok.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
 	private static String TRVOK_VOUCHER_EXPIRE = "TRVOK_VOUCHER_EXPIRE";//券码有效期时间
 	
	
	/**
	 * 获取空港区域信息
	 */
    @Override
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq) {
    	log.info("TrvokController getTrvokArea begin.............param areaType is " + areaReq.getAreaType());
    	
    	BaseResponse<TrvokAreaRes>  res = new  BaseResponse<TrvokAreaRes>();
    	try {
	    	BaseResponse<SysConfigVo> sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_AREA_SIGN_KEY);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_AREA_SIGN_KEY);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
	    	areaReq.setAreaSignKey(sysConfigVo.getData().getSysConfig().getParaValue());
	    	
	    	sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
	    	areaReq.setRequstUrl(sysConfigVo.getData().getSysConfig().getParaValue());
	    	
	    	res = tripService.getTrvokArea(areaReq);
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
			BaseResponse<SysConfigVo> sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_AREA_SIGN_KEY);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_AREA_SIGN_KEY);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trvokAirportReq.setAreaSignKey(sysConfigVo.getData().getSysConfig().getParaValue());
			
			sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trvokAirportReq.setRequstUrl(sysConfigVo.getData().getSysConfig().getParaValue());
			
			sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_IMG_URL);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_IMG_URL);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trvokAirportReq.setImgUrl(sysConfigVo.getData().getSysConfig().getParaValue());
			
			res = tripService.getTrvokAirportInfo(trvokAirportReq);
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
	        } 
			TrovkCodeReq trovkCodeReq = new TrovkCodeReq();
			trovkCodeReq.setSku(sku);
			if(StringUtils.isBlank(voucherReq.getExpire())) {
				//从缓存中获取券码有效期时间
				BaseResponse<SysConfigVo> sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_VOUCHER_EXPIRE);
		    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
		    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_VOUCHER_EXPIRE);
		    		res.setResponseCode(sysConfigVo.getResponseCode());
		    		res.setResponseMsg(sysConfigVo.getResponseMsg());
		    		return res;
		    	}
		    	//获取当前时间  加上默认的有效期时间
		    	Date time = DateTimeUtil.addDay(DateTimeUtil.getCurrentTime(), Integer.valueOf(sysConfigVo.getData().getSysConfig().getParaValue()));
		    	voucherReq.setExpire(DateTimeUtil.getFormatDate(time, "yyyyMMdd"));
			}
			trovkCodeReq.setExpire(voucherReq.getExpire());
			trovkCodeReq.setOutTradeNo(voucherOrderRes.getData().getOrder().getOrderNo());
			//从缓存中获取秘钥及请求URL
			BaseResponse<SysConfigVo> sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_PARTNER);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_PARTNER);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trovkCodeReq.setPartner(sysConfigVo.getData().getSysConfig().getParaValue());
			
			sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_WCF_SIGN_KEY);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_WCF_SIGN_KEY);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trovkCodeReq.setWcfSignKey(sysConfigVo.getData().getSysConfig().getParaValue());
			
			sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trovkCodeReq.setRequstUrl(sysConfigVo.getData().getSysConfig().getParaValue());
			
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
			voucherRes.setOrderNo(voucherOrderRes.getData().getOrder().getOrderNo());
			voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
			voucherRes.setVoucherCode(baseResponse.getData().getVerifyCode());
			voucherRes.setStartDate(voucherOrderRes.getData().getVoucherOrder().getStartDate());
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
		try {
			//调用空港接口禁用券码
			TrovkDisableReq trovkDisableReq = new TrovkDisableReq();
			BaseResponse<SysConfigVo> sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_PARTNER);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_PARTNER);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
	    	trovkDisableReq.setPartner(sysConfigVo.getData().getSysConfig().getParaValue());
	    	
	    	sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_WCF_SIGN_KEY);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_WCF_SIGN_KEY);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
	    	trovkDisableReq.setWcfSignKey(sysConfigVo.getData().getSysConfig().getParaValue());
			
	    	sysConfigVo = cacheFeignClient.getSysConfig("CUPD", TRVOK_REQUST_URL);
	    	if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
	    		log.error("cache-service getSysConfig result is null  params is + " + " CUPD " +  TRVOK_REQUST_URL);
	    		res.setResponseCode(sysConfigVo.getResponseCode());
	    		res.setResponseMsg(sysConfigVo.getResponseMsg());
	    		return res;
	    	}
			trovkDisableReq.setRequstUrl(sysConfigVo.getData().getSysConfig().getParaValue());
			trovkDisableReq.setVerifyCode(disableVoucherReq.getVoucherCode());
			BaseResponse<TrovkDisableRes> baseResponse = tripService.disableCode(trovkDisableReq);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())) {
				res.setResponseCode(baseResponse.getResponseCode());
				res.setResponseMsg(baseResponse.getResponseMsg());
				return res;
			}
		}catch(Exception e){
			log.error("disableVoucher error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
		return res;
	}
	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher( String sup,WriteOffVoucherReq writeOffVoucherReq) {
		return null;
	}

	
}
