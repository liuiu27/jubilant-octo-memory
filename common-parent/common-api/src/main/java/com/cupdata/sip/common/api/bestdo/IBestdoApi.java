package com.cupdata.sip.common.api.bestdo;

import com.cupdata.sip.common.lang.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tony
 * @date 2018/04/04
 */
@RequestMapping("/bestdo")
public interface IBestdoApi {

    @GetMapping("/getMerDetail")
    BaseResponse getMerDetail(String merItemId);


    @GetMapping("getMerLists")
    BaseResponse getMerLists();



}
