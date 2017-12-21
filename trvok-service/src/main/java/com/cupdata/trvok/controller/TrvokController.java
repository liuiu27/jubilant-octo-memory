package com.cupdata.trvok.controller;

import com.cupdata.commons.api.trvok.ITrvokController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.trvok.TrvokAreaRes;
import com.cupdata.commons.vo.trvok.TrvokAreaRes.AirportSummary;
import com.cupdata.trvok.service.TripService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auth: LinYong
 * @Description:空港易行相关的服务
 * @Date: 19:13 2017/12/19
 */
@Slf4j
public class TrvokController implements ITrvokController{
	@Autowired TripService  tTripService;
	
    @Override
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq) {
    	areaReq.setAreaType("1");
    	log.info("TrvokController getTrvokArea begin.............param areaTyoe is " + areaReq.getAreaType());
    	BaseResponse<TrvokAreaRes>  baseResponse = tTripService.getTrvokArea(areaReq);
        return baseResponse;
    }
}
