package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.OrgFeignClient;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.vo.ContentToLoginReq;
import com.cupdata.content.vo.request.ContentLoginReqVo;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private OrgFeignClient orgFeignClient;


	@Autowired
	private ContentBiz contentBiz;
	
	/**
	 * 内容引入登录接口   供应商请求机构
	 * @param contentLoginReq
	 * @return
	 */
	@GetMapping("contentLogin")
	public String contentLogin(@RequestParam(value = "sup") String sup, @Validated @RequestBody ContentLoginReqVo contentLoginReq) {
        log.info("contentLogin is begin contentLoginReq " + contentLoginReq.toString());

        //查询是否存在此流水号
        ContentTransactionLogDTO contentTransaction = contentBiz.queryContentTransactionByTranNo(contentLoginReq.getSipTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);

        //获取机构登录地址
        JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
        ContentToLoginReq contentToLoginReq = new ContentToLoginReq();
        contentToLoginReq.setProductNo(contentTransaction.getProductNo());
        contentToLoginReq.setSipTranNo(contentLoginReq.getSipTranNo());

        BaseResponse<OrgInfoVo> orgByNo = orgFeignClient.findOrgByNo(contentTransaction.getOrgNo());
        if (!orgByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //内部调用错误
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();

        String url = null;
        try {
            url = contentBiz.createRequseUrl(resJson.getString("loginUrl"), JSON.toJSONString(contentLoginReq),orgInfoVo.getOrgPubKey(),orgInfoVo.getSipPriKey());
        } catch (Exception e) {
            //封装参数失败
        }

        contentTransaction.setSupNo(sup);
        contentTransaction.setRequestInfo(JSON.toJSONString(contentLoginReq));
        contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_TO_LOGGED);

        contentBiz.insertAndUpdateContentTransaction(contentTransaction);

        StringBuffer ret = new StringBuffer("redirect:" + url);
        return ret.toString();
    }
	
//	@PostMapping(path="/contentQueryOrder")
//	public BaseResponse<ContentQueryOrderResVo> contentQueryOrder(SupVO<ContentQueryOrderReqVo> contentQueryOrderReq){
//		log.info("contentQueryOrder is begin params contentQueryOrderReq is" + contentQueryOrderReq.toString());
//		BaseResponse<ContentQueryOrderResVo> res = new BaseResponse<ContentQueryOrderResVo>();
//		//调用order-service的服务查询数据
//		res = OrderFeignClient.queryContentOrder(contentQueryOrderReq);
//		return res;
//	}

    ///**
    // * 支付请求接口
    // * @param sup 供应商号
    // * @param payPageVO 请求参数
    // * @return
    // */
    //@GetMapping("payRequest")
    //public String payRequest(@RequestParam(value = "sup") String sup,
    //                         @RequestBody @Validated PayPageVO payPageVO ){
    //    //Step1 验证本流水是否有效
    //    //查询数据库中是否存在此流水号
    //    ContentTransaction contentTransaction = contentBiz.queryContentTransactionByTranNo(payPageVO.getSipTranNo(),ModelConstants.CONTENT_TYPE_NOT_LOGGED);
    //    //验证流水号
    //    if(null == contentTransaction) {
    //        log.error("query result is null");
    //        throw new ErrorException(ResponseCodeMsg.NO_TRANNO_AINVALID.getCode(),ResponseCodeMsg.NO_TRANNO_AINVALID.getMsg());
    //    }
    //    //Step2 验证是否有对应交易订单。并对其进行检验。
    //    //Step3 创建交易订单，并保存参数
    //    //Step4 获取对应的支付接口。
    //    //Step5 拼接参数。
    //    //获取机构登录地址
    //
    //    Map<String,String> req = new HashMap();
    //
    //    req.put("timestamp",payPageVO.getTimestamp());
    //    req.put("orderAmt",payPageVO.getOrderAmt());
    //    req.put("sipOrderNo", payPageVO.getSupOrderNo());
    //    req.put("sipOrderTime",payPageVO.getSupOrderTime());
    //    req.put("orderTitle","1");
    //    req.put("orderInfo","1");
    //    req.put("timeOut","30");
    //    req.put("payBackUrl",payPageVO.getPayBackUrl());
    //    req.put("notifyUrl","http://10.193.17.86:46959/content/content/payNotify");
    //    req.put("productNum","1");
    //    req.put("orderShow","1");
    //    req.put("userId","110440");
    //
    //    JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
    //    String url = EncryptionAndEecryption.Encryption(req, resJson.getString("payUrl"));
    //
    //    StringBuffer ret = new StringBuffer("redirect:"+url);
    //    return ret.toString();
    //
    //}


    ///**
    // * 支付请求接口
    // * @param sup 供应商号
    // * @return
    // */
    //@ResponseBody
    //@GetMapping("payCancel")
    //public BaseResponse payCancel(@RequestParam(value = "sup") String sup,
    //                              @RequestBody @Validated CancelPayVO cancelPayVO ){
    //    RestTemplate restTemplate = new RestTemplate();
    //    String url ="http://192.168.100.212:9190/mall/sip/content/contentOrderRefund.action?provider=LBHTDnJkYy4=";
    //
    //    Map<String,String> req = new HashMap();
    //    req.put("sipOrderNo",cancelPayVO.getSupOrderNo());
    //    req.put("refundDate",cancelPayVO.getRefundDate());
    //    req.put("refundAmt", cancelPayVO.getRefundAmt());
    //    req.put("refundInfo",cancelPayVO.getRefundInfo());
    //    req.put("timeStamp",cancelPayVO.getTimeStamp());
    //
    //    //JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
    //    url = EncryptionAndEecryption.Encryption(req, url);
    //
    //    BaseResponse baseResponse = restTemplate.postForObject(url, null, BaseResponse.class);
    //
    //    if (baseResponse.getResponseCode().equals("2"))
    //        return  new BaseResponse();
    //
    //    return  new BaseResponse(ResponseCodeMsg.REQUEST_TIMEOUT.getCode(),ResponseCodeMsg.REQUEST_TIMEOUT.getMsg());
    //
    //}


}
