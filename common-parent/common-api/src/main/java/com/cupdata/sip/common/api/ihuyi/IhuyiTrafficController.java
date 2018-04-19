package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.recharge.IRechargeApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ihuyiTrafficRechargeCallBack", method = {RequestMethod.POST})
    public void ihuyiTrafficRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
