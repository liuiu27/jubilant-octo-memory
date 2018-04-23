package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;
import com.cupdata.commons.vo.content.ContentTransaction;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.utils.EncryptionAndEecryption;
import com.cupdata.content.vo.ContentLoginReq;
import com.cupdata.content.vo.ContentToLoginReq;
import com.cupdata.content.vo.request.CancelPayVO;
import com.cupdata.content.vo.request.PayPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月12日 下午15:42:31
*/
@Slf4j
@Controller
@RequestMapping("/supContent")
public class SupContentController {
	
	
	@Autowired
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private OrderFeignClient OrderFeignClient;
	
	@Autowired
	private ContentBiz contentBiz;
	
	/**
	 * 内容引入登录接口   供应商请求
	 * @param request
	 * @param response
	 * @return
	 */

	@PostMapping(path="/contentJump")
	public String contentJump(@RequestParam(value = "sup", required = true) String sup,
			@RequestBody ContentLoginReq contentLoginReq,	HttpServletRequest request, HttpServletResponse response){
		log.info("contentLogin is begin contentLoginReq " + contentLoginReq.toString());
		
		if(StringUtils.isBlank(contentLoginReq.getSipTranNo())) {
			log.error("prams tranNo  is  null");
			throw new ErrorException(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode(),ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
		}
		
		try {
			//查询数据库中是否存在此流水号
			ContentTransaction contentTransaction = contentBiz.queryContentTransactionByTranNo(contentLoginReq.getSipTranNo(),ModelConstants.CONTENT_TYPE_NOT_LOGGED);
			//验证流水号
			if(null == contentTransaction) {
				log.error("query result is null");
				throw new ErrorException(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode(),ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
			}
			//获取机构登录地址
			JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
			ContentToLoginReq contentToLoginReq = new ContentToLoginReq();
			contentToLoginReq.setProductNo(contentTransaction.getProductNo());
			contentToLoginReq.setSipTranNo(contentLoginReq.getSipTranNo());
			
			
			String url = EncryptionAndEecryption.Encryption(contentToLoginReq, resJson.getString("loginUrl"));
			
			//查询数据中是否存在此交易类型的流水号
			contentTransaction = contentBiz.queryContentTransactionByTranNo(contentLoginReq.getSipTranNo(),ModelConstants.CONTENT_TYPE_TO_LOGGED);
			if(null == contentTransaction) {
				//保持新的流水记录
				contentBiz.insertContentTransaction(
						contentLoginReq.getSipTranNo(), 
						null,
						sup, 
						JSONObject.toJSONString(contentLoginReq), 
						null, 
						ModelConstants.CONTENT_TYPE_TO_LOGGED);
			}else {
				//更新流水表
				contentBiz.updateContentTransaction(contentTransaction, 
						contentTransaction.getProductNo(), 
						ModelConstants.CONTENT_TYPE_TO_LOGGED, 
						null, 
						sup, 
						JSONObject.toJSONString(contentLoginReq));
			}
			StringBuffer ret = new StringBuffer("redirect:" + url);
		    return ret.toString();
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	@ResponseBody
	@PostMapping(path="queryOrder")
	public BaseResponse contentQueryOrder(@RequestParam(value = "sup") String sup,@RequestBody ContentQueryOrderReq contentQueryOrderReq){
		log.info("contentQueryOrder is begin params contentQueryOrderReq is" + contentQueryOrderReq.toString());
		BaseResponse<ContentQueryOrderRes> res = new BaseResponse<ContentQueryOrderRes>();
		//contentQueryOrderReq.getTranType();
		//1 验证
		// 2 根据订单号查询 订单信息  获取机构号
		//3 查询机构表 获取机构 查询订单接口地址
		RestTemplate restTemplate = new RestTemplate();
		String url ="http://cvpc.leagpoint.com/mall/sip/content/queryContentOrderStatus.action?provider=LBHTDnJkYy4=";

		Map<String,String> req = new HashMap();
		String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);

		req.put("sipOrderNo",contentQueryOrderReq.getSupOrderNo());
		req.put("timestamp",timestamp);

		req.put("tranType",contentQueryOrderReq.getTranType());

		url = EncryptionAndEecryption.Encryption(req, url);

		log.info("url:"+url);
		try {
			String data = restTemplate.getForObject(url,String.class);
			JSONObject jsonObject1 = JSON.parseObject(data);
			data = jsonObject1.getString("data");
			data = URLDecoder.decode(data,"utf-8");
			System.out.println(data);
			PrivateKey sipPriKey = null;// 平台私钥
			sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");
			data = RSAUtils.decrypt(data,sipPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
			log.info("decryt data:"+data);

			JSONObject jsonObject = JSONObject.parseObject(data);

			if (!jsonObject.getString("resultCode").equals("2")){
				return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),jsonObject.getString("resultMsg").toString());
			}
			jsonObject.remove("resultCode");
			log.info("URL = "+url);
			log.info(data);
			return  new BaseResponse(jsonObject);

		} catch (Exception e) {

			log.info("调用失败……");
			return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());
		}

		//4组装 参数 发送请求给机构查询机构订单信息
		//5返回

	}

	/**
	 * 支付请求接口
	 * @param sup 供应商号
	 * @param payPageVO 请求参数
	 * @return
	 */
	@PostMapping("payRequest")
	public String payRequest(@RequestParam(value = "sup") String sup,
								   @RequestBody @Validated PayPageVO payPageVO ){
	    //Step1 验证本流水是否有效
        //查询数据库中是否存在此流水号
        ContentTransaction contentTransaction = contentBiz.queryContentTransactionByTranNo(payPageVO.getSipTranNo(),ModelConstants.CONTENT_TYPE_NOT_LOGGED);
        //验证流水号
        if(null == contentTransaction) {
            log.error("query result is null");
            throw new ErrorException(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode(),ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
        }
	    //Step2 验证是否有对应交易订单。并对其进行检验。
	    //Step3 创建交易订单，并保存参数
	    //Step4 获取对应的支付接口。
	    //Step5 拼接参数。
        //获取机构登录地址

        Map<String,String> req = new HashMap();

        req.put("timestamp",payPageVO.getTimestamp());
        req.put("orderAmt",payPageVO.getOrderAmt());
        req.put("sipOrderNo", payPageVO.getSupOrderNo());
        req.put("sipOrderTime",payPageVO.getSupOrderTime());
        req.put("orderTitle","1");
        req.put("orderInfo","1");
        req.put("timeOut","30");
        req.put("payBackUrl",payPageVO.getPayBackUrl());
        req.put("notifyUrl","http://10.193.17.86:46959/content/content/payNotify");
        req.put("productNum","1");
        req.put("orderShow","1");
        req.put("userId","110440");

        JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
        String url = EncryptionAndEecryption.Encryption(req, resJson.getString("payUrl"));

        StringBuffer ret = new StringBuffer("redirect:"+url);
        return ret.toString();

	}


	/**
	 * 支付请求接口
	 * @param sup 供应商号
	 * @return
	 */
	@ResponseBody
	@PostMapping("payRefund")
	public BaseResponse payRefund(@RequestParam(value = "sup") String sup,
							 @RequestBody @Validated CancelPayVO cancelPayVO ){
		RestTemplate restTemplate = new RestTemplate();
		String url ="http://cvpc.leagpoint.com/mall/sip/content/contentOrderRefund.action?provider=LBHTDnJkYy4=";

		Map<String,String> req = new HashMap();
		req.put("sipOrderNo",cancelPayVO.getSupOrderNo());
		req.put("refundDate",cancelPayVO.getRefundDate());
		req.put("refundAmt", cancelPayVO.getRefundAmt());
		req.put("refundInfo",cancelPayVO.getRefundInfo());
		String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
		req.put("timestamp",timestamp);

		//JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
		url = EncryptionAndEecryption.Encryption(req, url);

		try {

			String data = restTemplate.getForObject(url,String.class);
			JSONObject jsonObject1 = JSON.parseObject(data);
			data = jsonObject1.getString("data");
			data = URLDecoder.decode(data,"utf-8");
			System.out.println(data);
			PrivateKey sipPriKey = null;// 平台私钥
			sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");
			data = RSAUtils.decrypt(data,sipPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
			log.info("decryt data:"+data);

			JSONObject jsonObject = JSONObject.parseObject(data);

			if (!jsonObject.getString("resultCode").equals("2")){
				return new BaseResponse<>(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),jsonObject.getString("resultMsg").toString());
			}

			log.info("URL = "+url);
			log.info(data);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			log.info("调用失败……");
		}
		return  new BaseResponse();

	}



}
