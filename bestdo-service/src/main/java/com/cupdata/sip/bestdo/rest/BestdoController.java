package com.cupdata.sip.bestdo.rest;

import com.cupdata.sip.bestdo.biz.BestdoBiz;
import com.cupdata.sip.common.api.bestdo.vo.OrderCreateReqVO;
import com.cupdata.sip.bestdo.vo.response.*;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.bestdo.IBestdoApi;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
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
    public BaseResponse crateBestdoOrder(@RequestBody @Validated OrderCreateReqVO orderCreateReqVO) {


        return new BaseResponse("哈哈啊哈哈哈");
    }


    @GetMapping("test")
    public String test(String p){

         User user=   getuser(p);

        return user.toString();
    }

    @CacheResult
   public User getuser(@CacheKey String name){

        if (name.equals("a")){
            return new User(name);
        }
        if (name.equals("bb")){
            return new User(name);
        }
        if (name.equals("ccc")){
            return new User(name);
        }
        if (name.equals("dddd")){
            return new User(name);
        }
        if (name.equals("eeee")){
            return new User(name);
        }

       return null;
    }

    @Data
    class User{
        String name;

        Integer age;

        User(String name){
            this.name =name;
            this.age = name.length();
        }
    }


}
