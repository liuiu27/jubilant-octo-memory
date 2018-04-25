package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 20:42 2018/4/25
 */
public class IhuyiVirtualGoodsControllerFallback implements IhuyiVirtualGoodsController{
    @Override
    public BaseResponse ihuyiVirtualRechargeCallBack(String taskid, String orderid, String account, String status, String Return, String money, String sign) {
        return null;
    }

    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        return null;
    }
}
