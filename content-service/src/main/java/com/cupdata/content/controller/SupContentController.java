package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.exception.ContentException;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.OrgFeignClient;
import com.cupdata.content.feign.SupplierFeignClient;
import com.cupdata.content.vo.*;
import com.cupdata.content.vo.request.*;
import com.cupdata.content.vo.response.ContentQueryOrderResVo;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.response.OrderContentVo;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.RSAHelper;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author 作者: liwei
 * @createDate 创建时间：2018年3月12日 下午15:42:31
 */
@Slf4j
@Controller
@RequestMapping("/supContent")
public class SupContentController {

    @Autowired
    private OrderFeignClient OrderFeignClient;

    @Autowired
    private OrgFeignClient orgFeignClient;

    @Autowired
    private SupplierFeignClient supplierFeignClient;

    @Autowired
    private ContentBiz contentBiz;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    /**
     * 内容引入登录接口   供应商请求机构
     *
     * @param loginReq
     * @return
     */
    @PostMapping("contentLogin")
    public String contentLogin(@RequestParam(value = "sup") String sup, @Validated @RequestBody SupLoginReqVo loginReq) {
        log.info("contentLogin is begin contentLoginReq " + loginReq.toString());

        //查询是否存在此流水号
        ContentTransactionLogDTO contentTransaction = contentBiz.queryContentTransactionByTranNo(loginReq.getSipTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);

        //获取机构登录地址
        OrgJumpReqVo resJson = JSONObject.parseObject(contentTransaction.getRequestInfo(), OrgJumpReqVo.class);

        BaseResponse<OrgInfoVo> orgByNo = orgFeignClient.findOrgByNo(contentTransaction.getOrgNo());
        if (!orgByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //内部调用错误
            log.info("查询供应商信息失败，info：" + contentTransaction.getOrgNo());
            throw new ContentException();
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();
        String url = null;
        try {
            url = contentBiz.createRequseUrl(resJson.getLoginUrl(), JSON.toJSONString(loginReq), orgInfoVo.getOrgPubKey(), orgInfoVo.getSipPriKey());
        } catch (Exception e) {
            log.info("封装重定向地址错误");
            throw new ContentException();
        }
        contentTransaction.setSupNo(sup);
        contentTransaction.setRequestInfo(JSON.toJSONString(loginReq));
        contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_TO_LOGGED);

        //插入或更新 登陆 流水记录
        contentBiz.insertAndUpdateContentTransaction(contentTransaction);

        return "redirect:" + url;
    }

