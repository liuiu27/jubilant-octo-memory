package com.cupdata.sip.common.api.lakala;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.voucher.IVoucherApi;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: LinYong
 * @Description:拉卡拉服务请求接口
 * @Date: 16:22 2017/12/19
 */
@RequestMapping("/lakala")
public interface ILakalaController extends IVoucherApi{
}
