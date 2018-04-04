package com.cupdata.commons.api.ihuyi;

import com.cupdata.commons.api.recharge.IRechargeAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: DingCong
 * @Description: 互亿话费充值
 * @CreateDate: 2018/3/6 15:28
 */
@RequestMapping("/ihuyiPhoneRecharge")
public interface IHuyiPhoneController extends IRechargeAPI {

    /**
     * 互亿话费充值异步通知接口
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ihuyiPhoneRechargeCallBack", method = {RequestMethod.POST})
    public void ihuyiPhoneRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
