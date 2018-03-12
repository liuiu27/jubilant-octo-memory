package com.cupdata.content.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentJumpReq;
import com.cupdata.commons.vo.content.ContentLoginReq;
import com.cupdata.commons.vo.content.ContentTransaction;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.biz.ContentBiz;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月12日 下午15:42:31
*/
@Slf4j
public class SupContentController {
	
	
	@Autowired
	private ProductFeignClient productFeignClient;
	
	
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
									 @RequestParam(value = "tranNo", required = true) String tranNo,
			@RequestBody ContentLoginReq contentLoginReq,	HttpServletRequest request, HttpServletResponse response){
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
			if(null == contentTransaction) {
				//如果为空 则保存新记录到数据库
			}else {
				//数据库更新此条交易记录
			}
			//组装参数 跳转
			return null;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}
}
