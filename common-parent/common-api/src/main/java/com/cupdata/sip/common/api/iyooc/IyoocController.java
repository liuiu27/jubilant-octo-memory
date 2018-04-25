package com.cupdata.sip.common.api.iyooc;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.iyooc.response.ParkingFeeReqRes;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 13:34 2018/4/24
 */
@RequestMapping("/iyooc")
public interface IyoocController{

    /**
     * 停车费查询接口
     * @return
     */
    BaseResponse<ParkingFeeReqRes> parkingInfo();

}
