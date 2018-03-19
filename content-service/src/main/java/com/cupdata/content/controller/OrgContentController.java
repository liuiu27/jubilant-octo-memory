package com.cupdata.content.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.vo.request.OrgVO;

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
	public BaseResponse contentJump1(@RequestParam(value = "org", required = true) String org,
//			@RequestParam(value = "tranNo", required = true) String tranNo,
			@RequestBody ContentJumpReq contentJumpReq,	HttpServletRequest request, HttpServletResponse response){
		String tranNo = "";
		//Step1： 验证数据是否为空 是否合法
		log.info("contentJump is begin params org is" + org + "contentJumpReq is" + contentJumpReq.toString());
		BaseResponse res = new BaseResponse();
		try {
			if(StringUtils.isBlank(contentJumpReq.getProductNo())
					||StringUtils.isBlank(contentJumpReq.getMobileNo())
					||StringUtils.isBlank(contentJumpReq.getLoginFlag())
					||StringUtils.isBlank(contentJumpReq.getUserId())
					||StringUtils.isBlank(contentJumpReq.getUserName())
					||StringUtils.isBlank(contentJumpReq.getLoginUrl())
					||StringUtils.isBlank(contentJumpReq.getPayUrl())) {
				log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
				return res;
			}
			
			// Step2：查询服务产品信息
			BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(contentJumpReq.getProductNo());
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
					|| null == productInfRes.getData()) {// 如果查询失败
				log.error("procduct-service  findByProductNo result is null......  productNo is" + contentJumpReq.getProductNo()
						+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
				return res;
			}
			
			// Step3：判断服务产品是否为内容 引入商品
			if (!ModelConstants.PRODUCT_TYPE_CONTENT.equals(productInfRes.getData().getProduct().getProductType())) {
				log.error("Not a content product.....poductType is" + productInfRes.getData().getProduct().getProductType()
						+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
				res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
				return res;
			}
	
			// Step4：查询服务产品与机构是否关联
			BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, contentJumpReq.getProductNo());
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
					|| null == orgProductRelRes.getData()) {
				log.error("procduct-service findRel result is null...org is" + org + "productNo is "
						+ contentJumpReq.getProductNo() + " errorCode is "
						+ ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
				res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
				res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
				return res;
			}
			
			//Step5 :   判断流水号  如果为空创建 新的
			if(StringUtils.isBlank(tranNo)){
				//生成新的流水号
				String sipTranNo = CommonUtils.serialNumber();
				//保存数据库
				ContentTransaction contentTransaction =  new  ContentTransaction();
				contentTransaction.setTranNo(sipTranNo);
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
				paramMap.put("TRAN_NO", tranNo);
				paramMap.put("TRAN_TYPE", ModelConstants.CONTENT_TYPE_NOT_LOGGED);
				ContentTransaction contentTransaction = contentBiz.selectSingle(paramMap);
				if (null != contentTransaction) {
					// 查到数据判断时间戳是否超时
					Date timestamp = DateTimeUtil.getDateByString(contentJumpReq.getTimestamp().substring(0, 17),
							"yyyyMMddHHmmssSSS");
					// 时间戳超时
					if (!DateTimeUtil.compareTime(DateTimeUtil.getCurrentTime(), timestamp, -60 * 1000L, 3000 * 1000L)) {
						// 合法更新数据
						contentTransaction = new ContentTransaction();
						contentTransaction.setTranNo(tranNo);
						contentTransaction.setProductNo(contentJumpReq.getProductNo());
						contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_NOT_LOGGED);
						contentTransaction.setOrgNo(org);
						contentTransaction.setSupNo(productInfRes.getData().getProduct().getSupplierNo());
						String requestInfo = JSONObject.toJSONString(contentJumpReq);
						contentTransaction.setRequestInfo(requestInfo);
						contentBiz.update(contentTransaction);
					} else {
						// 超时 抛出异常
						res.setResponseCode(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getCode());
						res.setResponseMsg(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getMsg());
						return res;
					}
				} else {
					// 查不到 抛出异常
					res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
					res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
					return res;
				}
			}
			// 组装参数 发送请求 
			response.sendRedirect("www.baidu.com");
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}
	
	/***
	 * 新   内容引入跳转接口   机构请求 
	 * @param contentJumpReq
	 * @return
	 */
	@PostMapping(path="/contentJump",produces = "application/json")
	public String contentJump(@RequestBody @Validated OrgVO<ContentJumpReq> contentJumpReq){
		log.info("contentJump is begin params  is" + contentJumpReq.toString());
		try {
			// Step1：查询服务产品信息
			ProductInfVo productInfRes = contentBiz.findByProductNo(contentJumpReq.getData().getProductNo());
			// Step2：判断服务产品是否为内容 引入商品 是否与机构相关连
			contentBiz.validatedProductNo(contentJumpReq.getOrg(), productInfRes.getProduct().getProductType(), productInfRes.getProduct().getProductType());
			//Step3 :   判断流水号  
			if(StringUtils.isBlank(contentJumpReq.getTranNo())){
				//生成新的流水号
				String tranNo = CommonUtils.serialNumber();
				//如果为空创建 新的 并保持数据库
				contentBiz.insertContentTransaction(tranNo,
						contentJumpReq.getOrg(),
						JSONObject.toJSONString(contentJumpReq),
						productInfRes);
			}else {	
				//不为空查询流水表
				ContentTransaction contentTransaction = contentBiz.queryContentTransactionByTranNo(contentJumpReq.getTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);
				//验证流水号
				if(null == contentTransaction) {
					log.error("query result is null");
					throw new ErrorException(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode(),ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
				}
				//验证时间戳
				contentBiz.validatedtimestamp(contentJumpReq.getData().getTimestamp());
				// 合法更新数据
				contentBiz.updateContentTransaction(contentTransaction, 
						productInfRes.getProduct().getProductNo(), 
						ModelConstants.CONTENT_TYPE_NOT_LOGGED, 
						contentJumpReq.getOrg(), 
						null, 
						JSONObject.toJSONString(contentJumpReq));
			} 
			//TODO 组装参数 发送请求
			return null;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}
	
	
	
	
	
	
	
	
	
	
}
