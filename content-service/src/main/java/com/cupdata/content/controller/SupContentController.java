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

    //@ResponseBody
    //@PostMapping(path="queryOrder")
    //public BaseResponse contentQueryOrder(@RequestParam(value = "sup") String sup,@RequestBody ContentQueryOrderReq contentQueryOrderReq){
    //    log.info("contentQueryOrder is begin params contentQueryOrderReq is" + contentQueryOrderReq.toString());
    //    BaseResponse<ContentQueryOrderRes> res = new BaseResponse<ContentQueryOrderRes>();
    //    //contentQueryOrderReq.getTranType();
    //    //1 验证
    //    // 2 根据订单号查询 订单信息  获取机构号
    //    //3 查询机构表 获取机构 查询订单接口地址
    //    RestTemplate restTemplate = new RestTemplate();
    //    String url ="http://cvpc.leagpoint.com/mall/sip/content/queryContentOrderStatus.action?provider=LBHTDnJkYy4=";
    //
    //    Map<String,String> req = new HashMap();
    //    String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
    //
    //    req.put("sipOrderNo",contentQueryOrderReq.getSupOrderNo());
    //    req.put("timestamp",timestamp);
    //
    //    req.put("tranType",contentQueryOrderReq.getTranType());
    //
    //    url = EncryptionAndEecryption.Encryption(req, url);
    //
    //    log.info("url:"+url);
    //    try {
    //        String data = restTemplate.getForObject(url,String.class);
    //        JSONObject jsonObject1 = JSON.parseObject(data);
    //        data = jsonObject1.getString("data");
    //        data = URLDecoder.decode(data,"utf-8");
    //        System.out.println(data);
    //        PrivateKey sipPriKey = null;// 平台私钥
    //        sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");
    //        data = RSAUtils.decrypt(data,sipPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
    //        log.info("decryt data:"+data);
    //
    //        JSONObject jsonObject = JSONObject.parseObject(data);
    //
    //        if (!jsonObject.getString("resultCode").equals("2")){
    //            return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),jsonObject.getString("resultMsg").toString());
    //        }
    //        jsonObject.remove("resultCode");
    //        log.info("URL = "+url);
    //        log.info(data);
    //        String pubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
    //        PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
    //        String reqData = RSAUtils.encrypt(JSON.toJSONString(jsonObject), uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
    //        return  new BaseResponse(reqData);
    //    } catch (Exception e) {
    //
    //        log.info("调用失败……");
    //        return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());
    //    }
    //    //4组装 参数 发送请求给机构查询机构订单信息
    //    //5返回
    //
    //}

    /**
     * 支付请求接口
     * @param sup 供应商号
     * @param payPageVO 请求参数
     * @return
     */
    @PostMapping("payRequest")
    public String payRequest(@RequestParam(value = "sup") String sup, @RequestBody @Validated PayPageVO payPageVO ) {
        //Step1 验证本流水是否有效
        //查询数据库中是否存在此流水号。
        ContentTransactionLogDTO contentTransaction = contentBiz.queryContentTransactionByTranNo(payPageVO.getSipTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);

        //转行的出上次跳转信息
        ContentJumpReqVo contentJumpReqVo = JSONObject.parseObject(contentTransaction.getRequestInfo(), ContentJumpReqVo.class);
        //过滤已成功支付订单请求

        contentBiz.createAndModifyPayOrders();
        //Step2 验证是否有对应交易订单。并对其进行检验。

        //Step3 创建交易订单，并保存参数。

        //TODO 2018/4/19  req.put("notifyUrl","http://10.193.17.86:46959/content/content/payNotify");

        //Step5 拼接参数。
        Map<String, String> req = new HashMap();

        req.put("timestamp", payPageVO.getTimestamp());
        req.put("orderAmt", payPageVO.getOrderAmt());
        req.put("sipOrderNo", payPageVO.getSupOrderNo());
        req.put("sipOrderTime", payPageVO.getSupOrderTime());
        req.put("orderTitle", payPageVO.getProductName());
        req.put("orderInfo", payPageVO.getOrderInfo());
        req.put("timeOut", payPageVO.getTimeOut());
        req.put("payBackUrl", payPageVO.getPayBackUrl());
        req.put("productNum", payPageVO.getProductNum());
        req.put("orderShow", payPageVO.getOrderShow());
        req.put("userId", contentJumpReqVo.getUserId());
        req.put("userName", contentJumpReqVo.getUserName());

        BaseResponse<OrgInfoVo> orgByNo = orgFeignClient.findOrgByNo(contentTransaction.getOrgNo());
        if (!orgByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //内部调用错误
            log.info("查询供应商信息失败，info：" + contentTransaction.getOrgNo());
            throw new ContentException();
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();
        //获取机构登录地址
        String url = null;
        try {
            //Step4 获取对应的支付接口。
            url = contentBiz.createRequseUrl(contentJumpReqVo.getPayUrl(), JSON.toJSONString(req), orgInfoVo.getOrgPubKey(), orgInfoVo.getSipPriKey());
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
	//@PostMapping("payRefund")
	//public BaseResponse payRefund(@RequestParam(value = "sup") String sup,
	//						 @RequestBody @Validated CancelPayVO cancelPayVO ){
	//	RestTemplate restTemplate = new RestTemplate();
	//	String url ="http://cvpc.leagpoint.com/mall/sip/content/contentOrderRefund.action?provider=LBHTDnJkYy4=";
    //
	//	Map<String,String> req = new HashMap();
	//	req.put("sipOrderNo",cancelPayVO.getSupOrderNo());
	//	req.put("refundDate",cancelPayVO.getRefundDate());
	//	req.put("refundAmt", cancelPayVO.getRefundAmt());
	//	req.put("refundInfo",cancelPayVO.getRefundInfo());
	//	String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);
	//	req.put("timestamp",timestamp);
    //
	//	//JSONObject resJson = JSONObject.parseObject(contentTransaction.getRequestInfo());
	//	url = EncryptionAndEecryption.Encryption(req, url);
    //
	//	log.info("url:"+url);
	//	try {
    //
	//		String data = restTemplate.getForObject(url,String.class);
	//		JSONObject jsonObject1 = JSON.parseObject(data);
	//		data = jsonObject1.getString("data");
	//		//data = URLDecoder.decode(data,"utf-8");
     //       log.info("data:"+data);
	//		PrivateKey sipPriKey = null;// 平台私钥
	//		sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");
	//		data = RSAUtils.decrypt(data,sipPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
	//		log.info("decryt data:"+data);
    //
	//		JSONObject jsonObject = JSONObject.parseObject(data);
    //
	//		if (!jsonObject.getString("resultCode").equals("2")){
	//			return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),"退货失败");
	//		}
	//		Map<String,String> res = new HashMap<>(3);
    //
	//		res.put("sipOrderNo",jsonObject.getString("orgOrderNo"));
	//		res.put("supOrderNo",jsonObject.getString("sipOrderNo"));
	//		res.put("refundAmt","2");
    //
	//		log.info("URL = "+url);
	//		log.info(data);
    //
	//		String pubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
	//		PublicKey uppPubKey = RSAUtils.getPublicKeyFromString(pubKeyStr);
	//		String reqData = RSAUtils.encrypt(JSON.toJSONString(res), uppPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
	//		return new BaseResponse(reqData);
    //
	//	} catch (Exception e) {
	//		log.error(e.getMessage(),e);
	//		log.info("调用失败……");
	//		return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(),ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());
	//	}
    //
	//}

}
