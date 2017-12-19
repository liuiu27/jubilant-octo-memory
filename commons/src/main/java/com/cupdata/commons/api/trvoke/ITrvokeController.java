package com.cupdata.commons.api.trvoke;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.trvok.TrvokAreaRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:空港易行服务请求接口
 * @Date: 16:22 2017/12/19
 */
public interface ITrvokeController {
    @PostMapping("/getArea")
    public BaseResponse<TrvokAreaRes> getArea(@RequestBody TrvokAreaReq areaReq);
}
