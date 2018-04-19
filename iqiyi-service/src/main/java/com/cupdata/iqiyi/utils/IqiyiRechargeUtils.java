package com.cupdata.iqiyi.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.iqiyi.feign.ConfigFeignClient;
import com.cupdata.sip.common.api.iqiyi.request.IqiyiRechargeReq;
import com.cupdata.sip.common.api.iqiyi.response.IqiyiRechargeRes;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.HttpUtil;
import com.cupdata.sip.common.lang.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: DingCong
 * @Description: 爱奇艺账号充值工具类
 * @CreateDate: 2018/2/6 14:56
 */
@Slf4j
public class IqiyiRechargeUtils {

    /**
     * 爱奇艺充值业务
     */
    public static IqiyiRechargeRes iqiyiRecharge(IqiyiRechargeReq req, ConfigFeignClient configFeignClient){
        //设置响应信息
        IqiyiRechargeRes iqiyiRechargeRes = null;
        try {
            //step1.调用爱奇艺充值接口
            log.info("调用爱奇艺充值接口,请求数据CardCode:"+req.getCardCode()+",PartnerNo:"+req.getPartnerNo()+",UserAccount:"+req.getUserAccount()+",Sign:"+req.getSign());
            String rechargeUrl = null;
            if(CommonUtils.isWindows()){
                rechargeUrl = "http://openapi.vip.iqiyi.com/partner/card-subscribe.action";
            }else{
                //对data进行判空处理
                if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"IQIYI_RECHARGE_URL").getParaValue())){
                    log.error("获取爱奇艺地址接口数据异常");
                    iqiyiRechargeRes.setCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                    return iqiyiRechargeRes;
                }
                //调用爱奇艺官方接口
                rechargeUrl = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"IQIYI_RECHARGE_URL").getParaValue();
            }

            //step2.合作商户编号
            String partnerNo = null;
            if(CommonUtils.isWindows()){
                partnerNo = "SHrongshu-YLZGS_JHMZC";
            }else{
                //对data进行判空处理
                if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IQIYI_RECHARGE_PARTNER").getParaValue())) {
                    log.error("获取爱奇艺合作商户数据异常");
                    iqiyiRechargeRes.setCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                    return iqiyiRechargeRes;
                }
                //获取爱奇艺合作商户编号
                partnerNo = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IQIYI_RECHARGE_PARTNER").getParaValue();
            }
            req.setPartnerNo(partnerNo);//设置充值商户编号

            //step3.获取爱奇艺商家充值秘钥
            String key = null;
            if(CommonUtils.isWindows()){
                key = "9d458a0d3552edb1";
            }else{
                //data判空处理
                if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"IQIYI_RECHARGE_KEY").getParaValue())){
                    log.error("获取爱奇艺秘钥数据异常");
                    iqiyiRechargeRes.setCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                    return iqiyiRechargeRes;
                }
                //获取爱奇艺商家秘钥
                key = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IQIYI_RECHARGE_KEY").getParaValue();
            }

            //step4.生成签名
            String sign = MD5Util.sign(req.getUserAccount() + "_" + req.getCardCode() + "_" + req.getPartnerNo() + "_", key + "_", "utf-8");
            req.setSign(sign);

            //step5.请求爱奇艺充值接口
            Object[] obj = {req};
            String reqStr = CommonUtils.beanToString(obj, "=", "&", null, true, null);
            String resStr = HttpUtil.doPost(rechargeUrl, reqStr, "application/x-www-form-urlencoded;charset=utf-8");//post请求
            if(StringUtils.isNotBlank(resStr)){
                iqiyiRechargeRes = JSONObject.parseObject(resStr, IqiyiRechargeRes.class);
            }
        } catch (Exception e) {
            log.error("调用爱奇艺接口异常", e);
        }
        return iqiyiRechargeRes;
    }
}
