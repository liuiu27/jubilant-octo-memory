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
 * @Description: 互亿虚拟商品充值接口
 * @@Date: Created in 19:13 2018/4/18
 */
@RequestMapping("/ihuyiVirtualRecharge")
public interface IhuyiVirtualGoodsController extends IRechargeApi{

    /**
     * 互亿虚拟商品充值通知接口
     * @throws IOException
     */
    @PostMapping("ihuyiVirtualRechargeCallBack")
    BaseResponse ihuyiVirtualRechargeCallBack(String taskid,String orderid,String account,String status,String Return,String money,String sign);
}
