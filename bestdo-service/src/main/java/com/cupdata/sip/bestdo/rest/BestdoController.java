package com.cupdata.sip.bestdo.rest;

import com.cupdata.sip.bestdo.biz.BestdoBiz;
import com.cupdata.sip.bestdo.vo.response.*;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.bestdo.IBestdoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

        MerDetailResVO merDetail = bestdoBiz.getMerDetail(merItemId);

        return new BaseResponse(merDetail);
    }

    @Override
    public BaseResponse getMerItemList(String rightproduct, String sporttype) {

        BestaResVO<List<MerItemRes>> merItemList = bestdoBiz.getMerItemList(rightproduct, sporttype);

        return new BaseResponse(merItemList.getData());
    }

    @Override
    public BaseResponse getMerLists() {
        BestaResVO<List<MerInfoRes>> merLists = bestdoBiz.getMerLists();

        return new BaseResponse(merLists.getData());
    }

    @Override
    public BaseResponse getBookDate(String rightproduct, String sporttype, String setMerItemId, String setVenueNo) {

        BookDateResVO bookDate = bestdoBiz.getBookDate(rightproduct, sporttype, setMerItemId, setVenueNo);

        return new BaseResponse(bookDate.getData());
    }

    @Override
    public BaseResponse crateBestdoOrder(String parma) {


        return new BaseResponse("哈哈啊哈哈哈");
        //bestdoBiz.crateBestdoOrder(parma);
        //throw new BestdoException("000","1231232");
    }


}
