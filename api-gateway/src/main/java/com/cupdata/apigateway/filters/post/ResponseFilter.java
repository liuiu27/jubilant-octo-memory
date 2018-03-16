package com.cupdata.apigateway.filters.post;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.apigateway.feign.SupplierFeignClient;
import com.cupdata.apigateway.util.GatewayUtils;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.orgsupplier.SupplierInfVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * 响应过滤器
 */
@Component
public class ResponseFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
    private OrgFeignClient orgFeignClient;

    @Autowired
    private SupplierFeignClient supplierFeignClient;

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
            String sup = request.getParameter("sup"); // 供应商编号
            if (isIgnorePath(request.getRequestURI()))
                return null;
            InputStream stream = ctx.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            LOGGER.info("响应信息明文为" + body);
            body = body.replaceAll(":null,", ":\"\","); 
            //Step1：转换响应参数json格式，原始json格式为{"responseCode":"","responseMsg":"","data":{}}
            JSONObject jsonObj = JSONObject.parseObject(body);//原始json对象
            JSONObject resultJsonObj = new JSONObject();//返回结果json对象
            resultJsonObj.put("responseCode", jsonObj.get("responseCode"));
            resultJsonObj.put("responseMsg", jsonObj.get("responseMsg"));

            //遍历data json对象
            String dataStr = String.valueOf(jsonObj.get("data"));
            JSONObject dataJsonObj = JSONObject.parseObject(dataStr);//原始json对象中data的json对象
            if (null != dataJsonObj){
                Set<String> keySet = dataJsonObj.keySet();
                Iterator<String> it = keySet.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    resultJsonObj.put(key, dataJsonObj.get(key));
                }
            }

            //Step2：获取机构或者供应商公钥、平台私钥
            String orgOrSupPubKeyStr = null;//机构或者商户公钥字符串
            String sipPriKeyStr = null;//平台私钥字符串

            if (StringUtils.isNotBlank(org)) {//如果为机构请求
                BaseResponse<OrgInfVo> orgResponse = orgFeignClient.findOrgByNo(org);
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgResponse.getResponseCode()) || null == orgResponse.getData()){
                    LOGGER.error("根据机构编号" + org + "，获取机构信息失败");
                    ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                    ctx.setResponseStatusCode(401);// 返回错误码
                    ctx.setResponseBody(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());// 返回错误内容
                    return null;
                }else {
                    sipPriKeyStr = orgResponse.getData().getOrgInf().getSipPriKey();
                    orgOrSupPubKeyStr = orgResponse.getData().getOrgInf().getOrgPubKey();
                }
            }

            if (StringUtils.isNotBlank(sup)){//如果为供应商请求
                BaseResponse<SupplierInfVo> supplierResponse = supplierFeignClient.findSupByNo(sup);
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(supplierResponse.getResponseCode()) || null == supplierResponse.getData()){
                    LOGGER.error("根据服务供应商编号" + sup + "，获取服务供应商信息失败");
                    ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                    ctx.setResponseStatusCode(401);// 返回错误码
                    ctx.setResponseBody(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());// 返回错误内容
                    return null;
                }else {
                    sipPriKeyStr = supplierResponse.getData().getSuppliersInf().getSipPriKey();
                    orgOrSupPubKeyStr = supplierResponse.getData().getSuppliersInf().getSupplierPubKey();
                }
            }

            //通过密钥字符串，获取密钥
            PrivateKey sipPriKey = null;// 平台私钥
            PublicKey orgOrSupPubKey = null;// 机构公钥
            try {
                sipPriKey = RSAUtils.getPrivateKeyFromString(sipPriKeyStr);
                orgOrSupPubKey = RSAUtils.getPublicKeyFromString(orgOrSupPubKeyStr);
            }catch (Exception e){
                LOGGER.error("根据公钥、私钥字符串，获取公钥、私钥出现异常");
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
                        sipPriKey, ResponseCodeMsg.SYSTEM_ERROR)));// 返回错误内容
                return null;
            }

            //Step3：加密响应报文以及签名
            String resultJsonStr = resultJsonObj.toJSONString();//实际响应的明文数据
            String encryptedResponseData = null;//加密的响应数据
            String responseDataSign = null;//响应数据签名

            //加密数据
            try {
                //加密明文数据
                encryptedResponseData = RSAUtils.encrypt(resultJsonStr, orgOrSupPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
            } catch (Exception e) {
                LOGGER.error("平台响应报文的明文加密失败！");
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
                        sipPriKey, ResponseCodeMsg.DATA_DECRYPT_ERROR)));// 返回错误内容
                return null;
            }

            //生成签名
            try {
                //明文数据生成签名
                responseDataSign = RSAUtils.sign(resultJsonStr, sipPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
            } catch (Exception e) {
                LOGGER.error("平台响应报文的明文签名失败！");
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
                        sipPriKey, ResponseCodeMsg.DATA_SIGN_ERROR)));// 返回错误内容
                return null;
            }

            String resData = URLEncoder.encode(encryptedResponseData, "utf-8");
            String resSign = URLEncoder.encode(responseDataSign, "utf-8");
            LOGGER.info("响应数据密文为data=" + resData + "&sign=" + resSign);

            //生成新的body数据
            ctx.setResponseBody("data=" + resData + "&sign=" + resSign);

            //修改响应的
            HttpServletResponse response = ctx.getResponse();
            response.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }
        catch (IOException e) {
            rethrowRuntimeException(e);
        }
        return null;
    }

    @Value("${zuul.ignore-url}")
    private String ignoreUrl;

    private boolean isIgnorePath(String path) {
        for (String url : ignoreUrl.split(",")) {
            if (path.startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}