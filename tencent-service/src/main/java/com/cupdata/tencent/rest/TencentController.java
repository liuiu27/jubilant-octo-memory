package com.cupdata.tencent.rest;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import com.cupdata.sip.common.api.tencent.ITencentController;
import com.cupdata.sip.common.api.tencent.request.QQCheckOpenReq;
import com.cupdata.sip.common.api.tencent.request.QQOpenReq;
import com.cupdata.sip.common.api.tencent.response.QQCheckOpenRes;
import com.cupdata.sip.common.api.tencent.response.QQOpenRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.RechargeException;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.tencent.constant.QQRechargeResCode;
import com.cupdata.tencent.feign.ConfigFeignClient;
import com.cupdata.tencent.feign.OrderFeignClient;
import com.cupdata.tencent.feign.ProductFeignClient;
import com.cupdata.tencent.utils.QQRechargeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 腾讯充值业务
 * @@Date: Created in 14:27 2018/4/18
 */
@Slf4j
@RestController
public class TencentController implements ITencentController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient;

    /**
     * 腾讯充值业务
     * @param org
     * @param rechargeReq
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        log.info("调用腾讯充值Controller...org:"+org+",Account:"+rechargeReq.getAccount()+",ProductNo:"+rechargeReq.getProductNo()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo());
        BaseResponse<RechargeRes> res = new BaseResponse<>();
        try {
            //step1.根据服务产品编号查询对应的服务产品
            log.info("根据服务产品编号查询对应的服务产品,ProductNo:"+rechargeReq.getProductNo());
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            if (productInfo == null || !ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())){
                log.error("根据服务产品编号查询对应的服务产品失败,ProductNo:"+rechargeReq.getProductNo());
                res.setResponseCode(productInfo.getResponseCode());
                res.setResponseMsg(productInfo.getResponseMsg());
                return res;
            }

            //step2.腾讯充值鉴权
            log.info("开始腾讯充值鉴权");
            QQCheckOpenReq checkOpenReq = new QQCheckOpenReq();
            checkOpenReq.setAmount(String.valueOf(productInfo.getData().getRechargeDuration()));//开通时长
            checkOpenReq.setServiceid(productInfo.getData().getSupplierParam());                //充值业务类型
            checkOpenReq.setUin(rechargeReq.getAccount());                                      //需要充值QQ
            checkOpenReq.setServernum(rechargeReq.getMobileNo());                               //手机号码
            checkOpenReq.setPaytype("1");                                                       //支付类型
            QQCheckOpenRes checkOpenRes = QQRechargeUtils.qqCheckOpen(checkOpenReq,configFeignClient);//开通鉴权响应结果
            log.info("腾讯充值鉴权结果:"+checkOpenRes.getResult());
            if (null == checkOpenRes || !QQRechargeResCode.SUCCESS.getCode().equals(checkOpenRes.getResult())){
                log.info("腾讯充值鉴权失败,SupplierParam:"+productInfo.getData().getSupplierParam());
                res.setResponseCode(QQRechargeResCode.QQCHECK_FAIL.getCode());
                res.setResponseMsg(QQRechargeResCode.QQCHECK_FAIL.getMsg());
                return res;
            }

            //step3.创建订单
            CreateRechargeOrderVo createRechargeOrderVo = new CreateRechargeOrderVo();
            createRechargeOrderVo.setOrderDesc(rechargeReq.getOrderDesc());
            createRechargeOrderVo.setOrgNo(org);
            createRechargeOrderVo.setOrgOrderNo(rechargeReq.getOrgOrderNo());
            createRechargeOrderVo.setProductNo(rechargeReq.getProductNo());
            createRechargeOrderVo.setAccountNumber(rechargeReq.getAccount());
            log.info("腾讯充值controller开始创建服务订单");
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.createRechargeOrder(createRechargeOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                || null == rechargeOrderRes.getData() || null == rechargeOrderRes.getData().getOrderInfoVo()){
                //创建订单失败，设置响应错误消息和错误状态码，给予返回
                res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return res;
            }

            //step4.封装请求参数,调用腾讯充值工具类来进行充值业务
            QQOpenReq openReq = new QQOpenReq();
            openReq.setAmount(String.valueOf(productInfo.getData().getRechargeDuration()));//开通时长
            openReq.setServiceid(productInfo.getData().getSupplierParam());//充值业务类型
            openReq.setUin(rechargeReq.getAccount());//需要充值QQ
            openReq.setServernum(rechargeReq.getMobileNo());//手机号码
            openReq.setPaytype("1");//支付类型
            openReq.setTxparam(checkOpenRes.getTxparam());//腾讯给予鉴权结果响应
            openReq.setCommand("1");//开通状态
            openReq.setTimestamp(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmss"));//设置时间戳
            openReq.setPrice(productInfo.getData().getSupplierPrice().toString());//设置供应商价格
            QQOpenRes openRes = QQRechargeUtils.qqOpen(openReq,configFeignClient);//充值业务办理响应结果

            //step5.判断调用腾讯充值接口返回结果是否成功
            log.info("腾讯充值controller调用充值接口充值结果,openRes:"+openRes.getResult());
            if (null==openRes || !QQRechargeResCode.SUCCESS.getCode().equals(openRes.getResult())){
                log.error("调用腾讯接口充值失败");

                //更新订单状态
                rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);    //订单失败
                rechargeOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_NO);            //不通知机构
                rechargeOrderRes.getData().getOrderInfoVo().setOrderFailDesc("腾讯充值失败");

                //调用订单服务更新订单
                log.info("腾讯充值controller充值失败,更新服务订单OrderNo:"+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData() || null == rechargeOrderRes.getData()
                        || null == rechargeOrderRes.getData().getOrderInfoVo()){
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    return res;
                }

                //QQ会员充值失败，设置错误状态码和错误信息，给予返回
                res.setResponseCode(QQRechargeResCode.QQMEMBER_RECHARGE_FAIL.getCode());
                res.setResponseMsg(QQRechargeResCode.QQMEMBER_RECHARGE_FAIL.getMsg());

                //封装返回数据
                log.error("腾讯充值失败");
                res.getData().setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());  //平台单号
                res.getData().setRechargeStatus(ModelConstants.RECHARGE_FIAL); //充值状态:F
                return res;
            }

            //step6.会员充值成功,修改订单状态
            rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS); //订单成功
            rechargeOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_NO);            //不通知机构

            //step7.调用订单服务更新订单
            log.info("腾讯充值controller充值成功,更新服务订单OrderNo:"+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
            rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData() || null == rechargeOrderRes.getData().getOrderInfoVo()){
                res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                return res;
            }

            //step8.充值成功,响应用户
            log.info("腾讯充值controller充值成功,响应用户");
            res.getData().setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo()); //平台单号
            res.getData().setRechargeStatus(ModelConstants.RECHARGE_SUCCESS);                   //充值状态:S
            return res;

        } catch (Exception e) {
            log.error("调用腾讯充值服务异常,异常信息:"+e.getMessage());
            throw new RechargeException(ResponseCodeMsg.TENCENT_EXCEPTION.getCode(),ResponseCodeMsg.TENCENT_EXCEPTION.getMsg());
        }
    }
}
