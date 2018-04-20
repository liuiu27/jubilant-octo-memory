package com.cupdata.sip.common.api.trvok;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.trvok.request.TrvokAirportReq;
import com.cupdata.sip.common.api.trvok.request.TrvokAreaReq;
import com.cupdata.sip.common.api.trvok.response.TrvokAirportRes;
import com.cupdata.sip.common.api.trvok.response.TrvokAreaRes;
import com.cupdata.sip.common.api.voucher.IVoucherApi;

/**
 * @Auth: liwei
 * @Description:空港易行服务请求接口
 * @Date: 2018/04/20
 */

@RequestMapping("/trvok")
public interface ITrvokController extends IVoucherApi{
	
	@PostMapping("/getTrvokArea")
    public BaseResponse<TrvokAreaRes> getTrvokArea(@RequestBody TrvokAreaReq areaReq);
    
    @PostMapping("/getTrvokAirportInfo")
    public BaseResponse<TrvokAirportRes> getTrvokAirportInfo(@RequestBody TrvokAirportReq trvokAirportReq);

}
