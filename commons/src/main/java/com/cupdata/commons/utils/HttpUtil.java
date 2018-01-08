package com.cupdata.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.vo.trvok.TrvokAirportReq;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;

public class HttpUtil {
	private static Logger log =LoggerFactory.getLogger(HttpUtil.class);

	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";
	private static final String DEFAULT_CHARSET = "utf-8";

	private static myX509TrustManager XTM = new myX509TrustManager();
	private static myHostnameVerifier HNV = new myHostnameVerifier();

	/**
	 * get请求方法 编码方式为utf-8 
	 * 1
	 * 
	 * @param url
	 *            请求uel地址
	 * @return
	 */
	public static String doGet(String url) throws Exception {
		log.info("调用HttpUtil.doGet()请求的url为" + url);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		HttpURLConnection connection = null;
		try {
			URL getUrl = new URL(url); // 把字符串转换为URL请求地址
			connection = (HttpURLConnection) getUrl.openConnection();// 打开连接

			// connection.addRequestProperty("User-Agent", "Mozilla/4.76");
			connection.connect();// 连接会话

			// 获取输入流
			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), DEFAULT_CHARSET));
			String line;
			while ((line = br.readLine()) != null) {// 循环读取流
				sb.append(line);
			}
			br.close();// 关闭流
			connection.disconnect();// 断开连接
			if(sb.toString().length()<150){
				log.info("调用HttpUtil.doGet()请求，返回的字符串为" + sb.toString());
			}	
			return sb.toString();
		} catch (Exception e) {
			log.info("调用HttpUtil.doGet()请求出现异常！",e);
			throw e;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (IOException e) {
				br = null;
				connection = null;
			}
		}
	}

	/**
	 * post请求 - HttpURLConnection的contentType属性值为[text/html]
	 * 
	 * @param url
	 *            请求url地址
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String doPost(String url, String params) {
		return doPost(url, params, "text/html");
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param params
	 *            请求参数
	 * @param contentType
	 *            HttpURLConnection的contentType属性值，例如：application/json或者text/
	 *            html等
	 *            系统框架的contentType默认值为application/x-www-form-urlencoded;charset
	 *            =UTF-8
	 * @return
	 */
	public static String doPost(String url, String params, String contentType) {
		log.info("调用HttpUtil.doPost()请求的URL:" + url);
		log.info("调用HttpUtil.doPost()请求的参数:" + params);
		StringBuffer stringBuffer = new StringBuffer();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferReader = null;
		HttpURLConnection httpUrlCon = null;
		HttpsURLConnection httpsUrlCon = null;
		try {
			Date date1 = new Date();

			// 如果是https请求
			if (url.indexOf("https") >= 0) {
				SSLContext sslContext = null;
				sslContext = SSLContext.getInstance("TLS"); // 或SSL
				X509TrustManager[] xtmArray = new X509TrustManager[] { XTM };
				sslContext.init(null, xtmArray,
						new java.security.SecureRandom());
				if (sslContext != null) {
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
							.getSocketFactory());
				}
				HttpsURLConnection.setDefaultHostnameVerifier(HNV);
				httpsUrlCon = (HttpsURLConnection) (new URL(url))
						.openConnection();
				httpsUrlCon.setRequestProperty("content-type", contentType);
				httpsUrlCon.setDoOutput(true);
				httpsUrlCon.setRequestMethod(METHOD_POST);
				httpsUrlCon.setUseCaches(false);
				httpsUrlCon.setDoInput(true);
				httpsUrlCon.getOutputStream().write(
						params.getBytes(DEFAULT_CHARSET));
				httpsUrlCon.getOutputStream().flush();
				httpsUrlCon.getOutputStream().close();
				inputStream = httpsUrlCon.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream,
						DEFAULT_CHARSET);
				bufferReader = new BufferedReader(inputStreamReader);
				String line = "";
				while ((line = bufferReader.readLine()) != null) {
					stringBuffer.append(line);
				}
			} else {// 如果是http请求
				httpUrlCon = (HttpURLConnection) new URL(url).openConnection();
				httpUrlCon.setRequestProperty("content-type", contentType);
				httpUrlCon.setDoInput(true);
				httpUrlCon.setDoOutput(true);
				httpUrlCon.setRequestMethod(METHOD_POST);
				httpUrlCon.setConnectTimeout(100000);
				httpUrlCon.setUseCaches(false);
				OutputStream out = httpUrlCon.getOutputStream();
				out.write(params.getBytes(DEFAULT_CHARSET));
				out.flush();
				out.close();
				int status = httpUrlCon.getResponseCode();
				log.info("调用HttpUtil.doPost()响应状态为:" + status);
				inputStream = httpUrlCon.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream,
						DEFAULT_CHARSET);
				bufferReader = new BufferedReader(inputStreamReader);
				String line = "";
				while ((line = bufferReader.readLine()) != null) {
					stringBuffer.append(line);
				}
			}

			Date date2 = new Date();
			log.info("Post响应信息为" + stringBuffer.toString());
			log.info("Post请求花费" + (date2.getTime() - date1.getTime()) + "ms");
			return stringBuffer.toString();
		} catch (Exception e) {
			log.info("调用HttpUtil.doPost()请求出现异常！", e);
			return "";
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (bufferReader != null) {
					bufferReader.close();
				}
				if (httpUrlCon != null) {
					httpUrlCon.disconnect();
				}
				if (httpsUrlCon != null) {
					httpsUrlCon.disconnect();
				}
			} catch (IOException e) {
				inputStream = null;
				inputStreamReader = null;
				bufferReader = null;
			}
		}
	}

	/**
	 * main方法
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		//获取区域测试
//		doPost("http://cvpa.leagpoint.com/cvpService/trip/getArea",
//		"bankCode=0305&orgNo=&reqJson=I0n6z2Ybnvz8XcU_vnOa3QWsmJEcwbDm5FAl3BFrvLfXO6LMiTshN5X67JBoPQsDcq5OT-X-SU7TbV3aitRnxHwu6rKysHQzCXQMCOWhGSuAkgrXDd9-5jWVCUnDRU2TepBP-KzzVymuPJxlGnHOLQ.."
//		,"application/x-www-form-urlencoded;charset=UTF-8");
//		
//		//获取机场详情测试
////		doPost("http://192.168.100.212:9190/cvpService/trip/getTicketDet", 
////		"bankCode=0305&orgNo=&reqJson=3L_xIWMZmTBx0z9oX9InNGVqTHAl04DEvMwLp0LoDpxOB3EM_JBtlo1b5N5CGrIT3K5K29YKZ3_kHCFTmOrnRluSG4AAnmpKhcDSDzP6XZVARvZQbBsjzOk4ZqKax00t8TYaszsP-R1d4IN4nlmWZXnLUx2bzFDL-oaEAvjrQTU.","application/x-www-form-urlencoded;charset=UTF-8");
//////		
////		
////		
////		
//////		//获取券码测试305&orgNo=&reqJson=I0n6z2Ybnvz8XcU_vnOa3QuZxXBK0Knp9TrxXlpJ9_s_iXZ2Y2p1zmmGPg23LpvcAoaGEg4nrfaRBgf0Opj9HzEzQ7jNCY7GW5IbgACeakpVOfPEx0oqVvaKFVbr22YhDQ3D3hqxJeGiD8JdMSORlEZX3-IUzgCcKmta7G6NMSI.","application/x-www-form-urlencoded;charset=UTF-8");
////		doPost("http://192.168.100.212:9190/cvpService/trip/getRedeemInfo", 
////		"bankCode=0305&orgNo=&reqJson=I0n6z2Ybnvz8XcU_vnOa3QuZxXBK0Knp9TrxXlpJ9_vYTYJu4Ln_P0C7FISU1DZPjVvk3kIashPcrkrb1gpnf-apKLMuCtE5W5IbgACeakqFwNIPM_pdlUBG9lBsGyPMnB_qrfJGn9frJhbcCVNJ4HhS1TtnxGhgc3nMgkMHErKPfLM66CpCpg..","application/x-www-form-urlencoded;charset=UTF-8");
	
		String pubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
		PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
//		String reqStr = "{\"loginFlag\":\"1\",\"mobileNo\":\"15857128524\",\"timestamp\":\"20170928135227836RE3I3YN0\"}";
		String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
		
//		TrvokAreaReq areaReq = new TrvokAreaReq();
//		areaReq.setAreaType("1");
//		areaReq.setTimestamp(timestamp);
//		
		
//		TrvokAirportReq trvokAirportReq = new TrvokAirportReq();
//		trvokAirportReq.setAirportId("504");
//		trvokAirportReq.setAreaType("1");
//		trvokAirportReq.setTimestamp(timestamp);
				
		
		GetVoucherReq getVoucherReq = new GetVoucherReq();
		getVoucherReq.setTimestamp(timestamp);
		getVoucherReq.setExpire("20180109");
		getVoucherReq.setProductNo("20180108V124");
		getVoucherReq.setOrgOrderNo("132132131");
		getVoucherReq.setOrderDesc("空港测试");
		
		
		String reqStr = JSONObject.toJSONString(getVoucherReq);
		
		String reqData = RSAUtils.encrypt(reqStr, uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
		reqData = URLEncoder.encode(reqData);
		
		String merchantPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
		PrivateKey merchantPriKey = RSAUtils.getPrivateKeyFromString(merchantPriKeyStr);
		String authReqSign = RSAUtils.sign(reqStr, merchantPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
		authReqSign = URLEncoder.encode(authReqSign);
		
		/*reqData="ElHn y68R/ HXvBQPcjFhdz/sjKORVOUi7yBeCPnkVQcISasQ5nuJbdcMHxog7 udw6HPHOihdqSgE0s8JN eF3mboA1Na/1FEkhiIQ3tn1zQ3vgzIAp3wJzpb/rDpJ5 ae68GbuvkKu6pRht4E/0ifvHXeMfToY2d9XTnFWNC4=，签名khIyKMh8qNJvHf5tApJhZ65J4YBCE4XLz1v0NIZRckOrD1XaWs0pfgyShM6zkQqupMS/L/DrhT4avjL2VirKAESAJq7P9JbzNhrUgQ02yrNsE/wT9rTc0mh4vavfwz8hUjeudXMUPqMncvIxXokJ/XwRKEt5OG0xABGdVlVA9DI=";
		PrivateKey sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");//平台私钥
*/
/*	    String dataPlain = RSAUtils.decrypt(reqData, sipPriKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
	     
		System.out.println(dataPlain);*/
		
		doPost("http://localhost:8040/voucher/voucher/getVoucher", "org=0000000120170203&data=" + reqData + 
				"&sign=" + authReqSign ,
				"application/x-www-form-urlencoded;charset=UTF-8");
	
	}

	public static void printByteLength(String charsetName) {
		//doPost("http://127.0.0.1:8080/cvpService/trip/getRedeemInfo", "bankCode=0
		String a = "a"; // 一个英文字符
		String b = "啊"; // 一个中文字符
		try {
			System.out.println(charsetName + "编码英文字符所占字节数:"
					+ a.getBytes(charsetName).length);
			System.out.println(charsetName + "编码中文字符所占字节数:"
					+ b.getBytes(charsetName).length);
			System.out.println();
		} catch (UnsupportedEncodingException e) {
			System.out.println("非法编码格式！");
		}
	}

}

/** */
/**
 * 重写三个方法
 * 
 * @author Administrator
 * 
 */
class myX509TrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) {
		System.out.println("cert: " + chain[0].toString() + ", authType: "
				+ authType);
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}

/** */
/**
 * 重写一个方法
 * 
 * @author Administrator
 * 
 */
class myHostnameVerifier implements HostnameVerifier {

	public boolean verify(String hostname, SSLSession session) {
		System.out.println("Warning: URL Host: " + hostname + " vs. "
				+ session.getPeerHost());
		return true;
	}
}