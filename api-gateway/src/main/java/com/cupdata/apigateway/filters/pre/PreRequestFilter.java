package com.cupdata.apigateway.filters.pre;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.apigateway.feign.SupplierFeignClient;
import com.cupdata.apigateway.util.GatewayUtils;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.RSAUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		String sipPriKeyStr = null;//平台私钥字符串
		String orgOrSupPubKeyStr = null;//机构或者商户公钥字符串
		if (StringUtils.isNotBlank(org)){//如果为机构请求
			BaseResponse<OrgInfoVo> orgResponse = orgFeignClient.findOrgByNo(org);
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgResponse.getResponseCode()) || null == orgResponse.getData()){
				log.error("根据机构编号" + org + "，获取机构信息失败");
				ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
				ctx.setResponseStatusCode(401);// 返回错误码
				ctx.setResponseBody(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());// 返回错误内容
				return null;
			}else {
				sipPriKeyStr = orgResponse.getData().getSipPriKey();
				orgOrSupPubKeyStr = orgResponse.getData().getOrgPubKey();
			}
		}

		if (StringUtils.isNotBlank(sup)){//如果为供应商请求
			BaseResponse<SupplierInfVo> supplierResponse = supplierFeignClient.findSupByNo(sup);
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(supplierResponse.getResponseCode()) || null == supplierResponse.getData()){
				log.error("根据服务供应商编号" + sup + "，获取服务供应商信息失败");
				ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
				ctx.setResponseStatusCode(401);// 返回错误码
				ctx.setResponseBody(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());// 返回错误内容
				return null;
			}else {
				sipPriKeyStr = supplierResponse.getData().getSipPriKey();
				orgOrSupPubKeyStr = supplierResponse.getData().getSupplierPubKey();
			}
		}

		//通过密钥字符串，获取密钥
		PrivateKey sipPriKey = null;// 平台私钥
		PublicKey orgOrSupPubKey = null;// 机构公钥
		try {
			sipPriKey = RSAUtils.getPrivateKeyFromString(sipPriKeyStr);
			orgOrSupPubKey = RSAUtils.getPublicKeyFromString(orgOrSupPubKeyStr);
		}catch (Exception e){
			log.error("根据公钥、私钥字符串，获取公钥、私钥出现异常");
			ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
			ctx.setResponseStatusCode(401);// 返回错误码
			ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
					sipPriKey, ResponseCodeMsg.SYSTEM_ERROR)));// 返回错误内容
			return null;
		}

		// Step3：解密参数密文
		String dataPlain = null;// 请求参数明文
		try {
			dataPlain = RSAUtils.decrypt(data, sipPriKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
			log.info("解密明文為 " + dataPlain);
		} catch (Exception e) {
			log.error("请求报文的密文解密失败");
			ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
			ctx.setResponseStatusCode(401);// 返回错误码
			ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
					sipPriKey, ResponseCodeMsg.ENCRYPTED_DATA_ERROR)));// 返回错误内容
			return null;
		}

		// Step4：验证签名
		try {
			boolean isPass = RSAUtils.checkSign(dataPlain, sign, orgOrSupPubKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
			if (!isPass) {
				throw new Exception(ResponseCodeMsg.ILLEGAL_SIGN.getMsg());
			}
		} catch (Exception e) {
			log.error("请求报文验签失败");
			ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
			ctx.setResponseStatusCode(401);// 返回错误码
			ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
					sipPriKey, ResponseCodeMsg.ILLEGAL_SIGN)));// 返回错误内容
			return null;
		}

		//Step4：校验时间戳
		try {
			JSONObject jsonObj = JSONObject.parseObject(dataPlain);
			String timestampStr = String.valueOf(jsonObj.get("timestamp"));//获取时间戳
			//Date timestamp = DateTimeUtil.getDateByString(timestampStr.substring(0, 17), "yyyyMMddHHmmssSSS");
			//时间戳超时
			//if (!DateTimeUtil.compareTime(DateTimeUtil.getCurrentTime(), timestamp, -600 * 1000L, 120 * 1000L)){
			//	log.info("请求报文，时间戳超时");
			//	ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
			//	ctx.setResponseStatusCode(401);// 返回错误码
			//	ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
			//			sipPriKey, ResponseCodeMsg.TIMESTAMP_TIMEOUT)));// 返回错误内容
			//	return null;
			//}
		}catch (Exception e){
			log.error("", e);
			log.error("机构请求报文验签失败！");
			ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
			ctx.setResponseStatusCode(401);// 返回错误码
			ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgOrSupPubKey,
					sipPriKey, ResponseCodeMsg.TIMESTAMP_ERROR)));// 返回错误内容
			return null;
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
		ctx.getZuulRequestHeaders().put("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		return null;
	}


	@Value("${zuul.ignore-url}")
	private String ignoreUrl;

	@Value("${zuul.prefix}")
	private String zuulPrefix;

	private boolean isIgnorePath(String path) {
		for (String url : ignoreUrl.split(",")) {
			if (path.substring(zuulPrefix.length()).startsWith(url)) {

				return true;
			}
		}
		return false;
	}
}
