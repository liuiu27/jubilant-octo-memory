package com.cupdata.commons.api.ihuyi;

import com.cupdata.commons.api.recharge.IRechargeAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: DingCong
 * @Description: 互亿虚拟充值接口
 * @CreateDate: 2018/3/8 20:27
 */
@RequestMapping("/ihuyiVirtualRecharge")
public interface IhuyiVirtualGoodsController extends IRechargeAPI {

    /**
     * 互亿虚拟商品充值通知接口
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "ihuyiVirtualRechargeCallBack",method = {RequestMethod.POST})
    public void ihuyiVirtualRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
