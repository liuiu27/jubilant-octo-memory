package com.cupdata.commons.api.voucher;

import com.cupdata.commons.model.ElectronicVoucherLib;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DingCong
 * @Description: 本地数据库获取券码接口
 * @CreateDate: 2018/3/1 19:24
 */
@RequestMapping("/getVoucherFromLocal")
public interface IGetVoucherFromLocalController{

    /**
     * SIP根据券码类型id从本地券码库获取一条券码
     * @return
     */
    @PostMapping("/getVoucherFromLocal")
    public BaseResponse<GetVoucherRes> getVoucherFromLocal(@RequestBody GetVoucherReq voucherReq);

    /**
     * 其他机构调用SIP的接口来从本地获取一条券码
     * @param org
     * @param voucherReq
     * @return
     */
    @PostMapping("/getVoucher")
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value = "org" , required = true) String org , @RequestBody GetVoucherReq voucherReq);


    /**
     * 更新券码列表
     * @param electronicVoucherLib
     * @return
     */
    @PostMapping("/updateVoucherLib")
    public void updateVoucherLib(@RequestBody ElectronicVoucherLib electronicVoucherLib);
}
