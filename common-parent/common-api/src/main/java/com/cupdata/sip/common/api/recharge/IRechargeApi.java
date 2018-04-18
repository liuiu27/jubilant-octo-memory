package com.cupdata.sip.common.api.recharge;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Auther: DingCong
 * @Description: 充值接口API
 * @@Date: Created in 14:23 2018/4/18
 */
public interface IRechargeApi {

    /**
     * 充值业务接口（实现方法中需要添加@RequestBody注解获取参数）
     * @param org
     * @param rechargeReq
     * @return
     */
    @PostMapping("/getRecharge")
    BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq);

}
