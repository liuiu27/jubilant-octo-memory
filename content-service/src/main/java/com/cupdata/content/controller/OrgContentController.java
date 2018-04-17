package com.cupdata.content.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentJumpReq;
import com.cupdata.commons.vo.content.ContentTransaction;
import com.cupdata.commons.vo.content.SupContentJumReq;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.utils.EncryptionAndEecryption;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月8日 下午6:24:22
*/
@Slf4j
@Controller
@RequestMapping("/content")
public class OrgContentController{


	@Autowired
	private ProductFeignClient productFeignClient;


	@Autowired
	private ContentBiz contentBiz;

	/**
	 * 内容引入跳转接口   机构请求
	 * @param org
	 * @param contentJumpReq
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(path="/contentJump")
	public String contentJump(@RequestParam(value = "org", required = true) String org,
			@RequestBody ContentJumpReq contentJumpReq,	HttpServletRequest request, HttpServletResponse response){
		//Step1： 验证数据是否为空 是否合法
		log.info("contentJump is begin params org is" + org + "contentJumpReq is" + contentJumpReq.toString());
		BaseResponse res = new BaseResponse();
		try {
			if(StringUtils.isBlank(contentJumpReq.getProductNo())
//					||StringUtils.isBlank(contentJumpReq.getMobileNo())
					||StringUtils.isBlank(contentJumpReq.getLoginFlag())
//					||StringUtils.isBlank(contentJumpReq.getUserId())
//					||StringUtils.isBlank(contentJumpReq.getUserName())
					||StringUtils.isBlank(contentJumpReq.getLoginUrl())
					||StringUtils.isBlank(contentJumpReq.getPayUrl())) {
				log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
			}
//
			// Step2：查询服务产品信息
			BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(contentJumpReq.getProductNo());
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
					|| null == productInfRes.getData()) {// 如果查询失败
				log.error("procduct-service  findByProductNo result is null......  productNo is" + contentJumpReq.getProductNo()
						+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
			}
//
//			// Step3：判断服务产品是否为内容 引入商品
//			if (!ModelConstants.PRODUCT_TYPE_CONTENT.equals(productInfRes.getData().getProduct().getProductType())) {
//				log.error("Not a content product.....poductType is" + productInfRes.getData().getProduct().getProductType()
//						+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
//				res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
//				res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
//			}
//
//			// Step4：查询服务产品与机构是否关联
//			BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, contentJumpReq.getProductNo());
//			if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
//					|| null == orgProductRelRes.getData()) {
//				log.error("procduct-service findRel result is null...org is" + org + "productNo is "
//						+ contentJumpReq.getProductNo() + " errorCode is "
//						+ ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
//				res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
//				res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
//			}
			String supUrl = "https://test.wantu.cn/v2/m/?channel=rongshu";
//			//Step5 :   判断流水号  如果为空创建 新的
			String sipTranNo = contentJumpReq.getSipTranNo();
			if(StringUtils.isBlank(sipTranNo)){
				//生成新的流水号
				sipTranNo = CommonUtils.serialNumber();
				//保存数据库
				ContentTransaction contentTransaction =  new  ContentTransaction();
				contentTransaction.setSipTranNo(sipTranNo);
				contentTransaction.setProductNo(contentJumpReq.getProductNo());
				contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_NOT_LOGGED);
				contentTransaction.setOrgNo(org);
				contentTransaction.setSupNo(productInfRes.getData().getProduct().getSupplierNo());
				String requestInfo = JSONObject.toJSONString(contentJumpReq);
				contentTransaction.setRequestInfo(requestInfo);
				contentBiz.insert(contentTransaction);
			}else {

				//不为空查询数据库
				Map<String, Object> paramMap = new HashMap<String,Object>();
				paramMap.put("tranNo", sipTranNo);
				paramMap.put("tranType", ModelConstants.CONTENT_TYPE_NOT_LOGGED);
				ContentTransaction contentTransaction = contentBiz.selectSingle(paramMap);
				if (null != contentTransaction) {
					// 查到数据判断时间戳是否超时
					Date timestamp = DateTimeUtil.getDateByString(contentJumpReq.getTimestamp().substring(0, 17),
							"yyyyMMddHHmmssSSS");
					// 时间戳超时
					if (DateTimeUtil.compareTime(DateTimeUtil.getCurrentTime(), timestamp, -60 * 1000L, 3000 * 1000L)) {
						// 合法更新数据
						contentTransaction.setSipTranNo(sipTranNo);
						contentTransaction.setProductNo(contentJumpReq.getProductNo());
						contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_NOT_LOGGED);
						contentTransaction.setOrgNo(org);
						contentTransaction.setSupNo(productInfRes.getData().getProduct().getSupplierNo());
						String requestInfo = JSONObject.toJSONString(contentJumpReq);
						contentTransaction.setRequestInfo(requestInfo);
						contentBiz.update(contentTransaction);
						//查询是否存在callback地址
						paramMap.clear();
						paramMap.put("tranNo", sipTranNo);
						paramMap.put("tranType", ModelConstants.CONTENT_TYPE_TO_LOGGED);
						contentTransaction = contentBiz.selectSingle(paramMap);
						if(null != contentTransaction) {
							JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
							supUrl = resJson.getString("callBackUrl");
						}
					} else {
						// 超时 抛出异常
						res.setResponseCode(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getCode());
						res.setResponseMsg(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getMsg());
					}
				} else {
					// 查不到 抛出异常
					res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
					res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
				}
			}
			// 组装参数 发送请求
			SupContentJumReq jumReq = new SupContentJumReq();
			jumReq.setLoginFlag(contentJumpReq.getLoginFlag());

			//获取SIP的跳转URL
//			jumReq.setLoginUrl(contentJumpReq.getLoginUrl() + "?tranNo=" + tranNo);
//			jumReq.setLoginUrl("http://cvpa.leagpoint.com/sipService/content/SupContent/contentLogin" + "?tranNo=" + sipTranNo);
//			jumReq.setPayUrl(contentJumpReq.getPayUrl() + "?tranNo=" + sipTranNo);

			jumReq.setMobileNo(contentJumpReq.getMobileNo());
			String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
			jumReq.setTimestamp(timestamp);
			jumReq.setUserId(contentJumpReq.getUserId());
			jumReq.setUserName(contentJumpReq.getUserName());
			String url = EncryptionAndEecryption.Encryption(jumReq, supUrl);
			StringBuffer ret = new StringBuffer("redirect:" + url);
		    return ret.toString();
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}










}
