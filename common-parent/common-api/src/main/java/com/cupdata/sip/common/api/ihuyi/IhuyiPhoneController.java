package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.recharge.IRechargeApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @param orderid
     * @throws IOException
     */
    @PostMapping("ihuyiPhoneRechargeCallBack")
    BaseResponse<String> ihuyiPhoneRechargeCallBack (String taskid,String orderid,String mobile,String message,String state,String sign);
}
