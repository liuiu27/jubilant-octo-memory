package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.recharge.IRechargeApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: DingCong
 * @Description: 互亿话费充值服务接口
 * @@Date: Created in 10:02 2018/4/19
 */
@RequestMapping("/ihuyiPhoneRecharge")
public interface IhuyiPhoneController extends IRechargeApi{

    /**
     * 互亿话费充值异步通知接口
     * @param response
     * @throws IOException
     */
    @PostMapping("/ihuyiPhoneRechargeCallBack")
    void ihuyiPhoneRechargeCallBack(HttpServletRequest request, HttpServletResponse response);
}
