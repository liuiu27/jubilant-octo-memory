package com.cupdata.tencent.controller;

import com.cupdata.commons.api.tencent.ITencentController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.CreateRechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import com.cupdata.commons.vo.recharge.RechargeResQuery;
import com.cupdata.tencent.constant.QQRechargeResCode;
import com.cupdata.tencent.feign.CacheFeignClient;
import com.cupdata.tencent.feign.OrderFeignClient;
import com.cupdata.tencent.feign.ProductFeignClient;
import com.cupdata.tencent.utils.QQRechargeUtils;
import com.cupdata.tencent.vo.QQCheckOpenReq;
import com.cupdata.tencent.vo.QQCheckOpenRes;
import com.cupdata.tencent.vo.QQOpenReq;
import com.cupdata.tencent.vo.QQOpenRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 腾讯相关服务
 * @Author: DingCong
 * @CreateDate: 2018/2/1 13:17
 */
@Slf4j
@RestController
public class TencentController implements ITencentController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private  CacheFeignClient cacheFeignClient ;

    /**
     * 腾讯充值业务
     * @param org
     * @param rechargeReq
     * @param request
     * @param response
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value = "org" , required = true)String org, @RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {
       log.info("调用腾讯充值接口......org:"+org+",Account:"+rechargeReq.getAccount()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo()+",ProductNo:"+rechargeReq.getProductNo()+",MobileNo:"+rechargeReq.getMobileNo());
        //设置响应结果
        BaseResponse<RechargeRes> rechargeRes = new BaseResponse<RechargeRes>();
        try {
            //根据服务产品编号查询对应的服务产品
            BaseResponse<ProductInfVo> productInfo = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            //产品信息响应码失败,返回错误信息参数
            if (productInfo == null || !ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())){
                log.info("腾讯充值controller获取产品失败,ProductNo:"+rechargeReq.getProductNo());
                //产品查询失败，设置错误的响应码和响应信息，给予返回
                rechargeRes.setResponseCode(productInfo.getResponseCode());
                rechargeRes.setResponseMsg(productInfo.getResponseMsg());
                return rechargeRes;
            }

            //腾讯充值鉴权
            QQCheckOpenReq checkOpenReq = new QQCheckOpenReq();
            checkOpenReq.setAmount(String.valueOf(productInfo.getData().getProduct().getRechargeDuration()));//开通时长
            checkOpenReq.setServiceid(productInfo.getData().getProduct().getSupplierParam());//充值业务类型
            checkOpenReq.setUin(rechargeReq.getAccount());//需要充值QQ
            checkOpenReq.setServernum(rechargeReq.getMobileNo());//手机号码
            checkOpenReq.setPaytype("1");//支付类型
            QQCheckOpenRes checkOpenRes = QQRechargeUtils.qqCheckOpen(checkOpenReq,cacheFeignClient);//开通鉴权响应结果
            if (null == checkOpenRes || !QQRechargeResCode.SUCCESS.getCode().equals(checkOpenRes.getResult())){
                log.info("腾讯充值鉴权失败,SupplierParam:"+productInfo.getData().getProduct().getSupplierParam());
                //鉴权失败，设置错误状态码和错误信息，给予返回
                rechargeRes.setResponseCode(QQRechargeResCode.QQNUMBER_ILLEGAL.getCode());
                rechargeRes.setResponseMsg(QQRechargeResCode.QQNUMBER_ILLEGAL.getMsg());
                return rechargeRes;
            }

            //创建充值订单：订单状态已完成初始化
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

            //封装请求参数,调用腾讯充值工具类来进行充值业务
            QQOpenReq openReq = new QQOpenReq();
            openReq.setAmount(String.valueOf(productInfo.getData().getProduct().getRechargeDuration()));//开通时长
            openReq.setServiceid(productInfo.getData().getProduct().getSupplierParam());//充值业务类型
            openReq.setUin(rechargeReq.getAccount());//需要充值QQ
            openReq.setServernum(rechargeReq.getMobileNo());//手机号码
            openReq.setPaytype("1");//支付类型
            openReq.setTxparam(checkOpenRes.getTxparam());//腾讯给予鉴权结果响应
            openReq.setCommand("1");//开通状态
            openReq.setTimestamp(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmss"));//设置时间戳
            openReq.setPrice(productInfo.getData().getProduct().getSupplierPrice().toString());//设置供应商价格
            QQOpenRes openRes = QQRechargeUtils.qqOpen(openReq,cacheFeignClient);//充值业务办理响应结果
            if (null==openRes || !QQRechargeResCode.SUCCESS.getCode().equals(openRes.getResult())){
                log.error("调用腾讯充值接口失败");
                //QQ会员充值失败，设置错误状态码和错误信息，给予返回
                rechargeRes.setResponseCode(QQRechargeResCode.QQMEMBER_RECHARGE_FAIL.getCode());
                rechargeRes.setResponseMsg(QQRechargeResCode.QQMEMBER_RECHARGE_FAIL.getMsg());
                return rechargeRes;
            }

            //会员充值成功,修改订单状态
            rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS); //订单成功
            rechargeOrderRes.getData().getOrder().setIsNotify(ModelConstants.IS_NOTIFY_NO);            //不通知机构

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
            RechargeRes res = new RechargeRes();
            res.setOrderNo(rechargeOrderRes.getData().getOrder().getOrderNo()); //平台单号
            res.setRechargeStatus(ModelConstants.RECHARGE_SUCCESS);             //充值状态
            rechargeRes.setData(res);
        }catch (Exception e){
            log.info("腾讯充值业务出现异常"+e.getMessage());
            rechargeRes.setResponseCode(QQRechargeResCode.QQRECHARGE_EXCEPTION.getCode());
            rechargeRes.setResponseMsg(QQRechargeResCode.QQRECHARGE_EXCEPTION.getMsg());
            return rechargeRes;
        }
        return rechargeRes;
    }
}
