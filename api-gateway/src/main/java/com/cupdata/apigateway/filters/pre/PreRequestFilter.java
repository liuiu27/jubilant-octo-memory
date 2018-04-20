package com.cupdata.apigateway.filters.pre;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.apigateway.feign.SupplierFeignClient;
import com.cupdata.apigateway.util.GatewayUtils;
import com.cupdata.sip.common.lang.RSAHelper;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.*;

/**
 * 请求过滤器
 */
@Slf4j
public class PreRequestFilter extends ZuulFilter {

	@Autowired
	private OrgFeignClient orgFeignClient;//机构Feign

	@Autowired
	private SupplierFeignClient supplierFeignClient;//商户feign

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
		log.info(String.format("send %s request to %s", request.getMethod(), request.getRequestURL().toString()));

		// 不需要进行网关处理的请求
		if (isIgnorePath(request.getRequestURI())){
			return null;
		}

		// Step1：获取请求参数
		String org = request.getParameter("org"); // 机构编号
		String sup = request.getParameter("sup");//供应商编号
		String data = request.getParameter("data"); // 请求参数密文
		String sign = request.getParameter("sign");// 请求参数签名
		log.info("sup:" + sup + ", org:" + org + ", data" + data + ", sign" + sign);

		//Step2：根据供应商编号或者机构编号，获取秘钥
	    //平台私钥字符串
        PrivateKey priKey = null;
        //机构或者商户公钥字符串
		PublicKey pubKey = null;

        // Step3：解密参数密文
        String dataPlain =null;
        try {
            GatewayUtils.getKey(pubKey,priKey,org,sup,orgFeignClient,supplierFeignClient);
            // 请求参数明文
            dataPlain=  RSAHelper.decipher(data,priKey,117);
            if (StringUtils.isBlank(data)||StringUtils.isBlank(sign)||StringUtils.isBlank(dataPlain)) throw new Exception();

        } catch (Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ctx.setResponseBody("");
            return null;
        }

        if (checkTimestamp){
            try {
                JSONObject jsonObj = JSONObject.parseObject(dataPlain);
                //获取时间戳
                String timestampStr = String.valueOf(jsonObj.get("timestamp"));
                Date timestamp = null;
                timestamp = DateUtils.parseDate(timestampStr.substring(0, 17), "yyyyMMddHHmmssSSS");

            } catch (Exception e) {

                log.info("请求报文，时间戳超时");
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                return null;
            }
        }

        // Step4：重置解密之后的请求参数
        final byte[] reqBodyBytes = dataPlain.getBytes();

        ctx.setRequest(new HttpServletRequestWrapper(request) {

			@Override
			public String getContentType() {
				return MediaType.APPLICATION_JSON_UTF8_VALUE;
			}
			@Override
			public ServletInputStream getInputStream(){
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


		Map<String, List<String>> pa = ctx.getRequestQueryParams();
		if (null == pa){
			pa = new HashMap<>();
		}
		List<String> p = new ArrayList<>();
		if (StringUtils.isNotBlank(org)){//如果为机构请求
			p.add(org);
			pa.put("org", p);
		}else if (StringUtils.isNotBlank(sup)){//如果为供应商请求
			p.add(sup);
			pa.put("sup", p);
		}
		ctx.setRequestQueryParams(pa);
		ctx.getZuulRequestHeaders().put("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		return null;
	}


	@Value("${zuul.ignore-url}")
	private String ignoreUrl;

	@Value("${zuul.prefix}")
	private String zuulPrefix;

    @Value("${zuul.check-timestamp:false}")
    private boolean checkTimestamp;

	private boolean isIgnorePath(String path) {
		for (String url : ignoreUrl.split(",")) {
			if (path.substring(zuulPrefix.length()).startsWith(url)) {

				return true;
			}
		}
		return false;
	}



}
