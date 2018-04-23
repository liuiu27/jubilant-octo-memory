package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.IRechargeApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: DingCong
 * @Description: 互亿流量充值接口
 * @@Date: Created in 19:12 2018/4/18
 */
@RequestMapping("/ihuyiTrafficRecharge")
public interface IhuyiTrafficController extends IRechargeApi{

    /**
     * 互亿流量充值结果异步推送接口
     * @throws IOException
     */
    @PostMapping("ihuyiTrafficRechargeCallBack")
    BaseResponse<String> ihuyiTrafficRechargeCallBack(String taskid,String orderid,String mobile,String message,String status,String sign);
}
