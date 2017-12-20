package com.cupdata.trvok.controller;

import com.cupdata.commons.api.trvok.ITrvokController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.trvok.TrvokAreaRes;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auth: LinYong
 * @Description:空港易行相关的服务
 * @Date: 19:13 2017/12/19
 */
public class TrvokController implements ITrvokController{
    @Override
    public BaseResponse<TrvokAreaRes> getArea(@RequestBody TrvokAreaReq areaReq) {
        return null;
    }
}
