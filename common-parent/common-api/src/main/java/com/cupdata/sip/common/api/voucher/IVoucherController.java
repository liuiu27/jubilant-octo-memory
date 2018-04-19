package com.cupdata.sip.common.api.voucher;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import org.springframework.web.bind.annotation.*;

/**
 * @Auth: LinYong
 * @Description:券码服务接口
 * @Date: 13:06 2017/12/14
 */
@RequestMapping("/voucher")
public interface IVoucherController extends IVoucherApi{



}
