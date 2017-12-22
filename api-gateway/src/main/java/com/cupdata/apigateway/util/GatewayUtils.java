package com.cupdata.apigateway.util;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 16:10 2017/12/20
 */
public class GatewayUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);

    /**
     * 根据机构公钥字符串，整合平台私钥字符串，错误响应码，获取响应字符串（包括data以及sign）
     * @param orgPubKeyStr 机构公钥字符串
     * @param sipPriKeyStr 整合平台私钥字符串
     * @param responseCodeMsg 响应信息
     * @return
     */
    public static String getResStrFromFailResCode(String orgPubKeyStr, String sipPriKeyStr, ResponseCodeMsg responseCodeMsg) {
        StringBuffer resposeStr = new StringBuffer();
        try {
            //响应报文vo
            BaseResponse tradeBseRes = new BaseResponse();
            tradeBseRes.setResponseCode(responseCodeMsg.getCode());
            tradeBseRes.setResponseMsg(responseCodeMsg.getMsg());

            //响应字符串
            String plainData = JSONObject.toJSONString(tradeBseRes);
            //使用商户公钥字符串加密明文数据
            String encryptData = RSAUtils.encrypt(plainData, RSAUtils.getPublicKeyFromString(orgPubKeyStr), RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
            String data = URLEncoder.encode(encryptData, RSAUtils.UTF_8);
            //使用花积分私钥文件签名明文数据
            String sign = URLEncoder.encode(RSAUtils.sign(plainData, RSAUtils.getPrivateKeyFromString(sipPriKeyStr), RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8), RSAUtils.UTF_8);
            //响应字符串
            resposeStr = new StringBuffer("data=");
            resposeStr.append(data);
            resposeStr.append("&sign=");
            resposeStr.append(sign);
        }catch (Exception e){
            LOGGER.error("根据错误响应码，获取响应信息出现异常", e);
        }
        return resposeStr.toString();
    }
}