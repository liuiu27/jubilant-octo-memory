package com.cupdata.sip.common.api.bestdo;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.bestdo.vo.MerReq;

/**
 * @author Tony
 * @date 2018/04/04
 */
public interface IBestdoApi {

    BaseResponse get(MerReq merReq );

}
