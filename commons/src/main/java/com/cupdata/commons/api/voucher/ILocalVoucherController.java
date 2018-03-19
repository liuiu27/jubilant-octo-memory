package com.cupdata.commons.api.voucher;

import com.cupdata.commons.model.ElectronicVoucherLib;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.voucher.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DingCong
 * @Description: 本地数据库获取券码接口
 * @CreateDate: 2018/3/1 19:24
 */
@RequestMapping("/localVoucher")
public interface ILocalVoucherController {

    /**
     * SIP根据券码类型id从本地券码库获取一条券码
     * @return
     */
    @PostMapping("/getLocalVoucher")
    public BaseResponse<GetVoucherRes> getLocalVoucher(@RequestParam(value = "org" , required = true) String org ,@RequestBody GetVoucherReq voucherReq);

    /**
     * 获取券码接口方法
     * @param org 机构编号
     * @param voucherReq 获取券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @PostMapping("/getVoucher")
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value = "org" , required = true) String org ,@RequestBody GetVoucherReq voucherReq);

    /**
     * 禁用券码接口方法
     * @param org 机构编号
     * @param disableVoucherReq 禁用券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/disableVoucher")
    public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value = "org" , required = true) String org,@RequestBody DisableVoucherReq disableVoucherReq);

    /**
     * 核销券码接口方法
     * @param sup 商户编号
     * @param writeOffVoucherReq 核销券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/writeOffVoucher")
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(@RequestParam(value = "sup" , required = true) String sup,@RequestBody WriteOffVoucherReq writeOffVoucherReq);

}
