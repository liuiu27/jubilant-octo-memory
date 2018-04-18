package com.cupdata.sip.common.api.recharge;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 10:43 2018/4/18
 */
public class RechargeControllerFallback implements IRechargeController{
    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        return null;
    }
}
