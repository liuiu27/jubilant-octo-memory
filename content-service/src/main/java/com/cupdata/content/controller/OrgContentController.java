package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.exception.ContentException;
import com.cupdata.content.feign.SupplierFeignClient;
import com.cupdata.content.vo.request.ContentJumpReqVo;
import com.cupdata.content.vo.request.ContentLoginReqVo;
import com.cupdata.content.vo.request.SupContentJumReqVo;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

	@Autowired
    private SupplierFeignClient supplierFeignClient;

	/**
	 * 内容引入跳转接口   机构请求
	 * @param org
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


//		//Step5 :   判断流水号  如果为空创建 新的   如果不为空则修改
		contentBiz.queryAndinsertOrUpdateContentTransaction(contentJumpReqVo,productInfRes,ModelConstants.CONTENT_TYPE_NOT_LOGGED,null);

		//查询是否存在下一步操作  fallback地址
		ContentTransactionLogDTO  contentTransactionLogDTO = contentBiz.queryContentTransactionByTranNo(contentJumpReqVo.getSipTranNo(), ModelConstants.CONTENT_TYPE_TO_LOGGED);

		if(null != contentTransactionLogDTO) {
            ContentLoginReqVo contentLoginReq =  JSONObject.parseObject(contentTransactionLogDTO.getRequestInfo(),ContentLoginReqVo.class);
			supUrl = contentLoginReq.getCallBackUrl();
		}
		// 组装参数 发送请求
	    SupContentJumReqVo supContentJumReqVo = new SupContentJumReqVo();
	    supContentJumReqVo.setLoginFlag(contentJumpReqVo.getLoginFlag());
	    supContentJumReqVo.setMobileNo(contentJumpReqVo.getMobileNo());
		String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
		supContentJumReqVo.setTimestamp(timestamp);
		supContentJumReqVo.setUserId(contentJumpReqVo.getUserId());
		supContentJumReqVo.setUserName(contentJumpReqVo.getUserName());

        //封装重定向到机构的地址
        BaseResponse<SupplierInfVo> supByNo = supplierFeignClient.findSupByNo(productInfRes.getSupplierNo());
        if (!supByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //内部调用错误
			throw new ContentException();
        }
        //组装重定向地址及参数
        SupplierInfVo supplierInfVo = supByNo.getData();

        String url = null;
        try {
            url = contentBiz.createRequseUrl(supUrl, JSON.toJSONString(supContentJumReqVo),supplierInfVo.getSupplierPubKey(),supplierInfVo.getSipPriKey());
        } catch (Exception e) {
            //封装参数失败
        }

		StringBuffer ret = new StringBuffer("redirect:" + url);
	    return ret.toString();
	}


	@ResponseBody
	@PostMapping("payNotify")
	public BaseResponse payNotify(@RequestParam(value = "org") String org,@RequestBody NotifyVO notifyVO) {
		RestTemplate restTemplate = new RestTemplate();

		String notifyurl = "https://test.wantu.cn/payGate/payNotify";
		Map<String, String> param = new HashMap<>();

		param.put("resultCode", notifyVO.getResultCode());
		param.put("supOrderNo", notifyVO.getSipOrderNo());//TODO 2018/4/17
		param.put("orderAmt", notifyVO.getOrderAmt());
		param.put("sipOrderNo", notifyVO.getOrgOrderNo());

		//TODO 2018/4/19
		//封装重定向到机构的地址
		BaseResponse<SupplierInfVo> supByNo = supplierFeignClient.findSupByNo("");
		if (!supByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
			//内部调用错误
		}
		//组装重定向地址及参数
		SupplierInfVo supplierInfVo = supByNo.getData();

		String url = null;
		try {
			url = contentBiz.createRequseUrl(notifyurl, JSON.toJSONString(param),supplierInfVo.getSupplierPubKey(),supplierInfVo.getSipPriKey());
		} catch (Exception e) {
			//封装参数失败
		}

        String baseResponse = restTemplate.postForObject(notifyurl, null, String.class);

        return new BaseResponse(baseResponse);
	}

	@Data
 	static class NotifyVO {

		String resultCode;

		String sipOrderNo;

		String orderAmt;

		String orgOrderNo;

		String payTime;

	}

}
