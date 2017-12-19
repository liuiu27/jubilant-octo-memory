package com.cupdata.commons.api.product;

import com.cupdata.commons.vo.Result;
import com.cupdata.commons.vo.product.AreaResVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/trip")
public interface ITripService {

    @PostMapping("/getArea")
    Result<List<AreaResVO>> getArea(@RequestParam("reqJson") String reqJson, String bankCode, String orgNo);

}
