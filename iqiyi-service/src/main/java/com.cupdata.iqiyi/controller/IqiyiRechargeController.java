package com.cupdata.iqiyi.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cupdata.commons.api.iqiyi.IQiYiController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.CreateRechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.iqiyi.constant.IqiyiRechargeResCode;
import com.cupdata.iqiyi.feign.OrderFeignClient;
import com.cupdata.iqiyi.feign.ProductFeignClient;
import com.cupdata.iqiyi.feign.VoucherFeignClient;
import com.cupdata.iqiyi.utils.IqiyiRechargeUtils;
import com.cupdata.iqiyi.vo.IqiyiRechargeReq;
import com.cupdata.iqiyi.vo.IqiyiRechargeRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DingCong
 * @Description: 爱奇艺会员充值业务控制层
 * @CreateDate: 2018/2/6 15:44
 */
@Slf4j
@RestController
public class IqiyiRechargeController implements IQiYiController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private VoucherFeignClient voucherFeignClient;

    /**
     * 爱奇艺会员充值业务实现
     * @param org
     * @param rechargeReq
     * @param request
     * @param response
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value = "org" , required = true) String org, @RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {
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
                    || null == rechargeOrderRes.getData().getRechargeOrder()) {
                //创建订单失败，设置响应错误消息和错误状态码，给予返回
                log.info("创建订单失败！");
                rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return rechargeRes;
            }

            //调用券码微服务获取一条爱奇艺券码号(为爱奇艺券码类型id)
            BaseResponse<GetVoucherRes> IqiyiVoucherGetres = voucherFeignClient.getVoucherByCategoryId(org , rechargeReq);
            log.info("激活码结果:"+IqiyiVoucherGetres);

            //判断是否存在可用券码
            if(!"000000".equals(IqiyiVoucherGetres.getResponseCode())){
                log.info("券码列表没有可用券码");
                rechargeRes.setResponseCode(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getCode());
                rechargeRes.setResponseMsg(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getMsg());
                return rechargeRes;
            }

            //封装请求参数,调用爱奇艺充值工具类来进行充值业务
            IqiyiRechargeReq req = new IqiyiRechargeReq();
            req.setUserAccount(rechargeReq.getAccount());
            req.setCardCode(IqiyiVoucherGetres.getData().getVoucherCode());//充值激活码

            //调用爱奇艺工具类进行券码激活充值
            IqiyiRechargeRes res = IqiyiRechargeUtils.iqiyiRecharge(req);
            if(null == res || !"A00000".equals(res.getCode())) {
                log.error("调用爱奇艺会员充值接口，返回报文结果result非A00000-爱奇艺会员充值失败");
                //QQ会员充值失败，设置错误状态码和错误信息，给予返回
                rechargeRes.setResponseCode(IqiyiRechargeResCode.RECHARGE_EXCEPTION.getCode());
                rechargeRes.setResponseMsg(IqiyiRechargeResCode.RECHARGE_EXCEPTION.getMsg());
                return rechargeRes;
            }

            //会员充值成功,修改订单状态
            rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);

            //调用订单服务更新订单
            rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData()
                    || null == rechargeOrderRes.getData().getOrder()
                    || null == rechargeOrderRes.getData().getRechargeOrder()){
                rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                return rechargeRes;
            }

            //充值成功,响应用户
            RechargeRes IqiyirechargeRes = new RechargeRes();
            IqiyirechargeRes.setOrderNo(rechargeReq.getOrgOrderNo());
            IqiyirechargeRes.setRechargeStatus(IqiyiRechargeResCode.SUCCESS.getMsg());
            rechargeRes.setData(IqiyirechargeRes);
        }catch(Exception e){
            e.printStackTrace();
            log.error("爱奇艺充值业务类异常");
            rechargeRes.setResponseCode(IqiyiRechargeResCode.RECHARGE_EXCEPTION.getCode());
            rechargeRes.setResponseMsg(IqiyiRechargeResCode.RECHARGE_EXCEPTION.getMsg());
            return rechargeRes;
        }
        return rechargeRes;
    }
}
