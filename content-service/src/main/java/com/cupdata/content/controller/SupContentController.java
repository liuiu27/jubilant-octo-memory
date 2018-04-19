package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.exception.ContentException;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.OrgFeignClient;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.content.vo.ContentToLoginReq;
import com.cupdata.content.vo.request.ContentJumpReqVo;
import com.cupdata.content.vo.request.ContentLoginReqVo;
import com.cupdata.content.vo.request.PayPageVO;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private OrgFeignClient orgFeignClient;


	@Autowired
	private ContentBiz contentBiz;
	
	/**
	 * 内容引入登录接口   供应商请求机构
	 * @param contentLoginReq
	 * @return
	 */
	@PostMapping("contentLogin")
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
            log.info("查询供应商信息失败，info："+ contentTransaction.getOrgNo());
            throw new ContentException();
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();
        String url = null;
        try {
            url = contentBiz.createRequseUrl(resJson.getString("loginUrl"), JSON.toJSONString(contentLoginReq),orgInfoVo.getOrgPubKey(),orgInfoVo.getSipPriKey());
        } catch (Exception e) {
            log.info("封装重定向地址错误");
            throw new ContentException();
        }

        contentTransaction.setSupNo(sup);
        contentTransaction.setRequestInfo(JSON.toJSONString(contentLoginReq));
        contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_TO_LOGGED);

        contentBiz.insertAndUpdateContentTransaction(contentTransaction);

        return "redirect:" + url;
    }
	
//	@PostMapping(path="/contentQueryOrder")
//	public BaseResponse<ContentQueryOrderResVo> contentQueryOrder(SupVO<ContentQueryOrderReqVo> contentQueryOrderReq){
//		log.info("contentQueryOrder is begin params contentQueryOrderReq is" + contentQueryOrderReq.toString());
//		BaseResponse<ContentQueryOrderResVo> res = new BaseResponse<ContentQueryOrderResVo>();
//		//调用order-service的服务查询数据
//		res = OrderFeignClient.queryContentOrder(contentQueryOrderReq);
//		return res;
//	}

    /**
     * 支付请求接口
     * @param sup 供应商号
     * @param payPageVO 请求参数
     * @return
     */
    @PostMapping("payRequest")
    public String payRequest(@RequestParam(value = "sup") String sup, @RequestBody @Validated PayPageVO payPageVO ){
        //Step1 验证本流水是否有效
        //查询数据库中是否存在此流水号。
        ContentTransactionLogDTO contentTransaction = contentBiz.queryContentTransactionByTranNo(payPageVO.getSipTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);

        //转行的出上次跳转信息
        ContentJumpReqVo contentJumpReqVo = JSONObject.parseObject(contentTransaction.getRequestInfo(),ContentJumpReqVo.class);
        //过滤已成功支付订单请求

        contentBiz.createAndModifyPayOrders();
        //Step2 验证是否有对应交易订单。并对其进行检验。

        //Step3 创建交易订单，并保存参数。

        //TODO 2018/4/19  req.put("notifyUrl","http://10.193.17.86:46959/content/content/payNotify");

        //Step5 拼接参数。
        Map<String,String> req = new HashMap();

        req.put("timestamp",payPageVO.getTimestamp());
        req.put("orderAmt",payPageVO.getOrderAmt());
        req.put("sipOrderNo", payPageVO.getSupOrderNo());
        req.put("sipOrderTime",payPageVO.getSupOrderTime());
        req.put("orderTitle",payPageVO.getProductName());
        req.put("orderInfo",payPageVO.getOrderInfo());
        req.put("timeOut",payPageVO.getTimeOut());
        req.put("payBackUrl",payPageVO.getPayBackUrl());
        req.put("productNum",payPageVO.getProductNum());
        req.put("orderShow",payPageVO.getOrderShow());
        req.put("userId",contentJumpReqVo.getUserId());
        req.put("userName",contentJumpReqVo.getUserName());

        BaseResponse<OrgInfoVo> orgByNo = orgFeignClient.findOrgByNo(contentTransaction.getOrgNo());
        if (!orgByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //内部调用错误
            log.info("查询供应商信息失败，info："+ contentTransaction.getOrgNo());
            throw new ContentException();
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();
        //获取机构登录地址
        String url = null;
        try {
            //Step4 获取对应的支付接口。
            url = contentBiz.createRequseUrl(contentJumpReqVo.getPayUrl(), JSON.toJSONString(req),orgInfoVo.getOrgPubKey(),orgInfoVo.getSipPriKey());
        } catch (Exception e) {
            log.info("封装重定向地址错误");
            throw new ContentException();
        }
        return "redirect:" + url;

    }


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
