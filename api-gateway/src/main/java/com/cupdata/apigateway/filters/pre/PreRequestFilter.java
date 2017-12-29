package com.cupdata.apigateway.filters.pre;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.apigateway.feign.OrgFeignClient;
import com.cupdata.apigateway.util.GatewayUtils;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

public class PreRequestFilter extends ZuulFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(PreRequestFilter.class);

	@Autowired
	private OrgFeignClient orgFeignClient;

	private String dataPlain = null;// 请求参数明文

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

	private class ModifyParametersWrapper extends HttpServletRequestWrapper {
		private Map<String, String[]> parameterMap; // 所有参数的Map集合

		public ModifyParametersWrapper(HttpServletRequest request) {
			super(request);
			parameterMap = request.getParameterMap();
		}

		// 重写几个HttpServletRequestWrapper中的方法
		/**
		 * 获取所有参数名
		 *
		 * @return 返回所有参数名
		 */
		@Override
		public Enumeration<String> getParameterNames() {
			Vector<String> vector = new Vector<String>(parameterMap.keySet());
			return vector.elements();
		}

		/**
		 * 获取指定参数名的值，如果有重复的参数名，则返回第一个的值 接收一般变量 ，如text类型
		 *
		 * @param name
		 *            指定参数名
		 * @return 指定参数名的值
		 */
		@Override
		public String getParameter(String name) {
			String[] results = parameterMap.get(name);
			if (results == null || results.length <= 0)
				return null;
			else {
				System.out.println("修改之前： " + results[0]);
				return modify(results[0]);
			}
		}

		/**
		 * 获取指定参数名的所有值的数组，如：checkbox的所有数据 接收数组变量 ，如checkobx类型
		 */
		@Override
		public String[] getParameterValues(String name) {
			String[] results = parameterMap.get(name);
			if (results == null || results.length <= 0)
				return null;
			else {
				int length = results.length;
				for (int i = 0; i < length; i++) {
					System.out.println("修改之前2： " + results[i]);
					results[i] = modify(results[i]);
				}
				return results;
			}
		}

		/**
		 * 自定义的一个简单修改原参数的方法，即：给原来的参数值前面添加了一个修改标志的字符串
		 *
		 * @param string
		 *            原参数值
		 * @return 修改之后的值
		 */
		private String modify(String string) {

			return dataPlain + string;
		}
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		PreRequestFilter.LOGGER
				.info(String.format("send %s request to %s", request.getMethod(), request.getRequestURL().toString()));

		// Step1：获取请求参数
		String org = request.getParameter("org"); // 机构编号
		String data = request.getParameter("data"); // 请求参数密文
		String sign = request.getParameter("sign");// 请求参数签名
		LOGGER.info("机构编号" + org + "，请求密文" + data + "，签名" + sign);

		// Step2：解密参数密文
		// String dataPlain = null;//请求参数明文
		BaseResponse<OrgInfVo> orgResponse = orgFeignClient.findOrgByNo(org);
		if (ResponseCodeMsg.SUCCESS.getCode().equals(orgResponse.getResponseCode()) && null != orgResponse.getData()) {
			OrgInfVo orgInfVo = orgResponse.getData();
			String sipPriKeyStr = orgInfVo.getOrgInf().getSipPriKey();// 平台私钥字符串
			String orgPubKeyStr = orgInfVo.getOrgInf().getOrgPubKey();// 机构公钥字符串

			try {
				PrivateKey sipPriKey = RSAUtils.getPrivateKeyFromString(sipPriKeyStr);// 平台私钥

				dataPlain = RSAUtils.decrypt(data, sipPriKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
			} catch (Exception e) {
				LOGGER.error("机构请求报文的密文解密失败！");
				ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
				ctx.setResponseStatusCode(401);// 返回错误码
				ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgPubKeyStr,
						sipPriKeyStr, ResponseCodeMsg.ENCRYPTED_DATA_ERROR)));// 返回错误内容
				return null;
			}

			// Step3：验证签名
			try {
				PublicKey orgPubKey = RSAUtils.getPublicKeyFromString(orgPubKeyStr);// 机构公钥

				boolean isPass = RSAUtils.checkSign(dataPlain, sign, orgPubKey, RSAUtils.SIGN_ALGORITHMS_MGF1,
						RSAUtils.UTF_8);
				if (!isPass) {
					throw new Exception(ResponseCodeMsg.ILLEGAL_SIGN.getMsg());
				}
			} catch (Exception e) {
				LOGGER.error("机构请求报文验签失败！");
				ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
				ctx.setResponseStatusCode(401);// 返回错误码
				ctx.setResponseBody(JSONObject.toJSONString(GatewayUtils.getResStrFromFailResCode(orgPubKeyStr,
						sipPriKeyStr, ResponseCodeMsg.ILLEGAL_SIGN)));// 返回错误内容
				return null;
			}
		} else {
			LOGGER.error("获取机构：" + org + "信息失败！");
			ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
			ctx.setResponseStatusCode(401);// 返回错误码
			ctx.setResponseBody(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());// 返回错误内容
			return null;
		}
		System.out.println(1);
		ModifyParametersWrapper mParametersWrapper = new ModifyParametersWrapper(request);
		mParametersWrapper.getParameterValues("org");
		ctx.setRequest(mParametersWrapper);
		// Step4：重置解密之后的请求参数
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
