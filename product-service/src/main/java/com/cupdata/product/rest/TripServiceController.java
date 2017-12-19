package com.cupdata.product.rest;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.api.product.ITripService;
import com.cupdata.commons.vo.Result;
import com.cupdata.commons.vo.product.AreaResVO;
import com.cupdata.product.biz.TripBiz;
import com.cupdata.product.utils.TripUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class TripServiceController implements ITripService {


    @Resource
    private TripBiz tripBiz;

    @Override
    public Result<List<AreaResVO>> getArea(String reqJson, String bankCode, String orgNo) {
        List<AreaResVO> areaReVOS = null;

        // 判断请求参数是否非法 并解密 返回 jsonObj 与 error
       String jsonObj  = (String) TripUtil.isValidDecrypted(reqJson, bankCode, orgNo, null).get("jsonObj");
        // 验签通过，进行业务逻辑处理
        tripBiz.doGet("url");
        return null;
    }
}
