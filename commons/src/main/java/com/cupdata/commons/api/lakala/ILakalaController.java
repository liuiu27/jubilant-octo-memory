package com.cupdata.commons.api.lakala;

import com.cupdata.commons.api.voucher.IVoucherApi;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.trvok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: LinYong
 * @Description:拉卡拉服务请求接口
 * @Date: 16:22 2017/12/19
 */
@RequestMapping("/lakala")
public interface ILakalaController extends IVoucherApi{

}
