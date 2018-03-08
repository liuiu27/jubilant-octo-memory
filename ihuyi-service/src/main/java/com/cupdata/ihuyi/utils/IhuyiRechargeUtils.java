package com.cupdata.ihuyi.utils;

import com.alibaba.fastjson.JSON;
import com.cupdata.commons.constant.SysConfigParaNameEn;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.MD5Util;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.ihuyi.constant.IhuyiRechargeResCode;
import com.cupdata.ihuyi.feign.CacheFeignClient;
import com.cupdata.ihuyi.vo.IhuyiRechargeRes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DingCong
 * @Description: 互亿话费充值工具类
 * @CreateDate: 2018/3/6 16:50
 */

public class IhuyiRechargeUtils {

    @Autowired
    private static CacheFeignClient cacheFeignClient ;

    /**
     * 日志
     */
    protected static Logger log = Logger.getLogger(IhuyiRechargeUtils.class);

    /**
     * 互亿话费充值
     * @return
     */
    public static IhuyiRechargeRes ihuyiPhoneRecharge(RechargeOrderVo orderVo, RechargeReq rechargeReq){
        log.info("互亿话费充值工具类...");
        IhuyiRechargeRes rechargeRes = new IhuyiRechargeRes();

        //互亿话费充值url
        String domain = null ;
        if(CommonUtils.isWindows()){
            domain = "https://api.ihuyi.com/f/phone" ;
        }else {
            //如果获取数据信息为空
            if ("".equals(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getData())
                    || null == cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getData()) {
                //设置错误码:获取url失败
                rechargeRes.setMessage(IhuyiRechargeResCode.FAIL_TO_GET_URL.getMsg());
                return rechargeRes;
            }
            domain = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getData().getSysConfig().getParaValue();
        }

        //互亿api_id
        String username = null ;
        if(CommonUtils.isWindows()){
            username = "cf_testapi";
        }else {
            //如果获取数据信息为空
            if ("".equals(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getData())
                    || null == cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getData()) {
                //设置错误码:获取api_id失败
                rechargeRes.setMessage(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                return rechargeRes;
            }
            username = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getData().getSysConfig().getParaValue();
        }

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mobile = rechargeReq.getAccount();
        String orderid = orderVo.getOrder().getOrderNo();
        String packag = String.valueOf(rechargeReq.getRechargeAmt());
        String action = "recharge";

        //封装请求参数
        Map<String, String> map = new HashMap();
        map.put("mobile",rechargeReq.getAccount());
        map.put("orderid",orderid);
        map.put("package",packag);
        map.put("timestamp",timestamp);
        map.put("username",username);

        //获取签名数据
        String sign = IhuyiRechargeUtils.getSign(map);

        String url = domain+"?action="+action+"&username="+username+"&mobile="+mobile+"&orderid="+orderid+"&package="+packag+"&timestamp="+timestamp+"&sign="+sign;

        try {
            log.info("互亿话费充值订购，get请求url为："+url);
            String str = HttpUtil.doGet(url);
            if (!StringUtils.isEmpty(str)) {
                rechargeRes = JSON.parseObject(str, IhuyiRechargeRes.class);
                log.info("互亿话费充值订购，返回数据:" + str);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return rechargeRes;
    }


    /**
     * 获取签名
     * @param map
     * @return
     */
    public static String getSign(Map<String,String> map){
        String apikey = map.get("apikey");
        if (StringUtils.isEmpty(apikey)) {
            String apikeyCache = "";
            if (CommonUtils.isWindows()) {
                apikeyCache = "6j3ao593wMNQRz4Zo4ao";
            } else {
                apikeyCache = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIKEY").getData().getSysConfig().getParaValue();
            }
            map.put("apikey", apikeyCache);
        }
        String[] arr = map.keySet().toArray(new String[0]);
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        if (null != arr && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                String values =  map.get(arr[i]);
                sb.append(arr[i]).append("=").append(StringUtils.isEmpty(values)?"":values);
                if (i < arr.length - 1) {
                    sb.append("&");
                }
            }
        }
        String sign = MD5Util.encode(sb.toString());
        return sign;
    }

}
