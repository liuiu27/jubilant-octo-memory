package com.cupdata.ihuyi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.ihuyi.constant.IhuyiRechargeResCode;
import com.cupdata.ihuyi.feign.ConfigFeignClient;
import com.cupdata.ihuyi.vo.IhuyiOrderQueryRes;
import com.cupdata.ihuyi.vo.IhuyiRechargeRes;
import com.cupdata.ihuyi.vo.IhuyiVirtualOrderQueryRes;
import com.cupdata.ihuyi.vo.IhuyiVoucherRes;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.HttpUtil;
import com.cupdata.sip.common.lang.utils.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DingCong
 * @Description: 互亿相关充值工具类
 * @CreateDate: 2018/3/6 16:50
 */

public class IhuyiUtils {

    protected static Logger log = Logger.getLogger(IhuyiUtils.class);

    /**
     * 互亿话费充值工具类
     * @param orderVo
     * @param rechargeReq
     * @return
     */
    public static IhuyiRechargeRes ihuyiPhoneRecharge(RechargeOrderVo orderVo, RechargeReq rechargeReq, ConfigFeignClient configFeignClient) {
        log.info("调用互亿话费充值工具类...Account:" + rechargeReq.getAccount() + ",ProductNo:" + rechargeReq.getProductNo() + ",OrderDesc:" + rechargeReq.getOrderDesc());
        IhuyiRechargeRes rechargeRes = new IhuyiRechargeRes();
        //step1.获取互亿话费充值url
        String domain = null;
        if (CommonUtils.isWindows()) {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getParaValue())) {
                //打印错误日志:获取url失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_URL.getMsg());
                return rechargeRes;
            }
            domain = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getParaValue();
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL"))) {
                //打印错误日志:获取url失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_URL.getMsg());
                return rechargeRes;
            }
            domain = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getParaValue();
        }
        log.info("互亿话费充值url："+domain);

        //step2.互亿api_id
        String username = null;
        if (CommonUtils.isWindows()) {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                //设置错误码:获取api_id失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                return rechargeRes;
            }
            username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                //设置错误码:获取api_id失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                return rechargeRes;
            }
            username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
        }
        log.info("互亿话费充值api_id："+username);

        //step3.封装请求参数
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mobile = rechargeReq.getAccount();
        String orderid = orderVo.getOrderInfoVo().getOrderNo();
        String packag = String.valueOf(rechargeReq.getRechargeAmt());
        String action = "recharge";
        Map<String, String> map = new HashMap();
        map.put("mobile", mobile);
        map.put("orderid", orderid);
        map.put("package", packag);
        map.put("timestamp", timestamp);
        map.put("username", username);

        //step4.获取签名数据
        String sign = IhuyiUtils.getSign(map, configFeignClient);
        log.info("验证签名正确");
        String url = domain + "?action=" + action + "&username=" + username + "&mobile=" + mobile + "&orderid=" + orderid + "&package=" + packag + "&timestamp=" + timestamp + "&sign=" + sign;
        try {
            log.info("开始请求互亿话费充值接口");
            long l1 = System.currentTimeMillis();
            String str = HttpUtil.doGet(url);
            long l2 = System.currentTimeMillis();
            log.info("调用互亿话费充值接口耗时:"+(l2 - l1)+"ms");
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
     * 互亿流量充值工具类
     * @param rechargeReq
     * @param orderVo
     * @return
     */
    public static IhuyiRechargeRes ihuyiTrafficRecharge(RechargeOrderVo orderVo, RechargeReq rechargeReq, ConfigFeignClient configFeignClient) {
        log.info("调用互亿流量充值工具类...Account:" + rechargeReq.getAccount() + ",ProductNo:" + rechargeReq.getProductNo() + ",OrderDesc:" + rechargeReq.getOrderDesc());
        IhuyiRechargeRes rechargeRes = new IhuyiRechargeRes();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //step1.获取互亿流量充值url
        String domain = null;
        if (CommonUtils.isWindows()) {
            domain = "http://f.ihuyi.com/v2";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_TRAFFIC_RECHARGE_URL").getParaValue())) {
                //设置错误码:获取url失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_URL.getMsg());
                return rechargeRes;
            }
            domain = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_TRAFFIC_RECHARGE_URL").getParaValue();
        }
        log.info("互亿流量充值url："+domain);

        //step2.互亿api_id
        String username = null;
        if (CommonUtils.isWindows()) {
            username = "cf_testapi";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                //设置错误码:获取api_id失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                return rechargeRes;
            }
            username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
        }
        log.info("互亿流量充值api_id："+username);

        //step3.封装请求参数
        String mobile = rechargeReq.getAccount();
        String orderid = orderVo.getOrderInfoVo().getOrderNo();
        String packag = String.valueOf(rechargeReq.getRechargeTraffic());
        String action = "recharge";
        Map<String, String> map = new HashMap();
        map.put("mobile", mobile);
        map.put("orderid", orderid);
        map.put("package", packag);
        map.put("timestamp", timestamp);
        map.put("username", username);
        String sign = IhuyiUtils.getSign(map, configFeignClient);
        log.info("验证签名正确");
        String url = domain + "?action=" + action + "&username=" + username + "&mobile=" + mobile + "&orderid=" + orderid + "&package=" + packag + "&timestamp=" + timestamp + "&sign=" + sign;
        try {
            long l1 = System.currentTimeMillis();
            String str = HttpUtil.doGet(url);
            long l2 = System.currentTimeMillis();
            log.info("调用互亿流量充值接口耗时:"+(l2 - l1)+"ms");
            if (!StringUtils.isEmpty(str)) {
                rechargeRes = JSON.parseObject(str, IhuyiRechargeRes.class);
                log.info("互亿流量充值订购,返回数据:" + str);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return rechargeRes;
    }


    /**
     * @param @return 参数
     * @return IhuyiRechargeRes 返回类型
     * @throws
     * @Description 互亿虚拟商品购买
     * @Author KaiZhang
     */
    //订单编号，充值账号，商户业务参数,充值件数
    public static IhuyiRechargeRes ihuyiVirtualGoodsRechargeBuy(String orderid, String account, String productid, Long rechargeNumber, String extend, String buyerip, ConfigFeignClient configFeignClient) {
        log.info("调用互亿虚拟充值工具类...orderid:"+orderid+",account:"+account+",productid:"+productid+",rechargeNumber:"+rechargeNumber+",extend:"+extend+",buyerip:"+buyerip);
        IhuyiRechargeRes rechargeRes = new IhuyiRechargeRes();
        //step1.获取互亿虚拟充值url
        String domain = null;
        if (CommonUtils.isWindows()) {
            domain = "http://f.ihuyi.com/recharge";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VIRTUAL_PRODUCT_RECHARGE_URL").getParaValue())) {
                //设置错误码:获取url失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_URL.getMsg());
                return rechargeRes;
            }
            domain = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VIRTUAL_PRODUCT_RECHARGE_URL").getParaValue();
        }
        log.info("互亿虚拟充值url:"+domain);

        //step2.互亿api_id
        String username = null;
        if (CommonUtils.isWindows()) {
            username = "cf_testapi";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                //设置错误码:获取api_id失败
                rechargeRes.setMessage(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                return rechargeRes;
            }
            username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
        }
        log.info("互亿api_id:"+username);

        //step2.互亿虚拟充值回调url
        String callback = null;
        if (CommonUtils.isWindows()) {
            callback = "https://localhost:8040/ihuyi/ihuyiVirtualRecharge/ihuyiVirtualRechargeCallBack";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VIRTUAL_RECHARGE_CALLBACK_URL"))) {
                //设置错误码:获取api_id失败
                rechargeRes.setMessage(IhuyiRechargeResCode.FAIL_TO_GET_VIRTUAL_CALLBACK.getMsg());
                return rechargeRes;
            }
            callback = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VIRTUAL_RECHARGE_CALLBACK_URL").getParaValue();
        }
        log.info("互亿虚拟充值回调url:"+callback);

        String action = "buy";
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String quantity = String.valueOf(rechargeNumber);
        String return_ = "";
        HashMap<String, String> map = new HashMap();
        map.put("username", username);
        map.put("orderid", orderid);
        map.put("timestamp", timestamp);
        map.put("account", account);
        map.put("productid", productid);
        map.put("quantity", quantity);
        map.put("extend", extend);
        map.put("return", return_);
        map.put("callback", callback);
        map.put("buyerip", buyerip);
        String sign = getSign(map, configFeignClient);
        String url = domain + "?action=" + action + "&username=" + username +
                "&orderid=" + orderid + "&timestamp=" + timestamp + "&account=" + account +
                "&productid=" + productid + "&quantity=" + quantity + "&extend=" + extend +
                "&return=" + return_ + "&callback=" + callback + "&buyerip=" + buyerip + "&sign=" + sign;
        try {
            long l1 = System.currentTimeMillis();
            String str = HttpUtil.doGet(url);
            long l2 = System.currentTimeMillis();
            log.info("调用互亿虚拟充值接口耗时:"+(l2 - l1)+"ms");
            if (!StringUtils.isEmpty(str)) {
                rechargeRes = JSON.parseObject(str, IhuyiRechargeRes.class);
                log.info("互亿虚拟商品订购结果：" + JSON.toJSONString(rechargeRes));
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return rechargeRes;
    }

    /**
     * 互亿礼品卡购买
     * 请求参数:主订单编号,产品业务参数,手机号码
     *
     * @return
     */
    public static IhuyiVoucherRes ihuyiGiftCardBuy(VoucherOrderVo orderVo, String productid, GetVoucherReq voucherReq, ConfigFeignClient configFeignClient) {
        log.info("互亿礼品券购买工具类...");
        IhuyiVoucherRes voucherRes = new IhuyiVoucherRes();
        //step1.获取互亿礼品卡购买的url
        String domain = null;
        if (CommonUtils.isWindows()) {
            domain = "https://api.ihuyi.com/f/giftcard";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VOUCHER_EXCHANGE_URL").getParaValue())) {
                //设置错误码:获取url失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_URL.getMsg());
                return voucherRes;
            }
            domain = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VOUCHER_EXCHANGE_URL").getParaValue();
        }
        log.info("互亿礼券url:"+domain);

        //step2.互亿api_id
        String username = null;
        if (CommonUtils.isWindows()) {
            username = "cf_testapi";
        } else {
            //如果获取数据信息为空
            if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                //设置错误码:获取api_id失败
                log.error(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                return voucherRes;
            }
            username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
        }
        log.info("互亿api_id:"+username);

        //step3.封装请求参数
        String action = "buy";
        String mobile = voucherReq.getMobileNo();
        String orderid = orderVo.getOrderInfoVo().getOrderNo();
        String buynum = "1";
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Map<String, String> map = new HashMap();
        map.put("username", username);
        map.put("mobile", mobile);
        map.put("productid", productid);
        map.put("orderid", orderid);
        map.put("buynum", buynum);
        map.put("timestamp", timestamp);
        String sign = getSign(map, configFeignClient);
        String url = domain + "?action=" + action + "&username=" + username + "&mobile=" + mobile + "&orderid=" + orderid + "&timestamp=" + timestamp + "&productid=" + productid + "&buynum=" + buynum + "&sign=" + sign;
        try {
            long l1 = System.currentTimeMillis();
            String str = HttpUtil.doGet(url);
            long l2 = System.currentTimeMillis();
            log.info("调用互亿卡券接口耗时:"+(l2 - l1)+"ms");
            if (!StringUtils.isEmpty(str)) {
                log.info("互亿里礼品卡券结果为:"+str);
                voucherRes = JSON.parseObject(str, IhuyiVoucherRes.class);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("订购互亿礼品卡返回结果：" + JSON.toJSONString(voucherRes));
        return voucherRes;
    }


    /**
     * 互亿流量/话费查询
     * @return
     */
    public static IhuyiOrderQueryRes ihuyiRechargeQuery(OrderInfoVo orderInfoVo, ConfigFeignClient configFeignClient) {
        log.info("调用互亿接口去查询流量/话费...OrderNo:" + orderInfoVo.getOrderNo() + ",OrderSubType:" + orderInfoVo.getOrderSubType() + ",SupplierFlag:" + orderInfoVo.getSupplierFlag());
        IhuyiOrderQueryRes res = new IhuyiOrderQueryRes();
        String action = "getorderinfo";
        String orderid = orderInfoVo.getOrderNo(); //平台订单号
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //互亿api_id
        String username = null;
        if (CommonUtils.isWindows()) {
            username = "cf_testapi";
        } else {
            //如果获取数据信息不为空
            if (!CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
            }
        }
        log.info("互亿api_id:"+username);

        Map<String, String> map = new HashMap();
        map.put("orderid", orderid);
        map.put("username", username);
        map.put("timestamp", timestamp);
        String sign = IhuyiUtils.getSign(map, configFeignClient);
        String url = "";
        if (ModelConstants.ORDER_TYPE_RECHARGE_TRAFFIC.equals(orderInfoVo.getOrderSubType())) { //如果充值类型是流量充值
            if (CommonUtils.isWindows()) {
                url = "http://f.ihuyi.com/v2";
            } else {
                if (!CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_TRAFFIC_RECHARGE_URL").getParaValue())) {
                    url = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_TRAFFIC_RECHARGE_URL").getParaValue();
                }
            }
        } else if (ModelConstants.ORDER_TYPE_RECHARGE_PHONE.equals(orderInfoVo.getOrderSubType())) { //如果充值类型是话费充值
            if (CommonUtils.isWindows()) {
                url = "https://api.ihuyi.com/f/phone";
            } else {
                if (!CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getParaValue())) {
                    url = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_PHONE_RECHARGE_URL").getParaValue();
                }
            }
        }
        url = url + "?action=" + action + "&orderid=" + orderid + "&username=" + username + "&timestamp=" + timestamp + "&sign=" + sign;
        try {
            log.info("互亿流量/话费查询，get请求url为：" + url);
            long l1 = System.currentTimeMillis();
            String str = HttpUtil.doGet(url);
            long l2 = System.currentTimeMillis();
            log.info("调用互亿话费流量查询接口耗时:"+(l2 - l1)+"ms");
            log.info("互亿流量话费查询工具类查询结果为:"+str);
            if (!StringUtils.isEmpty(str)) {
                res = JSON.parseObject(str, IhuyiOrderQueryRes.class);
                JSONObject object = JSON.parseObject(str);
                if (null != object.get("package")) {
                    res.setSize((Integer) object.get("package"));
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("互亿流量/话费查询返回结果：" + JSON.toJSONString(res));
        return res;
    }


    /**
     * 互亿虚拟充值订单查询
     *
     * @param orderInfoVo
     * @return
     */
    public static IhuyiVirtualOrderQueryRes virtualGoodsRechargeQuery(OrderInfoVo orderInfoVo, ConfigFeignClient configFeignClient) {
        IhuyiVirtualOrderQueryRes res = new IhuyiVirtualOrderQueryRes();
        String action = "getorderinfo";
        String orderid = orderInfoVo.getOrderNo();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //互亿api_id
        String username = null;
        if (CommonUtils.isWindows()) {
            username = "cf_testapi";
        } else {
            //如果获取数据信息不为空
            if (!CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue())) {
                username = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIID").getParaValue();
            }
        }
        Map<String, String> map = new HashMap();
        map.put("orderid", orderid);
        map.put("username", username);
        map.put("timestamp", timestamp);
        String sign = IhuyiUtils.getSign(map, configFeignClient);
        String url = null;
        if (CommonUtils.isWindows()) {
            url = "http://f.ihuyi.com/recharge";
        } else {
            //如果获取数据信息为空
            if (!CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VIRTUAL_PRODUCT_RECHARGE_URL"))) {
                url = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_VIRTUAL_PRODUCT_RECHARGE_URL").getParaValue();
            }
        }
        url = url + "?action=" + action + "&orderid=" + orderid + "&username=" + username + "&timestamp=" + timestamp + "&sign=" + sign;
        try {
            long l1 = System.currentTimeMillis();
            String str = HttpUtil.doGet(url);
            long l2 = System.currentTimeMillis();
            log.info("调用互亿虚拟充值接口耗时:"+(l2 - l1)+"ms");
            if (!StringUtils.isEmpty(str)) {
                res = JSON.parseObject(str, IhuyiVirtualOrderQueryRes.class);
                JSONObject object = JSON.parseObject(str);
                if (null != object.get("return")) {
                    res.setReturn_(String.valueOf(object.get("return")));
                }
                log.info(str);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("查询互亿虚拟商品结果：" + JSON.toJSONString(res));
        return res;
    }


    /**
     * 获取签名
     * @param map
     * @return
     */
    public static String getSign(Map<String, String> map, ConfigFeignClient configFeignClient) {
        String apikey = map.get("apikey");
        if (StringUtils.isEmpty(apikey)) {
            String apikeyCache = "";
            if (CommonUtils.isWindows()) {
                apikeyCache = "6j3ao593wMNQRz4Zo4ao";
            } else {
                apikeyCache = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIKEY").getParaValue();
            }
            map.put("apikey", apikeyCache);
        }
        String[] arr = map.keySet().toArray(new String[0]);
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        if (null != arr && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                String values = map.get(arr[i]);
                sb.append(arr[i]).append("=").append(StringUtils.isEmpty(values) ? "" : values);
                if (i < arr.length - 1) {
                    sb.append("&");
                }
            }
        }
        String sign = MD5Util.encode(sb.toString());
        return sign;
    }

}
