package com.cupdata.commons.api.ihuyi;

import com.cupdata.commons.api.recharge.IRechargeAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: DingCong
 * @Description: 互亿流量充值
 * @CreateDate: 2018/3/6 15:28
 */
@RequestMapping("/ihuyiTrafficRecharge")
public interface IHuyiTrafficController extends IRechargeAPI {

    /**
     * 互亿流量充值异步通知接口
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ihuyiTrafficRechargeCallBack", method = {RequestMethod.POST})
    public void ihuyiTrafficRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
