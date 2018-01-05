package com.cupdata.apigateway.filters.post;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.apigateway.util.GatewayUtils;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.Set;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

@Component
public class ResponseFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
    private OrgFeignClient orgFeignClient;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = getCurrentContext();

            HttpServletRequest request = ctx.getRequest();
            String org = request.getParameter("org"); // 机构编号

            InputStream stream = ctx.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            LOGGER.info("响应信息为" + body);

            JSONObject jsonObj = JSONObject.parseObject(body);//原始json对象

            JSONObject resultJsonObj = new JSONObject();//返回结果json对象
            resultJsonObj.put("responseCode", jsonObj.get("responseCode"));
            resultJsonObj.put("responseMsg", jsonObj.get("responseMsg"));

            //遍历data json对象
            String dataStr = String.valueOf(jsonObj.get("data"));
            JSONObject dataJsonObj = JSONObject.parseObject(dataStr);//原始json对象中data的json对象
            Set<String> keySet = dataJsonObj.keySet();
            Iterator<String> it = keySet.iterator();
            while (it.hasNext()) {
                String key = it.next();
                resultJsonObj.put(key, dataJsonObj.get(key));
            }

            //响应的明文数据
            String resultJsonStr = resultJsonObj.toJSONString();

            String encryptedResponseData = null;//加密的响应数据
            String responseDataSign = null;//响应数据签名
            //数据加密、签名
            BaseResponse<OrgInfVo> orgResponse = orgFeignClient.findOrgByNo(org);
            if (ResponseCodeMsg.SUCCESS.getCode().equals(orgResponse.getResponseCode()) && null != orgResponse.getData()) {
                OrgInfVo orgInfVo = orgResponse.getData();
                String orgPubKeyStr = orgInfVo.getOrgInf().getOrgPubKey();// 机构公钥字符串
                String sipPriKeyStr = orgInfVo.getOrgInf().getSipPriKey();// 平台私钥字符串

                //加密数据
                try {
                    // 机构公钥
                    PublicKey orgPubKey = RSAUtils.getPublicKeyFromString(orgPubKeyStr);

                    //加密明文数据
                    encryptedResponseData = RSAUtils.encrypt(resultJsonStr, orgPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
                } catch (Exception e) {
                    LOGGER.error("平台响应报文的明文加密失败！");
                    ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                    ctx.setResponseStatusCode(401);// 返回错误码
                    ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgPubKeyStr,
                            sipPriKeyStr, ResponseCodeMsg.DATA_DECRYPT_ERROR)));// 返回错误内容
                    return null;
                }

                //生成签名
                try {
                    //平台私钥
                    PrivateKey sipPriKey = RSAUtils.getPrivateKeyFromString(sipPriKeyStr);

                    //明文数据生成签名
                    responseDataSign = RSAUtils.sign(resultJsonStr, sipPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
                } catch (Exception e) {
                    LOGGER.error("平台响应报文的明文签名失败！");
                    ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                    ctx.setResponseStatusCode(401);// 返回错误码
                    ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgPubKeyStr,
                            sipPriKeyStr, ResponseCodeMsg.DATA_SIGN_ERROR)));// 返回错误内容
                    return null;
                }
            } else {
                LOGGER.error("获取机构：" + org + "信息失败！");
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());// 返回错误内容
                return null;
            }

            //生成新的body数据
            ctx.setResponseBody("data=" + URLEncoder.encode(encryptedResponseData, "utf-8") + "&sign=" + URLEncoder.encode(responseDataSign, "utf-8"));

            //修改响应的
            HttpServletResponse response = ctx.getResponse();
            response.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }
        catch (IOException e) {
            rethrowRuntimeException(e);
        }
        return null;
    }
}
