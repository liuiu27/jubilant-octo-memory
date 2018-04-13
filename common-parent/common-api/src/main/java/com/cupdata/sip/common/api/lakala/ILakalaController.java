package com.cupdata.sip.common.api.lakala;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.lakala.request.DisableVoucherReq;
import com.cupdata.sip.common.api.lakala.request.GetVoucherReq;
import com.cupdata.sip.common.api.lakala.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.lakala.response.DisableVoucherRes;
import com.cupdata.sip.common.api.lakala.response.GetVoucherRes;
import com.cupdata.sip.common.api.lakala.response.WriteOffVoucherRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: LinYong
 * @Description:拉卡拉服务请求接口
 * @Date: 16:22 2017/12/19
 */
@RequestMapping("/lakala")
public interface ILakalaController{

    /**
     * 获取券码接口方法
     * @param org 机构编号
     * @param voucherReq 获取券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @PostMapping("/getVoucher")
    public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq);

    /**
     * 禁用券码接口方法
     * @param org 机构编号
     * @param disableVoucherReq 禁用券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @PostMapping("/disableVoucher")
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq);

    /**
     * 核销券码接口方法
     * @param sup 商户编号
     * @param writeOffVoucherReq 核销券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @PostMapping("/writeOffVoucher")
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq);


}
