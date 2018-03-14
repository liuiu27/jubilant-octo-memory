package com.cupdata.commons.api.recharge;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.recharge.RechargeQueryReq;
import com.cupdata.commons.vo.recharge.RechargeResQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: DingCong
 * @Description: 虚拟充值结果查询接口
 * @CreateDate: 2018/3/12 21:08
 */
@RequestMapping("/query")
public interface IRechargeQueryController {

    /**
     * 充值业务查询
     * @return
     */
    @PostMapping("/rechargeQuery")
    public BaseResponse<RechargeResQuery> rechargeQuery(String org , RechargeQueryReq req);
}
