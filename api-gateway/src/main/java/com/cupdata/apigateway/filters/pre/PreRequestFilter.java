package com.cupdata.apigateway.filters.pre;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class PreRequestFilter extends ZuulFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PreRequestFilter.class);

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

    //Step2：解密参数密文
//    BaseResponse<OrgInfVo>  = ;
    String dataPlain ="";

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
