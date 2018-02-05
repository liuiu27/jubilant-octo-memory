package com.cupdata.commons.api.recharge;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 充值相关接口,所有充值相关业务必须实现该接口
 * @Author: Dcein
 * @CreateDate: 2018/2/1 10:15
 */

public interface IRechargeAPI {
    /**
     * 充值业务接口（实现方法中需要添加@RequestBody注解获取参数）
     */
    @PostMapping("/getRecharge")
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response);



}
