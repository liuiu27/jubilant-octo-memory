package com.cupdata.sip.common.api.bestdo;

import com.cupdata.sip.common.api.BaseResponse;

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
    public BaseResponse getMerItemList(String rightproduct, String sporttype) {
        return null;
    }

    @Override
    public BaseResponse getMerLists() {
        return null;
    }

    @Override
    public BaseResponse getBookDate(String rightproduct, String sporttype, String setMerItemId, String setVenueNo) {
        return null;
    }

    @Override
    public BaseResponse crateBestdoOrder(String parma) {
        return null;
    }
}
