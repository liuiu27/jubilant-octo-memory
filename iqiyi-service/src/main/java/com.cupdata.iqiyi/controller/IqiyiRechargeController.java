package com.cupdata.iqiyi.controller;

import com.cupdata.commons.api.iqiyi.IQiYiController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.CreateRechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import com.cupdata.iqiyi.feign.OrderFeignClient;
import com.cupdata.iqiyi.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DingCong
 * @Description: 爱奇艺会员充值业务控制层
 * @CreateDate: 2018/2/6 15:44
 */
@Slf4j
@Controller
public class IqiyiRechargeController implements IQiYiController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 爱奇艺会员充值业务实现
     * @param org
     * @param rechargeReq
     * @param request
     * @param response
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {
        log.info("调用爱奇艺会员充值接口...");
        //设置响应结果
        BaseResponse<RechargeRes> rechargeRes = new BaseResponse<RechargeRes>();
        //设置产品参数信息
        BaseResponse<ProductInfVo> productInfo = null;
        try {
            //获取供应商产品信息
            productInfo = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            if (null == productInfo || !ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())){
                rechargeRes.setResponseCode(productInfo.getResponseCode());
                rechargeRes.setResponseMsg(productInfo.getResponseMsg());
                return rechargeRes;
            }

            //创建充值订单
            CreateRechargeOrderVo createRechargeOrderVo = new CreateRechargeOrderVo();
            createRechargeOrderVo.setOrderDesc(rechargeReq.getOrderDesc());
            createRechargeOrderVo.setOrgNo(org);
            createRechargeOrderVo.setOrgOrderNo(rechargeReq.getOrgOrderNo());
            createRechargeOrderVo.setProductNo(rechargeReq.getProductNo());
            //调用订单服务创建订单
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.createRechargeOrder(createRechargeOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData()
                    || null == rechargeOrderRes.getData().getOrder()
                    || null == rechargeOrderRes.getData().getRechargeOrder()){
                //创建订单失败，设置响应错误消息和错误状态码，给予返回
                rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return rechargeRes;
            }

            //此处通过爱奇艺接口来激活会员





        }catch(Exception e){
            e.printStackTrace();
            log.error("爱奇艺充值业务类异常");
        }

        return null;
    }
}
