package com.cupdata.sip.common.api.lakala;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.lakala.request.DisableVoucherReq;
import com.cupdata.sip.common.api.lakala.request.GetVoucherReq;
import com.cupdata.sip.common.api.lakala.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.lakala.response.DisableVoucherRes;
import com.cupdata.sip.common.api.lakala.response.GetVoucherRes;
import com.cupdata.sip.common.api.lakala.response.WriteOffVoucherRes;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 11:11 2018/4/13
 */
public class LakalaControllerFallback implements ILakalaController{
    @Override
    public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq) {
        return null;
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
        return null;
    }
}
