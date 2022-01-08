package com.cupdata.commons.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @ClassName: HttpUtils
 * @Description: http工具类
 * @author LinYong
 * @date 2016年9月27日 下午4:26:54
 *
 */
public class HttpUtil {
    /**
     * 日志
     */
    protected static Logger log = Logger.getLogger(HttpUtil.class);

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String DEFAULT_CHARSET = "utf-8";

    private static myX509TrustManager XTM = new myX509TrustManager();
    private static myHostnameVerifier HNV = new myHostnameVerifier();

    /**
     * get请求方法
     * 编码方式为utf-8
     * @param url 请求uel地址
     * @return
     */
    public static String doGet(String url) throws Exception{
        log.info("调用HttpUtil.doGet()请求的url为" + url);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        HttpURLConnection connection = null;
        try {
            URL getUrl = new URL(url); // 把字符串转换为URL请求地址
            connection = (HttpURLConnection) getUrl.openConnection();// 打开连接

//			connection.addRequestProperty("User-Agent", "Mozilla/4.76");
            connection.connect();// 连接会话

            // 获取输入流
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
            log.info("调用HttpUtil.doGet()请求，返回的字符串为" + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            log.info("调用HttpUtil.doGet()请求出现异常！");
            e.printStackTrace();
            throw e;
        }finally{
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
     * @param url 请求url地址
     * @param params 请求参数
     * @return
     */
    public static String doPost(String url,String params) throws Exception{
        return doPost(url, params, "text/html");
    }

    /**
     * post请求
     * @param url url地址
     * @param params 请求参数
     * @param contentType HttpURLConnection的contentType属性值，例如：application/json或者text/html等
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws MalformedURLException
     * @throws KeyManagementException
     */
    public static String doPost(String url,String params, String contentType) throws Exception{
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

            //如果是https请求
            if(url.indexOf("https") >=0 ){
                SSLContext sslContext = null;
                sslContext = SSLContext.getInstance("TLS"); //或SSL
                X509TrustManager[] xtmArray = new X509TrustManager[] {XTM};
                sslContext.init(null, xtmArray, new java.security.SecureRandom());
                if (sslContext != null) {
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                }
                HttpsURLConnection.setDefaultHostnameVerifier(HNV);
                httpsUrlCon = (HttpsURLConnection)(new URL(url)).openConnection();
                httpsUrlCon.setRequestProperty("content-type", contentType);
                httpsUrlCon.setDoOutput(true);

                httpsUrlCon.setRequestMethod(METHOD_POST);
                httpsUrlCon.setConnectTimeout(30000);
                httpsUrlCon.setUseCaches(false);
                httpsUrlCon.setDoInput(true);
                httpsUrlCon.getOutputStream().write(params.getBytes(DEFAULT_CHARSET));
                httpsUrlCon.getOutputStream().flush();
                httpsUrlCon.getOutputStream().close();
                inputStream = httpsUrlCon.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, DEFAULT_CHARSET);
                bufferReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            }else{//如果是http请求
                httpUrlCon = (HttpURLConnection) new URL(url).openConnection();
                httpUrlCon.setRequestProperty("content-type", contentType);
                httpUrlCon.setRequestProperty("content-length", String.valueOf(params.getBytes().length));
                httpUrlCon.setDoInput(true);
                httpUrlCon.setDoOutput(true);
                httpUrlCon.setRequestMethod(METHOD_POST);
                httpUrlCon.setConnectTimeout(30000);
                httpUrlCon.setUseCaches(false);
                OutputStream out = httpUrlCon.getOutputStream();
                out.write(params.getBytes(DEFAULT_CHARSET));
                out.flush();
                out.close();
                int status = httpUrlCon.getResponseCode();
                log.info("调用HttpUtil.doPost()响应状态为:" + status);
                inputStream = httpUrlCon.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, DEFAULT_CHARSET);
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
        } catch(Exception e){
            log.info("调用HttpUtil.doPost()请求出现异常！", e);
            e.printStackTrace();
            throw e;
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
                if(httpUrlCon != null){
                    httpUrlCon.disconnect();
                }
                if(httpsUrlCon != null){
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
     *
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public static String doPost(String url, Map<String, String> map, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            // 设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
                        charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * post请求
     * @param url 请求url
     * @param params 请求参数
     * @param contentType 内容类型
     * @return
     */
    public static String doPost(String url, Map<String, Object> params, ContentType contentType) {
        //创建请求参数体
        List<NameValuePair> requestParams = null;
        if(null != params){
            requestParams = new ArrayList<NameValuePair>();
            for(Map.Entry<String, Object> entry : params.entrySet()) {
                requestParams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        return doPost(url, requestParams, null, contentType, "UTF-8");
    }

    /**
     * post请求
     * @param url 请求url
     * @param params 请求参数
     * @param files 文件
     * @param contentType 内容类型
     * @param charset 字符集
     * @return
     */
    public static String doPost(String url, List<NameValuePair> params, final Map<String, File> files, ContentType contentType, String charset) {
        //响应信息
        String resStr = null;
        try{
            HttpClient httpClient = HttpClients.createDefault();

            //创建请求参数体
            HttpEntity rquestEntity = makeMultipartEntity(params, files, contentType, charset);

            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            httpPost.addHeader(rquestEntity.getContentType());
            httpPost.setEntity(rquestEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity =  httpResponse.getEntity();
            resStr = EntityUtils.toString(responseEntity);
        }catch(Exception e){
            log.error("调用post请求出现异常", e);
        }
        return resStr;
    }

    /**
     * 创建post请求参数体
     * @param params 参数
     * @param files 文件列表
     * @param contentType
     * @param charset 字符集
     * @return
     */
    private static HttpEntity makeMultipartEntity(List<NameValuePair> params, final Map<String, File> files, ContentType contentType, String charset ) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); //如果有SocketTimeoutException等情况，可修改这个枚举
        builder.setContentType(contentType);//内容类型
        //builder.setCharset(Charset.forName("UTF-8"));
        //不要用这个，会导致服务端接收不到参数
        if (params != null && params.size() > 0) {
            for (NameValuePair p : params) {
                builder.addTextBody(p.getName(), p.getValue(), ContentType.TEXT_PLAIN.withCharset(charset));
            }
        }
        if (files != null && files.size() > 0) {
            Set<Entry<String, File>> entries = files.entrySet();
            for (Entry<String, File> entry : entries) {
                builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }
        }
        return builder.build();
    }

    public static void main(String[] args) throws Exception {
    	String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);

		//获取区域信息请求参数
//		TrvokAreaReq req = new TrvokAreaReq();
//		req.setAreaType("1");
//		req.setTimestamp(timestamp);

//		String data = doPost("http://cvpa.leagpoint.com/sipService/trvok/trvok/getTrvokArea", "org=20180208O21995540&data=" + reqData +
//		"&sign=" + authReqSign ,
//		"application/x-www-form-urlencoded;charset=UTF-8");


		//获取机场详情请求参数
//		TrvokAirportReq req = new TrvokAirportReq();
//		req.setAirportId("504");
//		req.setAreaType("1");
//		req.setTimestamp(timestamp);

		//获取空港券码请求参数
//		GetVoucherReq req = new GetVoucherReq();
//		req.setTimestamp(timestamp);
//		req.setExpire("20180327");
//		req.setProductNo("20180108V124");
//		req.setOrgOrderNo("13213213166");
//		req.setOrderDesc("空港测试");

		//空港禁用券码请求参数
		DisableVoucherReq req =  new DisableVoucherReq();
		req.setDisableDesc("禁用测试");
		req.setOrgOrderNo("13213213166");
		req.setTimestamp(timestamp);
		req.setVoucherCode("246716108");

		//获取车点点券码请求参数
		GetVoucherReq getVoucherReq = new GetVoucherReq();
//		getVoucherReq.setTimestamp(timestamp);
//		getVoucherReq.setExpire("20180109");
//		getVoucherReq.setProductNo("20180201CDD110");
//		getVoucherReq.setOrgOrderNo("132132131");
//		getVoucherReq.setMobileNo("13911111111");
//		getVoucherReq.setOrderDesc("车点点测试");



//		ContentJumpReq contentJumpReq = new ContentJumpReq();
//		contentJumpReq.setLoginFlag("123");
//		contentJumpReq.setLoginUrl("123");
//		contentJumpReq.setMobileNo("666");
//		contentJumpReq.setPayUrl("12312");
//		contentJumpReq.setProductNo("20180315SIP");
//		contentJumpReq.setTimestamp(timestamp);
//		contentJumpReq.setUserId("1");
//		contentJumpReq.setUserName("213");



		String reqStr = JSONObject.toJSONString(getVoucherReq);

		String pubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
		PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
		String reqData = RSAUtils.encrypt(reqStr, uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
		reqData = URLEncoder.encode(reqData);
		String merchantPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
		PrivateKey merchantPriKey = RSAUtils.getPrivateKeyFromString(merchantPriKeyStr);
		String authReqSign = RSAUtils.sign(reqStr, merchantPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
		authReqSign = URLEncoder.encode(authReqSign);


//		String data = doPost("http://10.193.17.86:46959/trvok/trvok/getTrvokArea", "org=20180208O21995540&data=" + reqData +
//				"&sign=" + authReqSign ,
//				"application/x-www-form-urlencoded;charset=UTF-8");
//
//		doPost("http://cvpa.leagpoint.com/sipService/trvok/trvok/getTrvokAirportInfo", "org=20180208O21995540&data=" + reqData +
//				"&sign=" + authReqSign ,
//				"application/x-www-form-urlencoded;charset=UTF-8");
//
//		doPost("http://cvpa.leagpoint.com/sipService/voucher/voucher/getVoucher", "org=20180208O21995540&data=" + reqData +
//				"&sign=" + authReqSign ,
//				"application/x-www-form-urlencoded;charset=UTF-8");
//		String  data = doPost("http://cvpa.leagpoint.com/sipService/voucher/voucher/getVoucher", "org=20180208O21995540&data=" + reqData +
//				"&sign=" + authReqSign ,
//				"application/x-www-form-urlencoded;charset=UTF-8");

		String data = doPost("http://localhost:46959/voucher/voucher/getVoucher", "org=20180208O21995540&data=" + reqData +
				"&sign=" + authReqSign ,
				"application/x-www-form-urlencoded;charset=UTF-8");

//		String data = doPost("http://localhost:8040/content/content/contentJump", "org=20180208O21995540&data=" + reqData +
//				"&sign=" + authReqSign ,
//				"application/x-www-form-urlencoded;charset=UTF-8");

		//String data = "Gb%2FDZbw8TY2BPpWTFVrVZyHOGYdeClprnES4xBEI4sAMgcA63w%2BRdzJr2RPKgtWHpAcX9QcHEQySrrB2eZiOrNgBtvMMWJ62scv2bbWF3zB%2FiIsWJwqURGNGSCk1tACD5PvcuicOEcMeGSTiyxXSJLmpuQO5DSYwIybRMt2685cLfyvBH0BjXichzjYi8qjYoirs76JIi3AUnVkaFAx86%2FifsoBS7I04OhFshMDbX0bj4z4miDlGeQRUZSou7yO6uqY5h7JM5O023yujfDynRFkIoMS3eoYUcQu6MYJB5PSx86GH1Tkp8TP8SDqYg9jrJDWwhW5U5NTKusdPD22PbQ%3D%3D";

        String [] a =	data.split("&sign=");
		data = a[0].substring(5, a[0].length());
		data = URLDecoder.decode(data,"utf-8");
		System.out.println(data);
		PrivateKey sipPriKey = null;// 平台私钥
		sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");
		data = RSAUtils.decrypt(data,sipPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
		log.info("decryt data:"+data);
	}
}

/** *//**
 * 重写三个方法
 * @author Administrator
 *
 */
class myX509TrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        System.out.println("cert: " + chain[0].toString() + ", authType: " + authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

/** *//**
 * 重写一个方法
 * @author Administrator
 *
 */
class myHostnameVerifier implements HostnameVerifier {

    public boolean verify(String hostname, SSLSession session) {
        System.out.println("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
        return true;
    }
}

