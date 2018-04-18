package com.cupdata.voucher.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.voucher.IVoucherController;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.constant.TimeConstants;
import com.cupdata.sip.common.lang.exception.ErrorException;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.voucher.feign.OrderFeignClient;
import com.cupdata.voucher.feign.ProductFeignClient;
import com.cupdata.voucher.utils.ExecuteThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: DingCong
 * @Description: 获取券码
 * @@Date: Created in 10:53 2018/4/13
 */
@Slf4j
@RestController
public class VoucherController implements IVoucherController{

    @Autowired
    private ProductFeignClient productFeignClient ;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExecuteThreadPool executeThreadPool;

    @Override
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam("org") String org, @RequestBody GetVoucherReq voucherReq) {

        log.info("VoucherController getVoucher is begin... org:"+org+" ,ProductNo:"+voucherReq.getProductNo()+" ,OrderDesc:"+voucherReq.getOrderDesc());
        //响应信息
        BaseResponse<GetVoucherRes> res = new BaseResponse();// 默认为成功
        try {
            // Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
            if (null == voucherReq || StringUtils.isBlank(voucherReq.getOrgOrderNo())
                    || StringUtils.isBlank(voucherReq.getProductNo()) || StringUtils.isBlank(voucherReq.getOrderDesc())) {
                log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return res;
            }

            // Step2：查询服务产品信息
            BaseResponse<ProductInfoVo> productInfRes = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                    || null == productInfRes.getData()) {// 如果查询失败
                log.error("procduct-service  findByProductNo result is null......  productNo is" + voucherReq.getProductNo()
                        + " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return res;
            }

            // Step3：判断服务产品是否为券码商品
            if (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().getProductType())) {
                log.error("Not a voucher product.....poductType is" + productInfRes.getData().getProductType()
                        + " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return res;
            }

            // Step4：查询服务产品与机构是否关联
            BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                    || null == orgProductRelRes.getData()) {
                log.error("procduct-service findRel result is null...org is" + org + "productNo is "
                        + voucherReq.getProductNo() + " errorCode is "
                        + ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                return res;
            }

            // Step5：根据券码商品配置信息中的服务名称，调用不同的微服务获取券码
            // http://trvok-service/trvok/trvok/getVoucher
            String url = "http://" + productInfRes.getData().getServiceApplicationPath() + "/getVoucher?org=" + org;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<GetVoucherReq> entity = new HttpEntity<GetVoucherReq>(voucherReq, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            String jsonStr = responseEntity.getBody();
            res = JSONObject.parseObject(jsonStr,
                    new TypeReference<BaseResponse<GetVoucherRes>>() {
                    });

        }catch(Exception e){
            log.error("getVoucher error is" + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
        return res;
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
        log.info("VoucherController disableVoucher is begin....");
        BaseResponse<DisableVoucherRes> res = new BaseResponse<>();
        try {
            // Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、禁用描述是否为空、券码号是否为空
            if (null == disableVoucherReq || StringUtils.isBlank(disableVoucherReq.getOrgOrderNo())
                    || StringUtils.isBlank(disableVoucherReq.getDisableDesc())
                    || StringUtils.isBlank(disableVoucherReq.getVoucherCode())) {
                log.error("params is null! + errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return res;
            }

            // Step2 查询订单和券码
            BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByOrgNoAndOrgOrderNo(org,
                    disableVoucherReq.getOrgOrderNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
                log.error("order-service getVoucherOrderByOrgNoAndOrgOrderNo is  null  org is " + org + "orgOrderNo is "
                        + disableVoucherReq.getOrgOrderNo() + "errorCode is " + voucherOrderVo.getResponseCode());
                res.setResponseCode(voucherOrderVo.getResponseCode());
                res.setResponseMsg(voucherOrderVo.getResponseMsg());
                return res;
            }

            // Step3：查询服务产品信息
            BaseResponse<ProductInfoVo>  productInfRes = productFeignClient
                    .findByProductNo(voucherOrderVo.getData().getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                    || null == productInfRes.getData()) {// 如果查询失败
                log.error("procduct-service findByProductNo is null productNo is "
                        + voucherOrderVo.getData().getProductNo() + "errorCode is "
                        + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return res;
            }

            // Step4：判断服务产品是否为券码商品
            if (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().getProductType())) {
                log.error("type is not " + ModelConstants.PRODUCT_TYPE_VOUCHER + "productType is"
                        + productInfRes.getData().getProductType() + " errorCode is "
                        + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return res;
            }

            // Step5：查询服务产品与机构是否关联
            BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org,
                    voucherOrderVo.getData().getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                    || null == orgProductRelRes.getData()) {
                log.error("procduct-service select org and product is null org is" + org + "org" + "productNo is " + org,
                        voucherOrderVo.getData().getProductNo() + "errorCode is"
                                + ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                return res;
            }

            // Step6 验证券码号是否一致
            if (!voucherOrderVo.getData().getVoucherCode().equals(disableVoucherReq.getVoucherCode())) {
                log.error("voucher is not equals! errorCode is " + ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
                res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
                res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
                return res;
            }

            // Step7：根据券码商品配置信息中的服务名称，调用不同的微服务禁用券码
            // http://trvok-service/trvok/trvok/disableVoucher
            String url = "http://" + productInfRes.getData().getServiceApplicationPath()
                    + "/disableVoucher?org=" + org;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<DisableVoucherReq> entity = new HttpEntity<DisableVoucherReq>(disableVoucherReq, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            String jsonStr = responseEntity.getBody();
            res = JSONObject.parseObject(jsonStr,
                    new TypeReference<BaseResponse<DisableVoucherRes>>() {
                    });
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(res.getResponseCode())) {
                log.error("disableVoucher is fail url is" + url + "errorCode is " + res.getResponseCode());
                res.setResponseCode(res.getResponseCode());
                res.setResponseMsg(res.getResponseMsg());
                return res;
            }
            // Step8 修改数据库券码状态为禁用
            voucherOrderVo = orderFeignClient.getVoucherOrderByOrgNoAndOrgOrderNo(org, disableVoucherReq.getOrgOrderNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())) {
                log.error("order-service getVoucherOrderByOrgNoAndOrgOrderNo is fail org is " + org + "orgOrderNo is "
                        + disableVoucherReq.getOrgOrderNo() + "errorCode is " + voucherOrderVo.getResponseCode());
                res.setResponseCode(voucherOrderVo.getResponseCode());
                res.setResponseMsg(voucherOrderVo.getResponseMsg());
                return res;
            }
            voucherOrderVo.getData().setEffStatus(ModelConstants.VOUCHER_STATUS_INVALID);
            voucherOrderVo = orderFeignClient.updateVoucherOrder(voucherOrderVo.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())
                    || null == voucherOrderVo.getData()) {
                log.error("order-service updateVoucherOrder is fail! errorCode is "
                        + ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return res;
            }
        }catch(Exception e){
            log.error("disableVoucher error is" + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
        return res;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
        BaseResponse<WriteOffVoucherRes> res = new BaseResponse<>();
        // Step1：判断参数是否合法
        try {
            if (StringUtils.isBlank(writeOffVoucherReq.getVoucherCode())) {
                log.error("params is null errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return res;
            }
            // Step2：根据 券码号 供应商标识 供应商订单号 查询订单和 券码表 //TODO 供应商订单编号 传空 报错
            BaseResponse<VoucherOrderVo> voucherOrderVo = orderFeignClient.getVoucherOrderByVoucher(sup,
                    writeOffVoucherReq.getSupplierOrderNo(), writeOffVoucherReq.getVoucherCode());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())
                    || null == voucherOrderVo.getData()) {
                log.error("order-service getVoucherOrderByVoucher query result is null ! errorCode is "
                        + voucherOrderVo.getResponseCode());
                res.setResponseCode(voucherOrderVo.getResponseCode());
                res.setResponseMsg(voucherOrderVo.getResponseMsg());
                return res;
            }

            // Step3：数据库 修改券码
            voucherOrderVo.getData().setUserName(writeOffVoucherReq.getUserName());
            voucherOrderVo.getData().setUserMobileNo(writeOffVoucherReq.getUserMobileNo());
            voucherOrderVo.getData().setUseTime(DateTimeUtil.stringToDate(writeOffVoucherReq.getUseTime(), TimeConstants.DATE_PATTERN_2));
            voucherOrderVo.getData().setUsePlace(writeOffVoucherReq.getUsePlace());
            voucherOrderVo.getData().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_USE);
            voucherOrderVo = orderFeignClient.updateVoucherOrder(voucherOrderVo.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())
                    || null == voucherOrderVo.getData()) {
                log.error("updateVoucherOrder is error errorCode is " + voucherOrderVo.getResponseCode());
                res.setResponseCode(voucherOrderVo.getResponseCode());
                res.setResponseMsg(voucherOrderVo.getResponseMsg());
                return res;
            }
            // Step4：判断是否需要通知
            if(ModelConstants.IS_NOTIFY_YES.equals(voucherOrderVo.getData().getOrderInfoVo().getIsNotify())){
                // 异步通知
                executeThreadPool.addNotifyTaskToPool(voucherOrderVo.getData().getOrderInfoVo().getOrderNo());
            }
        }catch(Exception e){
            log.error("writeOffVoucher error is" + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
        return res;
    }
}
