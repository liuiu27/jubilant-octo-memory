package com.cupdata.sip.bestdo.rest;

import com.cupdata.sip.bestdo.biz.BestdoBiz;
import com.cupdata.sip.common.lang.BaseResponse;
import com.cupdata.sip.common.api.bestdo.IBestdoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tony
 * @date 2018/03/30
 */
@RestController
public class BestdoController implements IBestdoApi {



    @Autowired
    private BestdoBiz bestdoBiz;

    @Override
    public BaseResponse getMerDetail(String merItemId) {

        bestdoBiz.getMerDetail(merItemId);

        return null;
    }

    @Override
    public BaseResponse getMerLists() {
        bestdoBiz.getMerLists();


        return null;
    }


}
