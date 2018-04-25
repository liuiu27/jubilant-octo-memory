package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 20:41 2018/4/25
 */
public class IhuyiPhoneControllerFallback implements IhuyiPhoneController{
    @Override
    public BaseResponse<String> ihuyiPhoneRechargeCallBack(String taskid, String orderid, String mobile, String message, String state, String sign) {
        return null;
    }

    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        return null;
    }
}
