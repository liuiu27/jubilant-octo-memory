package com.cupdata.sip.common.api.recharge;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeQueryReq;
import com.cupdata.sip.common.api.recharge.response.RechargeResQuery;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 11:40 2018/4/18
 */
public class RechargeQueryControllerFallback implements IRechargeQueryController{
    @Override
    public BaseResponse<RechargeResQuery> rechargeQuery(String org, RechargeQueryReq req) {
        return null;
    }
}