    /**
     * 查询订单状态
     *
     * @param sup
     * @param contentQueryOrderReq
     * @return
     */
    @ResponseBody
    @PostMapping(path = "queryOrder")
    public BaseResponse contentQueryOrder(@RequestParam(value = "sup") String sup, @RequestBody ContentQueryOrderReqVo contentQueryOrderReq) {
        log.info("contentQueryOrder is begin params contentQueryOrderReq is" + contentQueryOrderReq.toString());
        BaseResponse<ContentQueryOrderResVo> res = new BaseResponse<>();
        //contentQueryOrderReq.getTranType();
        //1 验证
        // 2 根据订单号查询 订单信息  获取机构号
        BaseResponse<OrderContentVo> contentOrderRes = OrderFeignClient.queryContentOrderBySupOrderNo(contentQueryOrderReq.getSupOrderNo());
        if (!contentOrderRes.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //查询失败
            log.error("contentOrderRes query error,info:" + contentOrderRes.toString());
            throw new ContentException();
        }
        OrderContentVo orderContentVo = contentOrderRes.getData();
        String orgNo = orderContentVo.getOrgNo();

        //查询机构表 获取机构
        BaseResponse<OrgInfoVo> orgByNo = orgFeignClient.findOrgByNo(orgNo);
        if (!orgByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            throw new ContentException();
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();

        try {
            //3 查询订单接口地址
            String queryUrl = orgByNo.getData().getContentOrderQueryUrl();

            //机构查询参数
            OrgQueryOrderVO orgQueryOrderVO = new OrgQueryOrderVO(orderContentVo.getOrderInfoVo().getOrderNo(), contentQueryOrderReq.getTranType());

            queryUrl = contentBiz.createRequseUrl(queryUrl, JSON.toJSONString(orgQueryOrderVO), orgInfoVo.getOrgPubKey(), orgInfoVo.getSipPriKey());

            //4组装 参数 发送请求给机构查询机构订单信息
            log.info("url:" + queryUrl);

            //调用机构订单查询接口
            String data = restTemplate.getForObject(queryUrl, String.class);
            data = JSON.parseObject(data).getString("data");
            data = URLDecoder.decode(data, "utf-8");

            PrivateKey sipPriKey = null;// 平台私钥
            sipPriKey = RSAHelper.getPemPrivateKey(orgInfoVo.getSipPriKey());

            //解密返回数据
            data = RSAHelper.decipher(data, sipPriKey, 117);
            log.info("decryt data:" + data);

            //创建返回对象
            ContentQueryOrderResVo contentQueryOrderResVo = JSONObject.parseObject(data, ContentQueryOrderResVo.class);

            if (!contentQueryOrderResVo.getResultCode().equals("2")) {
                return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(), contentQueryOrderResVo.getResultMsg());
            }
            //设置返回供应商订单号
            contentQueryOrderResVo.setSupOrderNo(contentQueryOrderReq.getSupOrderNo());

            log.info("data:" + contentQueryOrderResVo.toString());

            //step 5 加密返回
            BaseResponse<SupplierInfVo> supByNo = supplierFeignClient.findSupByNo(sup);

            PublicKey supPubKey = RSAHelper.getPemPublicKey(supByNo.getData().getSupplierPubKey());
            String reqData = RSAHelper.encipher(JSON.toJSONString(contentQueryOrderResVo), supPubKey, 117);

            return new BaseResponse(reqData);
        } catch (Exception e) {

            log.info("调用失败……", e);
            return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(), ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());
        }


    }

    /**
     * 支付请求接口
     *
     * @param sup       供应商号
     * @param payPageVO 请求参数
     * @return
     */
    @PostMapping("payRequest")
    public String payRequest(@RequestParam(value = "sup") String sup, @RequestBody @Validated PayPageVO payPageVO) {
        //Step1 验证本流水是否有效
        //查询数据库中是否存在此流水号。没有直接报错
        ContentTransactionLogDTO contentTransaction = contentBiz.queryContentTransactionByTranNo(payPageVO.getSipTranNo(), ModelConstants.CONTENT_TYPE_NOT_LOGGED);

        //获取上次跳转信息
        OrgJumpReqVo oldJumpReqVo = JSONObject.parseObject(contentTransaction.getRequestInfo(), OrgJumpReqVo.class);
        //过滤已成功支付订单请求
        //Step2 创建交易订单，并保存参数。
        OrderContentVo payOrders = contentBiz.createOrModifyPayOrders(payPageVO, oldJumpReqVo, contentTransaction.getOrgNo(), sup);

        //Step5 拼接机构所需要的参数。
        OrgPayVO orgPayVO = new OrgPayVO();

        orgPayVO.setOrderAmt(payPageVO.getOrderAmt());
        orgPayVO.setOrderInfo(payPageVO.getOrderInfo());
        orgPayVO.setOrderShow(payPageVO.getOrderShow());
        orgPayVO.setOrderTitle(payPageVO.getOrderTitle());
        orgPayVO.setProductNum(payPageVO.getProductNum());
        orgPayVO.setUserId(oldJumpReqVo.getUserId());
        orgPayVO.setUserName(oldJumpReqVo.getUserName());
        orgPayVO.setTimeOut(payPageVO.getTimeOut());
        orgPayVO.setSipOrderTime(payPageVO.getSupOrderTime());
        orgPayVO.setSipOrderNo(payOrders.getOrderInfoVo().getOrderNo());
        orgPayVO.setPayBackUrl(payPageVO.getPayBackUrl());
        orgPayVO.setProductNo(payPageVO.getProductNo());
        orgPayVO.setTimestamp(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));

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
            url = contentBiz.createRequseUrl(oldJumpReqVo.getPayUrl(), JSON.toJSONString(orgPayVO), orgInfoVo.getOrgPubKey(), orgInfoVo.getSipPriKey());
        } catch (Exception e) {
            log.info("封装重定向地址错误");
            throw new ContentException();
        }
        return "redirect:" + url;

    }

    /**
     * 支付退款接口
     *
     * @param sup 供应商号
     * @return
     */
    @ResponseBody
    @PostMapping("payRefund")
    public BaseResponse payRefund(@RequestParam(value = "sup") String sup,
                                  @RequestBody @Validated SupCancelPayVO cancelPayVO) {

        // 2 根据订单号查询 订单信息  获取机构号
        BaseResponse<OrderContentVo> contentOrderRes = OrderFeignClient.queryContentOrderBySupOrderNo(cancelPayVO.getSupOrderNo());
        if (!contentOrderRes.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //查询失败
            log.error("contentOrderRes query error,info:" + contentOrderRes.toString());
            throw new ContentException();
        }
        OrderContentVo orderContentVo = contentOrderRes.getData();
        String orgNo = orderContentVo.getOrgNo();

        //查询机构表 获取机构
        BaseResponse<OrgInfoVo> orgByNo = orgFeignClient.findOrgByNo(orgNo);
        if (!orgByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            throw new ContentException();
        }
        OrgInfoVo orgInfoVo = orgByNo.getData();
        //机构退货地址
        String contentOrderRefundUrl = orgInfoVo.getContentOrderRefundUrl();

        OrgRefundReqVO orgRefundReqVO = new OrgRefundReqVO();

        orgRefundReqVO.setRefundAmt(cancelPayVO.getRefundAmt());
        orgRefundReqVO.setRefundDate(cancelPayVO.getRefundDate());
        orgRefundReqVO.setRefundInfo(cancelPayVO.getRefundInfo());
        orgRefundReqVO.setSipOrderNo(orderContentVo.getOrderInfoVo().getOrderNo());
        orgRefundReqVO.setTimestamp(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));

        log.info("contentOrderRefundUrl:" + contentOrderRefundUrl);

        try {
            //Step4 获取对应的支付接口。
            contentOrderRefundUrl = contentBiz.createRequseUrl(contentOrderRefundUrl, JSON.toJSONString(orgRefundReqVO), orgInfoVo.getOrgPubKey(), orgInfoVo.getSipPriKey());
            String data = restTemplate.getForObject(contentOrderRefundUrl, String.class);
            data = JSON.parseObject(data).getString("data");
            data = URLDecoder.decode(data, "utf-8");

            PrivateKey sipPriKey = null;// 平台私钥
            sipPriKey = RSAHelper.getPemPrivateKey(orgInfoVo.getSipPriKey());
            //解密返回数据
            data = RSAHelper.decipher(data, sipPriKey, 117);
            log.info("decryt data:" + data);
            OrgRefundResVo orgRefundResVo = JSONObject.parseObject(data, OrgRefundResVo.class);

            if (!orgRefundResVo.getResultCode().equals("2")) {

                return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(), orgRefundResVo.getResultMsg());
            }

            //更新订单状态
            orderContentVo.getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_REFUND);
            OrderFeignClient.updateContentOrder(orderContentVo);

            SupRefundResVO supRefundResVO = new SupRefundResVO();

            supRefundResVO.setSipOrderNo(orderContentVo.getOrderInfoVo().getOrderNo());
            supRefundResVO.setRefundAmt(cancelPayVO.getRefundAmt());
            supRefundResVO.setSupOrderNo(cancelPayVO.getSupOrderNo());

            //step 5 加密返回
            BaseResponse<SupplierInfVo> supByNo = supplierFeignClient.findSupByNo(sup);

            PublicKey supPubKey = RSAHelper.getPemPublicKey(supByNo.getData().getSupplierPubKey());
            String reqData = RSAHelper.encipher(JSON.toJSONString(supRefundResVO), supPubKey, 117);

            return new BaseResponse(reqData);

        } catch (Exception e) {
            log.info("封装重定向地址错误");
            return new BaseResponse(ResponseCodeMsg.ILLEGAL_PARTNER.getCode(), ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());

        }

    }

}
