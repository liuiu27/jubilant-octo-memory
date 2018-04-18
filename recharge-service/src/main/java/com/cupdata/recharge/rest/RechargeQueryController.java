package com.cupdata.recharge.rest;

import com.cupdata.recharge.feign.OrderFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.recharge.IRechargeQueryController;
import com.cupdata.sip.common.api.recharge.request.RechargeQueryReq;
import com.cupdata.sip.common.api.recharge.response.RechargeResQuery;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.RechargeException;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 虚拟充值结果查询
 * @@Date: Created in 10:24 2018/4/18
 */
@Slf4j
@RestController
public class RechargeQueryController implements IRechargeQueryController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 虚拟充值结果查询Controller
     * @param org
     * @param rechargeQueryReq
     * @return
     */
    @Override
    public BaseResponse<RechargeResQuery> rechargeQuery(String org, RechargeQueryReq rechargeQueryReq) {
        log.info("调用虚拟充值结果查询Controller...org:"+org+",OrgOrderNo:"+rechargeQueryReq.getOrgOrderNo());
        BaseResponse<RechargeResQuery> res = new BaseResponse<>();
        try{
            //step1.判断请求参数是否合法
            if (StringUtils.isBlank(org) || StringUtils.isBlank(rechargeQueryReq.getOrgOrderNo())){
                log.error("判断请求参数是否合法发生错误,error message:"+ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                return res;
            }

            //step2.查询充值订单
            log.info("请求参数合法,开始查询充值订单");
            BaseResponse<RechargeOrderVo> rechargeOrderVo = orderFeignClient.getRechargeOrderByOrgNoAndOrgOrderNo(org,rechargeQueryReq.getOrgOrderNo());
            if (CommonUtils.isNullOrEmptyOfObj(rechargeOrderVo.getData()) || CommonUtils.isNullOrEmptyOfObj(rechargeOrderVo.getData().getOrderInfoVo())){
                log.error("订单数据为空,error message:"+ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
                res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
                res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
                return res;
            }

            //step3.封装查询结果信息给予返回
            log.info("查询充值订单结果为:orderNo:"+rechargeOrderVo.getData().getOrderInfoVo().getOrderNo()+",OrgOrderNo:"+rechargeOrderVo.getData().getOrderInfoVo().getOrgOrderNo()
                    +",OrderStatus:"+rechargeOrderVo.getData().getOrderInfoVo().getOrderStatus()+",ProductNo:"+rechargeOrderVo.getData().getProductNo());
            RechargeResQuery rechargeResQuery = new RechargeResQuery();
            rechargeResQuery.setAccount(rechargeOrderVo.getData().getAccountNumber());
            rechargeResQuery.setOrderDesc(rechargeOrderVo.getData().getOrderInfoVo().getOrderDesc());
            rechargeResQuery.setOrderNo(rechargeOrderVo.getData().getOrderInfoVo().getOrderNo());
            rechargeResQuery.setOrgOrderNo(rechargeOrderVo.getData().getOrderInfoVo().getOrgOrderNo());
            rechargeResQuery.setProductNo(rechargeOrderVo.getData().getProductNo());
            rechargeResQuery.setRechargeStatus(rechargeOrderVo.getData().getOrderInfoVo().getOrderStatus());
            res.setData(rechargeResQuery);
            return res;

        }catch(Exception e){
            log.error("虚拟充值结果查询异常,异常信息:"+e.getMessage());
            throw new RechargeException(ResponseCodeMsg.RECHARGE_RES_QUERY_EXCEPTION.getCode(),ResponseCodeMsg.RECHARGE_RES_QUERY_EXCEPTION.getMsg());
        }
    }
}
