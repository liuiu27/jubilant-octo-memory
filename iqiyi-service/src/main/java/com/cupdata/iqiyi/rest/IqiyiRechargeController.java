package com.cupdata.iqiyi.rest;

import com.cupdata.iqiyi.constant.IqiyiRechargeResCode;
import com.cupdata.iqiyi.feign.ConfigFeignClient;
import com.cupdata.iqiyi.feign.OrderFeignClient;
import com.cupdata.iqiyi.feign.ProductFeignClient;
import com.cupdata.iqiyi.feign.VoucherFeignClient;
import com.cupdata.iqiyi.utils.IqiyiRechargeUtils;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.iqiyi.IqiyiController;
import com.cupdata.sip.common.api.iqiyi.request.IqiyiRechargeReq;
import com.cupdata.sip.common.api.iqiyi.response.IqiyiRechargeRes;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.RechargeException;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 爱奇艺充值服务
 * @@Date: Created in 15:44 2018/4/18
 */
@Slf4j
@RestController
public class IqiyiRechargeController implements IqiyiController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private VoucherFeignClient voucherFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient;

    /**
     * 爱奇艺充值controller
     * @param org
     * @param rechargeReq
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam("org") String org, @RequestBody RechargeReq rechargeReq) {
        log.info("调用爱奇艺Controller...org:"+org+",Account:"+rechargeReq.getAccount()+",ProductNo:"+rechargeReq.getProductNo()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo());
        BaseResponse<RechargeRes> res = new BaseResponse<RechargeRes>();
        try {
            //step1.根据产品编号获取对应的产品信息
            log.info("根据服务产品编号查询对应的服务产品,ProductNo:"+rechargeReq.getProductNo());
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            if (null == productInfo || !ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())){
                log.error("供应商信息获取失败");
                res.setResponseCode(productInfo.getResponseCode());
                res.setResponseMsg(productInfo.getResponseMsg());
                return res;
            }

            //step2.创建充值订单
            CreateRechargeOrderVo createRechargeOrderVo = new CreateRechargeOrderVo();
            createRechargeOrderVo.setOrderDesc(rechargeReq.getOrderDesc());
            createRechargeOrderVo.setOrgNo(org);
            createRechargeOrderVo.setOrgOrderNo(rechargeReq.getOrgOrderNo());
            createRechargeOrderVo.setProductNo(rechargeReq.getProductNo());
            createRechargeOrderVo.setAccountNumber(rechargeReq.getAccount());
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.createRechargeOrder(createRechargeOrderVo);
            log.info("爱奇艺会员充值controller,创建订单成功,订单号 : "+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData()
                    || null == rechargeOrderRes.getData().getOrderInfoVo()) {
                log.error("爱奇艺会员充值创建订单失败！");
                res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return res;
            }

            //step3.调用券码微服务从本地获取一条爱奇艺券码(传入参数为爱奇艺券码类型id)
            GetVoucherReq getVoucherReq = new GetVoucherReq();
            getVoucherReq.setProductNo(rechargeReq.getProductNo());    //产品编号
            getVoucherReq.setOrgOrderNo(rechargeReq.getOrgOrderNo());  //机构订单号
            getVoucherReq.setMobileNo(rechargeReq.getMobileNo());      //手机号
            log.info("调用券码服获取本地券码");
            BaseResponse<GetVoucherRes> IqiyiVoucherGetRes = voucherFeignClient.getLocalVoucher(org,getVoucherReq);
            if(!CommonUtils.isNullOrEmptyOfObj(IqiyiVoucherGetRes.getData())){
                log.info("爱奇艺会员充值controller激活码获取结果 : "+IqiyiVoucherGetRes.getData().getVoucherCode());
            }

            //step4.对返回数据处理,如果券码库不存在可用券码
            if(!"000000".equals(IqiyiVoucherGetRes.getResponseCode())){
                log.info("爱奇艺会员充值controller从本地获取券码失败 : 券码列表没有可用券码");
                //修改订单状态
                rechargeOrderRes.getData().getOrderInfoVo().setOrderFailDesc("爱奇艺会员获取激活码失败");
                rechargeOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_NO);
                rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);

                //调用订单服务更新订单
                log.info("爱奇艺会员充值controller,更新充值订单OrderNo : "+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
                BaseResponse baseResponse = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())){
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    return res;
                }

                //设置错误响应码："800004","券码列表没有可用券码"
                res.setResponseCode(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getCode());
                res.setResponseMsg(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getMsg());

                //设置响应数据,返回数据
                RechargeRes rechargeRes = new RechargeRes();
                rechargeRes.setRechargeStatus(IqiyiRechargeResCode.FAIL.getCode());  //充值状态
                rechargeRes.setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo()); //平台单号
                res.setData(rechargeRes);
                return res;
            }

            //step5.成功获取券码,封装请求参数,调用爱奇艺充值工具类来进行充值业务
            log.info("获取券码成功,开始进行券码充值,充值账号:"+rechargeReq.getAccount()+",激活券码号:"+IqiyiVoucherGetRes.getData().getVoucherCode());
            IqiyiRechargeReq req = new IqiyiRechargeReq();
            req.setUserAccount(rechargeReq.getAccount());     //爱奇艺充值账号
            req.setCardCode(IqiyiVoucherGetRes.getData().getVoucherCode());//充值激活码
            long l1 = System.currentTimeMillis();
            IqiyiRechargeRes iqiyiRechargeRes = IqiyiRechargeUtils.iqiyiRecharge(req,configFeignClient);
            long l2 = System.currentTimeMillis();
            iqiyiRechargeRes.setCode("A00000");
            //step6.判断充值结果
            log.info("爱奇艺会员充值工具类充值结果 : "+res.toString()+",调用爱奇艺充值接口时间:"+(l2-l1));
            if(null == res || !"A00000".equals(iqiyiRechargeRes.getCode())) {
                log.error("爱奇艺会员充值失败，返回报文结果result非A00000-爱奇艺会员充值失败:"+res);

                //充值失败,修改订单状态
                rechargeOrderRes.getData().getOrderInfoVo().setOrderFailDesc("爱奇艺会员充值失败");
                rechargeOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_NO);
                rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);

                //调用订单服务更新订单
                log.info("爱奇艺会员充值controller,更新充值订单OrderNo : "+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
                BaseResponse baseResponse = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())){
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    return res;
                }

                //设置错误响应码:200009 - 充值失败
                res.setResponseCode(ResponseCodeMsg.RECHARGE_FAIL.getCode());
                res.setResponseMsg(ResponseCodeMsg.RECHARGE_FAIL.getMsg());

                //爱奇艺会员充值失败,设置错误状态码和错误信息，给予返回
                log.info("爱奇艺会员充值controller充值失败,响应用户");
                RechargeRes rechargeRes = new RechargeRes();
                rechargeRes.setRechargeStatus(IqiyiRechargeResCode.FAIL.getCode());  //充值状态
                rechargeRes.setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo()); //平台单号
                res.setData(rechargeRes);
                return res;
            }

            //step8.充值成功，修改订单状态
            rechargeOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_NO);
            rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);

            //step9.调用订单服务更新订单
            log.info("爱奇艺会员充值controller,更新充值订单OrderNo : "+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
            BaseResponse baseResponse = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());

            //setp10.调用服务更新订单
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())){
                res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                return res;
            }

            //step11.充值会员成功,响应用户
            log.info("爱奇艺会员充值controller充值成功,响应用户");
            RechargeRes rechargeRes = new RechargeRes();
            rechargeRes.setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());//平台单号
            rechargeRes.setRechargeStatus(IqiyiRechargeResCode.SUCCESS.getCode());//充值状态
            res.setData(rechargeRes);
            return res;

        }catch(Exception e){
            log.error("爱奇艺充值服务异常,异常信息:"+e.getMessage());
            throw new RechargeException(ResponseCodeMsg.IQIYI_EXCEPTION.getCode(),ResponseCodeMsg.IQIYI_EXCEPTION.getMsg());
        }
    }
}
