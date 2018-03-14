package com.cupdata.recharge.controller;

import com.cupdata.commons.api.recharge.IRechargeQueryController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeQueryReq;
import com.cupdata.commons.vo.recharge.RechargeResQuery;
import com.cupdata.recharge.feign.OrderFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: DingCong
 * @Description: 虚拟充值结果查询
 * @CreateDate: 2018/3/12 21:06
 */
@Slf4j
@RestController
public class RechargeQueryController implements IRechargeQueryController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public BaseResponse<RechargeResQuery> rechargeQuery(@RequestParam(value = "org" ,required = true) String org, @RequestBody RechargeQueryReq rechargeQueryReq) {
        log.info("虚拟充值结果查询,机构号:"+org+",机构订单唯一编号:"+rechargeQueryReq.getOrgOrderNo());
        BaseResponse<RechargeResQuery> res = new BaseResponse<>();  //定义响应对象
        BaseResponse<RechargeOrderVo> rechargeOrderVo ;             //定义查询充值订单变量
        try {
            if (StringUtils.isBlank(org) || StringUtils.isBlank(rechargeQueryReq.getOrgOrderNo())){
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                return res;
            }
            rechargeOrderVo = orderFeignClient.getRechargeOrderByOrgNoAndOrgOrderNo(org,rechargeQueryReq.getOrgOrderNo());
            RechargeResQuery rechargeResQuery = new RechargeResQuery();
            rechargeResQuery.setAccount(rechargeOrderVo.getData().getRechargeOrder().getAccountNumber());
            rechargeResQuery.setOrderDesc(rechargeOrderVo.getData().getOrder().getOrderDesc());
            rechargeResQuery.setOrderNo(rechargeOrderVo.getData().getOrder().getOrderNo());
            rechargeResQuery.setOrgOrderNo(rechargeOrderVo.getData().getOrder().getOrgOrderNo());
            rechargeResQuery.setProductNo(rechargeOrderVo.getData().getRechargeOrder().getProductNo());
            rechargeResQuery.setRechargeStatus(rechargeOrderVo.getData().getOrder().getOrderStatus());
            res.setData(rechargeResQuery);
            return res;
        }catch (Exception e){
            log.info("订单查询出现异常"+e.getMessage());
            res.setResponseCode(ResponseCodeMsg.QUERY_ORDER_EXCEPTION.getCode());
            res.setResponseMsg(ResponseCodeMsg.QUERY_ORDER_EXCEPTION.getMsg());
            return res;
        }
    }
}