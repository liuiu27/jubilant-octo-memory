package com.cupdata.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.utils.EncryptionAndEecryption;
import com.cupdata.content.vo.request.ContentJumpReqVo;
import com.cupdata.content.vo.request.SupContentJumReqVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;

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
	public String contentJump(@RequestParam(value = "org") String org,
			@Validated @RequestBody ContentJumpReqVo contentJumpReqVo){
		//Step1： 验证数据是否为空 是否合法
		log.info("contentJump is begin params org is" + org + "contentJumpReq is" + contentJumpReqVo.toString());
		
		// Step2：查询服务产品信息
		ProductInfoVo productInfRes = contentBiz.findByProductNo(contentJumpReqVo.getProductNo());
		 
		// Step3：验证机构与产品是否关联
		contentBiz.validatedProductNo(org, productInfRes.getProductType(), productInfRes.getProductNo());
		
		//根据产品获取供应商 主页URL
		String supUrl = productInfRes.getServiceApplicationPath();
//		String supUrl = "https://test.wantu.cn/v2/m/?channel=rongshu";
		
//		//Step5 :   判断流水号  如果为空创建 新的           如果不为空则修改
		contentBiz.queryAndinsertOrUpdateContentTransaction(contentJumpReqVo,productInfRes,ModelConstants.CONTENT_TYPE_NOT_LOGGED,null);
		
		//查询是否存在下一步操作  fallback地址
		ContentTransactionLogDTO  contentTransactionLogDTO = contentBiz.queryContentTransactionByTranNo(contentJumpReqVo.getSipTranNo(), ModelConstants.CONTENT_TYPE_TO_LOGGED);
		
		if(null != contentTransactionLogDTO) {
			JSONObject resJson = JSONObject.parseObject(contentTransactionLogDTO.getRequestInfo());
			supUrl = resJson.getString("callBackUrl");
		}
		// 组装参数 发送请求
	    SupContentJumReqVo supContentJumReqVo = new SupContentJumReqVo();
	    supContentJumReqVo.setLoginFlag(contentJumpReqVo.getLoginFlag());
	    supContentJumReqVo.setMobileNo(contentJumpReqVo.getMobileNo());
		String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
		supContentJumReqVo.setTimestamp(timestamp);
		supContentJumReqVo.setUserId(contentJumpReqVo.getUserId());
		supContentJumReqVo.setUserName(contentJumpReqVo.getUserName());
		String url = EncryptionAndEecryption.Encryption(contentJumpReqVo, supUrl);
		StringBuffer ret = new StringBuffer("redirect:" + url);
	    return ret.toString();
	}

}
