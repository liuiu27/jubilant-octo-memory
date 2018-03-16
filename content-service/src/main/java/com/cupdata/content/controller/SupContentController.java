package com.cupdata.content.controller;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentLoginReq;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;
import com.cupdata.commons.vo.content.ContentTransaction;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.vo.request.PayPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月12日 下午15:42:31
*/
@Slf4j
@Controller
public class SupContentController {
	
	
	@Autowired
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private OrderFeignClient OrderFeignClient;
	
	
	@Autowired
	private ContentBiz ContentBiz;
	
	/**
	 * 内容引入登录接口   供应商请求
	 * @param org
	 * @param contentJumpReq
	 * @param request
	 * @param response
	 * @return
	 */
	public BaseResponse contentLogin(@RequestParam(value = "sup", required = true) String sup,
//									 @RequestParam(value = "tranNo", required = true) String tranNo,
			@RequestBody ContentLoginReq contentLoginReq,	HttpServletRequest request, HttpServletResponse response){
		String tranNo = "";
		//Step1： 验证数据是否为空 是否合法
		log.info("contentLogin is begin params sup is" + sup + "contentLoginReq is" + contentLoginReq.toString());
		BaseResponse res = new BaseResponse();	
		try {
			// 验证 参数是否合法
			if(StringUtils.isBlank(contentLoginReq.getCallBackUrl())||StringUtils.isBlank(tranNo)) {
				log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			//查询数据库中是否存在此流水号
			Map<String,Object> paramMap =  new HashMap<String,Object>();
			paramMap.put("TRAN_NO", tranNo);
			ContentTransaction contentTransaction =  ContentBiz.selectSingle(paramMap);
			if(null == contentTransaction) {
				log.error("query tranNo is Null tranNo is " + tranNo + "errorCode is" + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseCode(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode());
				res.setResponseMsg(ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
				return res;
			}
			//查询数据中是否存在此交易类型的流水号
			paramMap.clear();
			paramMap.put("TRAN_NO", tranNo);
			paramMap.put("TRAN_TYPE", ModelConstants.CONTENT_TYPE_NOT_LOGGED);
			contentTransaction =  ContentBiz.selectSingle(paramMap);
			ContentTransaction ct = new ContentTransaction(); 
			if(null == contentTransaction) {
				//如果为空 则保存新记录到数据库
				ct.setTranType(ModelConstants.CONTENT_TYPE_NOT_LOGGED);
				ct.setTranNo(tranNo);
				ct.setRequestInfo(JSONObject.toJSONString(contentLoginReq));
				ContentBiz.insert(ct);
			}else {
				
				// 查到数据判断时间戳是否超时
				Date timestamp = DateTimeUtil.getDateByString(ct.getTimestamp().substring(0, 17),
						"yyyyMMddHHmmssSSS");
				// 时间戳超时
				if (!DateTimeUtil.compareTime(DateTimeUtil.getCurrentTime(), timestamp, -60 * 1000L, 3000 * 1000L)) {
					// 超时 抛出异常
					res.setResponseCode(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getCode());
					res.setResponseMsg(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getMsg());
					return res;
				}
				//数据库更新此条交易记录
				contentTransaction.setRequestInfo(JSONObject.toJSONString(contentLoginReq));
				ContentBiz.update(contentTransaction);
			}
			//组装参数 跳转
			return null;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}
	
	public BaseResponse<ContentQueryOrderRes> contentQueryOrder(@RequestParam(value = "sup", required = true) String sup,
			// @RequestParam(value = "tranNo", required = true) String tranNo,
			 @RequestBody ContentQueryOrderReq contentQueryOrderReq,	HttpServletRequest request, HttpServletResponse response){
		String tranNo = "";
		log.info("contentQueryOrder is begin params sup is" + sup + "contentQueryOrderReq is" + contentQueryOrderReq.toString());
		BaseResponse<ContentQueryOrderRes> res = new BaseResponse<ContentQueryOrderRes>();	
		try {
			//验证参数是否合法
			if(StringUtils.isBlank(contentQueryOrderReq.getSupOrderNo())
					||StringUtils.isBlank(tranNo)
				    ||StringUtils.isBlank(sup)) {
				log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			//调用order-service的服务查询数据
			res = OrderFeignClient.queryContentOrder(contentQueryOrderReq);
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
	public String payRequest(@RequestParam(value = "sup", required = true) String sup,
								   @RequestParam(value = "tranNo", required = true) String tranNo,
								   @RequestBody @Validated PayPageVO payPageVO ){
	    //Step1 验证本流水是否有效
	    //Step2 验证是否有对应交易订单。并对其进行检验。
	    //Step3 创建交易订单，并保存参数
	    //Step4 获取对应的支付接口。
	    //Step5 拼接参数。

        StringBuffer ret = new StringBuffer("redirect:");

        return ret.toString();

	}


}
