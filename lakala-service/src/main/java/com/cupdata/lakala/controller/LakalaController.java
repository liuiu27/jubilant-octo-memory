package com.cupdata.lakala.controller;

import com.cupdata.commons.api.lakala.ILakalaController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.*;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value="org", required=true) String org, @RequestBody GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info(org);
        LOGGER.info(voucherReq.getProductNo());

        GetVoucherRes lakalaVoucher = new GetVoucherRes();
        lakalaVoucher.setOrderNo("DADAD32321DADD");
        BaseResponse<GetVoucherRes> lakalaVoucherRes = new BaseResponse<GetVoucherRes>(lakalaVoucher);
        return lakalaVoucherRes;
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value="org", required=true) String org, @RequestBody DisableVoucherReq disableVoucherReq, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(@RequestParam(value="sup", required=true) String sup, @RequestBody WriteOffVoucherReq writeOffVoucherReq, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
