package com.cupdata.iyooc.rest;

import com.cupdata.iyooc.feign.ConfigFeignClient;
import com.cupdata.iyooc.feign.OrderFeignClient;
import com.cupdata.iyooc.feign.ProductFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.iyooc.IyoocController;
import com.cupdata.sip.common.api.iyooc.response.ParkingFeeReqRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 优积付停车充值服务
 * @@Date: Created in 13:41 2018/4/24
 */
@Slf4j
@RestController
public class IyoocParkingController implements IyoocController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient;


    /**
     * 停车费用信息查询接口
     * @return
     */
    @Override
    public BaseResponse<ParkingFeeReqRes> parkingInfo() {


        return null;
    }
}
