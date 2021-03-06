package com.cupdata.apigateway.util;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.apigateway.feign.SupplierFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.RSAHelper;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.RSAUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Auth: LinYong
 * @Description: 网关工具类
 * @Date: 16:10 2017/12/20
 */
public class GatewayUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);

    /**
     * 根据机构公钥字符串，整合平台私钥字符串，错误响应码，获取响应字符串（包括data以及sign）
     * @param orgOrSupPubKey 机构公钥
     * @param sipPriKey 整合平台私钥
     * @param responseCodeMsg 响应信息
     * @return
     */
    public static String getResStrFromFailResCode(PublicKey orgOrSupPubKey, PrivateKey sipPriKey, ResponseCodeMsg responseCodeMsg) {
        StringBuffer resposeStr = new StringBuffer();
        try {
            //响应报文vo
            BaseResponse tradeBseRes = new BaseResponse();
            tradeBseRes.setResponseCode(responseCodeMsg.getCode());
            tradeBseRes.setResponseMsg(responseCodeMsg.getMsg());

            //响应字符串
            String plainData = JSONObject.toJSONString(tradeBseRes);
            //使用商户公钥字符串加密明文数据
            String encryptData = RSAUtils.encrypt(plainData, orgOrSupPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
            String data = URLEncoder.encode(encryptData, RSAUtils.UTF_8);
            //使用花积分私钥文件签名明文数据
            String sign = URLEncoder.encode(RSAUtils.sign(plainData, sipPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8), RSAUtils.UTF_8);
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

    public static void getKey(PublicKey pubKey,PrivateKey priKey,String org,String sup,OrgFeignClient orgFeignClient,SupplierFeignClient supplierFeignClient) throws Exception {
        if (StringUtils.isNotBlank(org)){//如果为机构请求
            BaseResponse<OrgInfoVo> orgResponse = orgFeignClient.findOrgByNo(org);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgResponse.getResponseCode()) || null == orgResponse.getData()) {
                //
                throw new Exception();
            }
            pubKey = RSAHelper.getPemPublicKey(orgResponse.getData().getOrgPubKey());
            priKey = RSAHelper.getPemPrivateKey(orgResponse.getData().getSipPriKey());
        }else if (StringUtils.isNotBlank(sup)){
            BaseResponse<SupplierInfVo> supplierResponse = supplierFeignClient.findSupByNo(sup);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(supplierResponse.getResponseCode()) || null == supplierResponse.getData()){
                throw new Exception();
            }
            pubKey = RSAHelper.getPemPublicKey(supplierResponse.getData().getSupplierPubKey());
            priKey = RSAHelper.getPemPrivateKey(supplierResponse.getData().getSipPriKey());
        }

    }




}
