package com.cupdata.content.controller;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
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
import com.cupdata.content.vo.request.SupVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	 * @param org
	 * @param contentJumpReq
	 * @param request
	 * @param response
	 * @return
	 */
//	@PostMapping(path="/contentLogin",produces = "application/json")
//	public String contentLogin(@RequestBody @Validated SupVO<ContentLoginReq> contentLoginReq){
//		log.info("contentLogin is begin contentLoginReq " + contentLoginReq.toString());
//		try {
//			//查询数据库中是否存在此流水号
//			ContentTransaction contentTransaction = contentBiz.queryContentTransactionByTranNo(contentLoginReq.getTranNo(),null);
//			//验证流水号
//			if(null == contentTransaction) {
//				log.error("query result is null");
//				throw new ErrorException(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode(),ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
//			}
//			//查询数据中是否存在此交易类型的流水号
//			contentTransaction = contentBiz.queryContentTransactionByTranNo(contentLoginReq.getTranNo(),ModelConstants.CONTENT_TYPE_NOT_LOGGED);
//			if(null == contentTransaction) {
//				//保持新的流水记录
//				contentBiz.insertContentTransaction(contentLoginReq.getTranNo(), 
//						contentLoginReq.getSup(), 
//						JSONObject.toJSONString(contentLoginReq), 
//						null);
//			}else {
//				//更新流水表
//				contentBiz.updateContentTransaction(contentTransaction, 
//						null, 
//						ModelConstants.CONTENT_TYPE_NOT_LOGGED, 
//						null, 
//						contentLoginReq.getSup(), 
//						JSONObject.toJSONString(contentLoginReq));
//			}
//			//组装参数 跳转
//			return null;
//		} catch (Exception e) {
//			log.error("error is " + e.getMessage());
//			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
//		}
//	}
//	
	@GetMapping(path="/contentJump")
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
	
	@PostMapping(path="/contentQueryOrder",produces = "application/json")
	public BaseResponse<ContentQueryOrderRes> contentQueryOrder(SupVO<ContentQueryOrderReq> contentQueryOrderReq){
		log.info("contentQueryOrder is begin params contentQueryOrderReq is" + contentQueryOrderReq.toString());
		BaseResponse<ContentQueryOrderRes> res = new BaseResponse<ContentQueryOrderRes>();	
		try {
			//调用order-service的服务查询数据
			res = OrderFeignClient.queryContentOrder(contentQueryOrderReq.getData());
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	/**
	 * 支付请求接口
	 * @param sup 供应商号
	 * @param tranNo 流水号
	 * @param payPageVO 请求参数
	 * @return
	 */
	@GetMapping("payRequest")
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
        req.put("notifyUrl","http://cvpa.leagpoint.com/sipService/content/content/payNotify");
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
	 * @param tranNo 流水号
	 * @param payPageVO 请求参数
	 * @return
	 */
	@ResponseBody
	@GetMapping("payCancel")
	public BaseResponse payCancel(@RequestParam(value = "sup") String sup,
							 @RequestBody @Validated CancelPayVO cancelPayVO ){
		RestTemplate restTemplate = new RestTemplate();
		String url ="http://cvpd.leagpoint.com/mall/sip/content/contentOrderRefund.action?provider=LBHTDnJkYy4=";


		Map<String,String> req = new HashMap();

		req.put("sipOrderNo",cancelPayVO.getSupOrderNo());
		req.put("refundDate",cancelPayVO.getRefundDate());
		req.put("refundAmt", cancelPayVO.getRefundAmt());
		req.put("refundInfo",cancelPayVO.getRefundInfo());
		req.put("timeStamp",cancelPayVO.getTimeStamp());

		//JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
		url = EncryptionAndEecryption.Encryption(req, url);

		BaseResponse baseResponse = restTemplate.postForObject(url, null, BaseResponse.class);

		if (baseResponse.getResponseCode().equals("2"))
		return  new BaseResponse();

		return  new BaseResponse(ResponseCodeMsg.REQUEST_TIMEOUT.getCode(),ResponseCodeMsg.REQUEST_TIMEOUT.getMsg());

	}



}
