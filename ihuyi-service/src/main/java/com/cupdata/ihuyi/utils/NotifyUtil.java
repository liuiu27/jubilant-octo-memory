package com.cupdata.ihuyi.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 互亿充值异步通知工具类
 */
public class NotifyUtil {

    protected static Logger log = Logger.getLogger(NotifyUtil.class);

    /**
     * 通知
     * @param serviceOrder
     * @param orgInfVo
     * @return
     */
    public static Boolean httpToOrg(ServiceOrder serviceOrder, OrgInfVo orgInfVo){
        log.info("OrderNo:"+serviceOrder.getOrderNo()+",OrgOrderNo:"+serviceOrder.getOrgOrderNo()+",OrderDesc:"+serviceOrder.getOrderDesc()+",NotifyUrl:"+serviceOrder.getNotifyUrl());
        Boolean bl = false;
        try {
            //封装充值业务通知vo
            RechargeNotifyToOrgVo rechargeNotifyToOrgVo = new RechargeNotifyToOrgVo();
            rechargeNotifyToOrgVo.setOrderNo(serviceOrder.getOrderNo());            //平台订单号
            rechargeNotifyToOrgVo.setOrgOrderNo(serviceOrder.getOrgOrderNo());      //机构唯一订单号
            rechargeNotifyToOrgVo.setRechargeStatus(serviceOrder.getOrderStatus()); //订单状态
            String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), TimeConstants.DATE_PATTERN_5) + CommonUtils.getCharAndNum(8);
            rechargeNotifyToOrgVo.setTimestamp(timestamp);    //时间戳

            //获取秘钥 加密 请求参数
            String pubKeyStr = orgInfVo.getOrgInf().getOrgPubKey();
            PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
            String reqStr = JSONObject.toJSONString(rechargeNotifyToOrgVo);
            String reqData = RSAUtils.encrypt(reqStr, uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
            reqData = URLEncoder.encode(reqData);
            String merchantPriKeyStr = orgInfVo.getOrgInf().getSipPriKey();
            PrivateKey merchantPriKey = RSAUtils.getPrivateKeyFromString(merchantPriKeyStr);
            String authReqSign = RSAUtils.sign(reqStr, merchantPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
            authReqSign = URLEncoder.encode(authReqSign);

            //发送请求
            log.info("dopost request url is " + serviceOrder.getNotifyUrl() + "data is  " +
                    reqData + "sign is " + authReqSign);
            String resStr = HttpUtil.doPost(serviceOrder.getNotifyUrl(), "data=" + reqData + "&sign=" + authReqSign ,
                    "application/x-www-form-urlencoded;charset=UTF-8");
            if("SUCCESS".equals(resStr)) {
                bl = true;
            }
        } catch (Exception e) {
            log.error("httpToOrg is error " + e.getMessage());
        }
        return bl;
    }

    /**
     * 初始化  OrderNotifyComplete
     * @param orderNo
     */
    public static OrderNotifyComplete initOrderNotifyComplete(String orderNo,String notifyUrl) {
        OrderNotifyComplete orderNotifyComplete = new OrderNotifyComplete();
        orderNotifyComplete.setOrderNo(orderNo);
        orderNotifyComplete.setNotifyUrl(notifyUrl);
        orderNotifyComplete.setCompleteDate(GetNextTime(1,DateTimeUtil.getCurrentTime()));
        orderNotifyComplete.setNotifyTimes(1);
        orderNotifyComplete.setNotifyStatus(ModelConstants.NOTIFY_STATUS_SUCCESS);
        orderNotifyComplete.setNodeName(getNodeName());
        return orderNotifyComplete;
    }


    /**
     * 根据通知次数获取下次通知时间
     * @param notifyTimes
     * @return
     */
    public static Date GetNextTime(Integer notifyTimes, Date time) {
        if(null == notifyTimes || null == time) {
            return null;
        }
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        map.put(1, 1);
        map.put(2, 1);
        map.put(3, 3);
        map.put(4, 10);
        map.put(5, 30);
        map.put(6, 30);
        map.put(7, 30);
        map.put(8, 30);
        map.put(9, 60);
        if(!map.containsKey(notifyTimes)) {
            return null;
        }
        Integer minutes = map.get(notifyTimes);
        return  DateTimeUtil.addMinute(time, minutes);
    }

    /**
     * 获取nodeName IP 加 端口号
     * @return
     */
    public static String getNodeName() {

        return CommonUtils.getHostAddress();
    }

    /**
     * 初始化  OrderNotifyWait
     * @param orderNo
     */
    public static OrderNotifyWait initOrderNotifyWait(String orderNo, String notifyUrl) {
        OrderNotifyWait orderNotifyWait = new OrderNotifyWait();
        orderNotifyWait.setOrderNo(orderNo);
        orderNotifyWait.setNotifyUrl(notifyUrl);
        orderNotifyWait.setNextNotifyDate(GetNextTime(1,DateTimeUtil.getCurrentTime()));
        orderNotifyWait.setNotifyTimes(1);
        orderNotifyWait.setNotifyStatus(ModelConstants.NOTIFY_STATUS_FAIL);
        orderNotifyWait.setNodeName(getNodeName());
        return orderNotifyWait;
    }


}
