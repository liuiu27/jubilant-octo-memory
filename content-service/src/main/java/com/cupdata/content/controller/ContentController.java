package com.cupdata.content.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentJumpReq;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.content.feign.ProductFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月8日 下午6:24:22
*/
@Slf4j
public class ContentController {
	
	
	@Autowired
	private ProductFeignClient productFeignClient;
	
	/**
	 * 内容引入跳转接口
	 * @param org
	 * @param contentJumpReq
	 * @param request
	 * @param response
	 * @return
	 */
	public BaseResponse contentJump(@RequestParam(value = "org", required = true) String org,
			@RequestBody ContentJumpReq contentJumpReq,	HttpServletRequest request, HttpServletResponse response){
		//Step1： 验证数据是否为空 是否合法
		log.info("contentJump is begin params org is" + org + "contentJumpReq is" + contentJumpReq.toString());
		BaseResponse res = new BaseResponse();
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
		if(StringUtils.isBlank(contentJumpReq.getSipTranNo())){
			//生成新的流水号
			String sipTranNo = CommonUtils.serialNumber();
			//保存数据库
			
			
		}else {
			//不为空查询数据库
			
			//查到数据判断时间戳是否超时
					
						//合法更新数据
						
						//不合法抛出异常
			
			
			//查不到 抛出异常
		}	
		
		//组装参数  发送请求
		
		return null;
	}
}
