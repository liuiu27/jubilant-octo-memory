package com.cupdata.trvok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.cupdata.commons.api.trvok.ITrvokController;
import com.cupdata.commons.vo.BaseResponse;
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
import com.cupdata.trvok.service.TripService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:空港易行相关的服务
 * @Date: 19:13 2017/12/19
 */
@Slf4j
public class TrvokController implements ITrvokController{
	@Autowired TripService  tripService;
	
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
	 * 空港获取券码
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
	
	
}
