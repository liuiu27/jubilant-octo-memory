package com.cupdata.commons.api.voucher;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auth: LinYong
 * @Description: 券码相关的接口，所有提供券码相关的服务必须要实现该接口中的相关方法
 * @Date: 14:21 2018/1/4
 */
public interface IVoucherApi {

    /**
     * 获取券码接口方法
     * @param org 机构编号
     * @param voucherReq 获取券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/getVoucher")
    public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response);

    /**
     * 禁用券码接口方法
     * @param org 机构编号
     * @param disableVoucherReq 禁用券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/disableVoucher")
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq, HttpServletRequest request, HttpServletResponse response);

    /**
     * 核销券码接口方法
     * @param sup 商户编号
     * @param writeOffVoucherReq 核销券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/writeOffVoucher")
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq, HttpServletRequest request, HttpServletResponse response);
}
