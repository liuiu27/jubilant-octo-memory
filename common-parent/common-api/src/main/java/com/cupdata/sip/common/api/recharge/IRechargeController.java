package com.cupdata.sip.common.api.recharge;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: DingCong
 * @Description: 虚拟充值服务接口
 * @@Date: Created in 10:29 2018/4/18
 */
@RequestMapping("/recharge")
public interface IRechargeController extends IRechargeApi{
}
