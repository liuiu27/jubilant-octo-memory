package com.cupdata.iqiyi.rest;

import com.cupdata.iqiyi.feign.ConfigFeignClient;
import com.cupdata.iqiyi.feign.OrderFeignClient;
import com.cupdata.iqiyi.feign.ProductFeignClient;
import com.cupdata.iqiyi.feign.VoucherFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.iqiyi.IqiyiController;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 爱奇艺充值服务
 * @@Date: Created in 15:44 2018/4/18
 */
@Slf4j
@RestController
public class IqiyiRechargeController implements IqiyiController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private VoucherFeignClient voucherFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient;

    /**
     * 爱奇艺充值controller
     * @param org
     * @param rechargeReq
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        log.info("调用爱奇艺Controller...org:"+org+",Account:"+rechargeReq.getAccount()+",ProductNo:"+rechargeReq.getProductNo()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo());
        BaseResponse<RechargeRes> res = new BaseResponse<RechargeRes>();
        try {

        }catch(Exception e){

        }

        return null;
    }
}
