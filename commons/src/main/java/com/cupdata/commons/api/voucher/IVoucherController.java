package com.cupdata.commons.api.voucher;

import com.cupdata.commons.vo.BaseData;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auth: LinYong
 * @Description:券码服务接口
 * @Date: 13:06 2017/12/14
 */
@RequestMapping("/voucher")
public interface IVoucherController extends IVoucherApi{
}
