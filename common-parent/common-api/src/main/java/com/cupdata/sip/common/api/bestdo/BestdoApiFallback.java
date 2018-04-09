package com.cupdata.sip.common.api.bestdo;

import com.cupdata.sip.common.lang.BaseResponse;

/**
 * @author Tony
 * @date 2018/04/04
 */
public class BestdoApiFallback implements IBestdoApi {

    @Override
    public BaseResponse getMerDetail(String merItemId) {
        return null;
    }

    @Override
    public BaseResponse getMerLists() {
        return null;
    }
}
