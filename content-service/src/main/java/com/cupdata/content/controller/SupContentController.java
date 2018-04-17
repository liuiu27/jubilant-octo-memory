package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.utils.EncryptionAndEecryption;
import com.cupdata.content.vo.ContentToLoginReq;
import com.cupdata.content.vo.request.ContentLoginReq;
import com.cupdata.content.vo.request.PayPageVO;
import com.cupdata.content.vo.request.SupVO;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

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
	 * @param contentLoginReq q
	 * @return
	 */
	@GetMapping(path="/contentJump")
	public String contentJump(@RequestParam(value = "sup") String sup, @Validated @RequestBody ContentLoginReq contentLoginReq) {
		log.info("contentLogin is begin contentLoginReq " + contentLoginReq.toString());

		//查询是否存在此流水号
		ContentTransactionLogDTO contentTransaction = contentBiz.queryContentTransactionByTranNo(contentLoginReq.getSipTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);

		//获取机构登录地址
		JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
		ContentToLoginReq contentToLoginReq = new ContentToLoginReq();
		contentToLoginReq.setProductNo(contentTransaction.getProductNo());
		contentToLoginReq.setSipTranNo(contentLoginReq.getSipTranNo());


		//TODO 2018/4/17 封装供应商参数
		String url = EncryptionAndEecryption.Encryption(contentToLoginReq, resJson.getString("loginUrl"));

		contentTransaction.setSupNo(sup);
		contentTransaction.setRequestInfo(JSON.toJSONString(contentLoginReq));
		contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_TO_LOGGED);

		contentBiz.insertAndUpdateContentTransaction(contentTransaction);

		StringBuffer ret = new StringBuffer("redirect:" + url);
		return ret.toString();
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
	 * @param payPageVO 请求参数
	 * @return
	 */
	public String payRequest(@RequestParam(value = "sup", required = true) String sup,
								   @RequestBody @Validated PayPageVO payPageVO ){
	    //Step1 验证本流水是否有效
        payPageVO.getSipTranNo();

	    //Step2 验证是否有对应交易订单。并对其进行检验。
	    //Step3 创建交易订单，并保存参数


	    //Step4 获取对应的支付接口。
	    //Step5 拼接参数。

        StringBuffer ret = new StringBuffer("redirect:");

        return ret.toString();

	}


}
