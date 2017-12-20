package com.cupdata.apigateway.filters.pre;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.commons.RSAUtils;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PreRequestFilter extends ZuulFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PreRequestFilter.class);

  @Autowired
  private OrgFeignClient orgFeignClient;

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    PreRequestFilter.LOGGER.info(String.format("send %s request to %s", request.getMethod(), request.getRequestURL().toString()));

    //Step1：获取请求参数
    String org = request.getParameter("org"); //机构编号
    String data = request.getParameter("data"); //请求参数密文
    String sign = request.getParameter("sign");//请求参数签名

    String dataPlain = null;//请求参数明文
    //Step2：解密参数密文
    BaseResponse<OrgInfVo> orgResponse = orgFeignClient.findOrgByNo(org);
    if (ResponseCodeMsg.SUCCESS.getCode().equals(orgResponse.getResponseCode())){
      OrgInfVo orgInfVo = orgResponse.getData();
      String sipPriKeyStr = orgInfVo.getOrgInf().getSipPriKey();//平台私钥字符串
      String orgPubKeyStr = orgInfVo.getOrgInf().getOrgPubKey();//机构公钥字符串

      try {
        PrivateKey sipPriKey = RSAUtils.getPrivateKeyFromString(sipPriKeyStr);//平台私钥

        dataPlain = RSAUtils.decrypt(data, sipPriKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
      }catch (Exception e){
        LOGGER.error("合作商户密文解密失败！");
        ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
        ctx.setResponseStatusCode(401);// 返回错误码

        BaseResponse responseVo = new BaseResponse();

        ctx.setResponseBody(JSONObject.toJSONString(responseVo));// 返回错误内容
        return null;
      }

      try {
        PublicKey orgPubKey = RSAUtils.getPublicKeyFromString(orgPubKeyStr);//机构公钥

        boolean isPass = RSAUtils.checkSign(dataPlain, sign, orgPubKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        if(!isPass){
          throw new Exception(ResponseCodeMsg.ILLEGAL_SIGN.getMsg());
        }
      }catch (Exception e){

      }
    }else {

    }


    //Step3：验证签名

    //Step4：重置解密之后的请求参数
    final byte[] reqBodyBytes = dataPlain.getBytes();
    ctx.setRequest(new HttpServletRequestWrapper(request) {
      @Override
      public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(reqBodyBytes);
      }

      @Override
      public int getContentLength() {
        return reqBodyBytes.length;
      }

      @Override
      public long getContentLengthLong() {
        return reqBodyBytes.length;
      }
    });

    return null;
  }
}
