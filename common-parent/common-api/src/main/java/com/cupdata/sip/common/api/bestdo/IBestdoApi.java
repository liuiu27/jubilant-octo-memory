package com.cupdata.sip.common.api.bestdo;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.bestdo.vo.OrderCreateReqVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tony
 * @date 2018/04/04
 */
@RequestMapping("/bestdo")
public interface IBestdoApi {

    @GetMapping("/getMerDetail")
    BaseResponse getMerDetail(String merItemId);

    @GetMapping("getMerItemList")
    BaseResponse getMerItemList(String rightproduct,String sporttype);

    @GetMapping("getMerLists")
    BaseResponse getMerLists();

    @GetMapping("getBookDate")
    BaseResponse getBookDate(String rightproduct,String sporttype,String setMerItemId,String setVenueNo);

    @PostMapping("crateBestdoOrder")
    BaseResponse crateBestdoOrder(@RequestBody @Validated OrderCreateReqVO orderCreateReqVO);

}
