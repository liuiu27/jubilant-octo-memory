package com.cupdata.commons.api.voucher;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


}
