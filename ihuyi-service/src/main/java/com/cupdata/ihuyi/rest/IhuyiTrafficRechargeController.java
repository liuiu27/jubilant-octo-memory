package com.cupdata.ihuyi.rest;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.ihuyi.IhuyiTrafficController;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 19:10 2018/4/18
 */
public class IhuyiTrafficRechargeController implements IhuyiTrafficController{
    @Override
    public void ihuyiTrafficRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        return null;
    }
}
