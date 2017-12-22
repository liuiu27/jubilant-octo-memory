package com.cupdata.commons.api.voucher;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auth: LinYong
 * @Description:券码服务接口
 * @Date: 13:06 2017/12/14
 */
public interface IVoucherController {
    /**
     * 根据机构编号，查询机构信息
     * @return
     */
    @PostMapping("/voucher")
    public BaseResponse<GetVoucherRes> createByProductNo(@RequestParam(required=true) String org, @RequestBody GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response);
}
