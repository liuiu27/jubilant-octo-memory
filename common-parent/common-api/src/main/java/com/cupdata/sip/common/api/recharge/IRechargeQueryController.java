package com.cupdata.sip.common.api.recharge;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.request.RechargeQueryReq;
import com.cupdata.sip.common.api.recharge.response.RechargeResQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: DingCong
 * @Description: 券码充值结果查询接口
 * @@Date: Created in 11:39 2018/4/18
 */
@RequestMapping("query")
public interface IRechargeQueryController {

    /**
     * 充值结果查询接口
     * @return
     */
    @PostMapping("/rechargeQuery")
    public BaseResponse<RechargeResQuery> rechargeQuery(String org , RechargeQueryReq req);

}
