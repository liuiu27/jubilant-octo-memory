package com.cupdata.voucher.controller;

import com.cupdata.commons.api.voucher.IVoucherController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 16:45 2017/12/20
 */
public class VoucherController implements IVoucherController{

    @Override
    public BaseResponse<GetVoucherRes> createByProductNo(@PathVariable("productNo") String productNo) {
        return null;
    }
}
