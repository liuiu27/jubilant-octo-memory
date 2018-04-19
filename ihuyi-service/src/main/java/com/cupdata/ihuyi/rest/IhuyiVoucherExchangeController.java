package com.cupdata.ihuyi.rest;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.ihuyi.IhuyiVoucherController;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 19:11 2018/4/18
 */
public class IhuyiVoucherExchangeController implements IhuyiVoucherController{
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
