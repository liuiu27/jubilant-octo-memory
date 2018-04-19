package com.cupdata.sip.common.api.ihuyi;

import com.cupdata.sip.common.api.recharge.IRechargeApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ihuyiVirtualRechargeCallBack",method = {RequestMethod.POST})
    public void ihuyiVirtualRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
