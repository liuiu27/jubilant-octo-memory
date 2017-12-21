package com.cupdata.commons.api.trvok;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:空港易行服务请求接口
 * @Date: 16:22 2017/12/19
 */

@RequestMapping("/trvok")
public interface ITrvokController {
	
    @PostMapping("/getTrvokArea")
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq);
    
    @PostMapping("/getTrvokAirportInfo")
    public BaseResponse<TrvokAirportRes> getTrvokAirportInfo(@RequestBody TrvokAirportReq trvokAirportReq);

    @PostMapping("/getTrvokVerifyCode")
    public BaseResponse<TrovkCodeRes> getTrvokVerifyCode(@RequestBody TrovkCodeReq trovkCodeReq);

    @PostMapping("/activationTrvokCode")
    public BaseResponse<TrovkActivatRes> activationTrvokCode(@RequestBody TrovkActivatReq trovkActivatReq);
    
    @PostMapping("/disableCode")
    public BaseResponse<TrovkDisableRes> disableCode(@RequestBody TrovkDisableReq trovkDisableReq);

}
