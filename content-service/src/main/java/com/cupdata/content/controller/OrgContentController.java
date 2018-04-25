package com.cupdata.content.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.content.biz.ContentBiz;
import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.exception.ContentException;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.SupplierFeignClient;
import com.cupdata.content.vo.SupContentJumReqVo;
import com.cupdata.content.vo.SupPayNotifyReqVO;
import com.cupdata.content.vo.request.ContentJumpReqVo;
import com.cupdata.content.vo.request.ContentLoginReqVo;
import com.cupdata.content.vo.request.OrgPayNotifyVO;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.response.OrderContentVo;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
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

/**
 * @author 作者: liwei
 * @createDate 创建时间：2018年3月8日 下午6:24:22
 */
@Slf4j
@Controller
@RequestMapping("/content")
public class OrgContentController {

    @Autowired
    private ContentBiz contentBiz;

    @Autowired
    private SupplierFeignClient supplierFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    /**
     * 内容引入跳转接口   机构请求
     *
     * @param org
     * @param contentJumpReqVo
     * @return
     */
    @PostMapping(path = "/contentJump")
    public String contentJump(@RequestParam(value = "org") String org,
                              @Validated @RequestBody ContentJumpReqVo contentJumpReqVo) {
        //Step1： 验证数据是否为空 是否合法
        log.info("contentJump is begin params org is" + org + "contentJumpReq is" + contentJumpReqVo.toString());

        // Step2：查询服务产品信息
        ProductInfoVo productInfRes = contentBiz.findByProductNo(contentJumpReqVo.getProductNo());

        // Step3：验证机构与产品是否关联
        contentBiz.validatedProductNo(org, productInfRes.getProductType(), productInfRes.getProductNo());

        //根据产品获取供应商 主页URL
        String supUrl = productInfRes.getServiceApplicationPath();


//		//Step5 :   判断流水号  如果为空创建 新的   如果不为空则修改
        contentBiz.queryAndinsertOrUpdateContentTransaction(contentJumpReqVo, productInfRes, ModelConstants.CONTENT_TYPE_NOT_LOGGED, null);

        //查询是否存在下一步操作  fallback地址
        ContentTransactionLogDTO contentTransactionLogDTO = contentBiz.queryContentTransactionByTranNo(contentJumpReqVo.getSipTranNo(), ModelConstants.CONTENT_TYPE_TO_LOGGED);

        if (null != contentTransactionLogDTO) {
            ContentLoginReqVo contentLoginReq = JSONObject.parseObject(contentTransactionLogDTO.getRequestInfo(), ContentLoginReqVo.class);
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
            log.info("内部调用错误");
            throw new ContentException();
        }
        //组装重定向地址及参数
        SupplierInfVo supplierInfVo = supByNo.getData();

        String url = null;
        try {
            url = contentBiz.createRequseUrl(supUrl, JSON.toJSONString(supContentJumReqVo), supplierInfVo.getSupplierPubKey(), supplierInfVo.getSipPriKey());
        } catch (Exception e) {
            //封装参数失败
            log.info("封装参数失败");
            throw new ContentException();
        }
        StringBuffer ret = new StringBuffer("redirect:" + url);
        return ret.toString();
    }

    @ResponseBody
    @PostMapping("payNotify")
    public BaseResponse payNotify(@RequestParam(value = "org") String org, @RequestBody OrgPayNotifyVO notifyVO) {

        //查询内容订单
        BaseResponse<OrderContentVo> orderContentVo = orderFeignClient.queryContentOrder(notifyVO.getSipOrderNo());
        if (!orderContentVo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //查询失败
            log.error("orderContentVo query error,info:" + orderContentVo.toString());
            throw new ContentException();
        }
        OrderContentVo contentVo = orderContentVo.getData();

        //更新内容订单
        contentVo.getOrderInfoVo().setOrgOrderNo(notifyVO.getOrgOrderNo());
        contentVo.getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
        orderFeignClient.updateContentOrder(contentVo);

        String notifyurl = contentVo.getPayNotifyUrl();

        SupPayNotifyReqVO notifyReqVO = new SupPayNotifyReqVO();
        notifyReqVO.setOrderAmt(notifyVO.getOrderAmt());
        notifyReqVO.setResultCode(notifyVO.getResultCode());
        notifyReqVO.setSipOrderNo(notifyVO.getSipOrderNo());
        notifyReqVO.setSupOrderNo(contentVo.getOrderInfoVo().getOrderNo());
        notifyReqVO.setTimestamp(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(),
                    "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));

        //封装重定向到机构的地址
        BaseResponse<SupplierInfVo> supByNo = supplierFeignClient.findSupByNo(contentVo.getSupNo());
        if (!supByNo.getResponseCode().equals(ResponseCodeMsg.SUCCESS.getCode())) {
            //内部调用错误
        }
        //组装重定向地址及参数
        SupplierInfVo supplierInfVo = supByNo.getData();
        try {
            notifyurl = contentBiz.createRequseUrl(notifyurl, JSON.toJSONString(notifyReqVO), supplierInfVo.getSupplierPubKey(), supplierInfVo.getSipPriKey());
            String response = restTemplate.getForObject(notifyurl, String.class);
            return new BaseResponse(response);
        } catch (Exception e) {
            //封装参数失败
            log.info("封装参数失败");
            return new BaseResponse(ResponseCodeMsg.FAIL.getCode(),ResponseCodeMsg.FAIL.getMsg());
        }

    }

}
