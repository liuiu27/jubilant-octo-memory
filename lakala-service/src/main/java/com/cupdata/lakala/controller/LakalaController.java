package com.cupdata.lakala.controller;

import com.cupdata.commons.api.lakala.ILakalaController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.*;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LinYong
 * @Description: 拉卡拉相关的服务
 * @create 2018-01-04 18:29
 */
@RestController
public class LakalaController implements ILakalaController{
    Logger LOGGER = Logger.getLogger(LakalaController.class);

    @Override
    public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info(org);
        LOGGER.info(voucherReq.getProductNo());
        return null;
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
