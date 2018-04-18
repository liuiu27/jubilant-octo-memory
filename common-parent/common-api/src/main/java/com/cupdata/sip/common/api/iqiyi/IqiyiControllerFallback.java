package com.cupdata.sip.common.api.iqiyi;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 15:45 2018/4/18
 */
public class IqiyiControllerFallback implements IqiyiController{
    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        return null;
    }
}
