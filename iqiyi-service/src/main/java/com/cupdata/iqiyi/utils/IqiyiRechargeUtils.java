/*
package com.cupdata.iqiyi.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.constant.SysConfigParaNameEn;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.MD5Util;
import com.cupdata.iqiyi.feign.ConfigFeignClient;
import com.cupdata.iqiyi.vo.IqiyiRechargeReq;
import com.cupdata.iqiyi.vo.IqiyiRechargeRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

*/
/**
 * @Author: DingCong
 * @Description: 爱奇艺账号充值工具类
 * @CreateDate: 2018/2/6 14:56
 *//*

@Slf4j
public class IqiyiRechargeUtils {



    */
/**
     * 爱奇艺充值业务
     *//*

    public static IqiyiRechargeRes iqiyiRecharge(IqiyiRechargeReq req, ConfigFeignClient cacheFeignClient){
        //设置响应信息
        IqiyiRechargeRes iqiyiRechargeRes = null;
        try {
            //调用爱奇艺充值接口
            log.info("调用爱奇艺充值接口,请求数据CardCode:"+req.getCardCode()+",PartnerNo:"+req.getPartnerNo()+",UserAccount:"+req.getUserAccount()+",Sign:"+req.getSign());
            String rechargeUrl = null;
            if(CommonUtils.isWindows()){
                rechargeUrl = "http://openapi.vip.iqiyi.com/partner/card-subscribe.action";
            }else{
                //对data进行判空处理
                if (CommonUtils.isNullOrEmptyOfObj(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"IQIYI_RECHARGE_URL").getData())){
                    log.error("获取爱奇艺地址接口数据异常");
                    iqiyiRechargeRes.setCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                    return iqiyiRechargeRes;
                }
                //调用爱奇艺官方接口
                rechargeUrl = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"IQIYI_RECHARGE_URL").getData().getSysConfig().getParaValue();
            }
            //合作商户编号
            String partnerNo = null;
            if(CommonUtils.isWindows()){
                partnerNo = "SHrongshu-YLZGS_JHMZC";
            }else{
                //对data进行判空处理
                if (CommonUtils.isNullOrEmptyOfObj(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IQIYI_RECHARGE_PARTNER").getData())) {
                    log.error("获取爱奇艺合作商户数据异常");
                    iqiyiRechargeRes.setCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                    return iqiyiRechargeRes;
                }
                //获取爱奇艺合作商户编号
                partnerNo = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IQIYI_RECHARGE_PARTNER").getData().getSysConfig().getParaValue();
            }
            req.setPartnerNo(partnerNo);//设置充值商户编号

            //获取爱奇艺商家充值秘钥
            String key = null;
            if(CommonUtils.isWindows()){
                key = "9d458a0d3552edb1";
            }else{
                //data判空处理
                if (CommonUtils.isNullOrEmptyOfObj(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"IQIYI_RECHARGE_KEY").getData())){
                    log.error("获取爱奇艺秘钥数据异常");
                    iqiyiRechargeRes.setCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                    return iqiyiRechargeRes;
                }
                //获取爱奇艺商家秘钥
                key = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IQIYI_RECHARGE_KEY").getData().getSysConfig().getParaValue();
            }

            //生成签名
            String sign = MD5Util.sign(req.getUserAccount() + "_" + req.getCardCode() + "_" + req.getPartnerNo() + "_", key + "_", "utf-8");
            req.setSign(sign);

            //生成请求字符串
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
*/
