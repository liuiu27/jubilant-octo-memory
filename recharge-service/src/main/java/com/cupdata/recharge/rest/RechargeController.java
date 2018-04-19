package com.cupdata.recharge.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cupdata.recharge.feign.ProductFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.recharge.IRechargeController;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.RechargeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: DingCong
 * @Description: 虚拟充值
 * @@Date: Created in 10:24 2018/4/18
 */
@Slf4j
@RestController
public class RechargeController implements IRechargeController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 虚拟充值Controller
     * @param org
     * @param rechargeReq
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam("org") String org, @RequestBody RechargeReq rechargeReq) {
        log.info("调用虚拟充值Controller...org:"+org+",Account:"+rechargeReq.getAccount()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo());
        BaseResponse<RechargeRes> res = new BaseResponse<RechargeRes>();
        try{
            // Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
            if (null == rechargeReq || StringUtils.isBlank(rechargeReq.getProductNo()) || StringUtils.isBlank(rechargeReq.getAccount())
                    || StringUtils.isBlank(rechargeReq.getOrgOrderNo()) || StringUtils.isBlank(rechargeReq.getMobileNo()) || StringUtils.isBlank(rechargeReq.getOrderDesc())){
                log.error("判断请求参数是否合法发生错误,error message:" + ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return res;
            }

            // Step2：查询服务产品信息：查看机构是否存在此服务产品
            BaseResponse<ProductInfoVo> productInfRes = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                    || null == productInfRes.getData()) {// 如果查询失败
                log.error("查询服务产品信息发生错误,ProductNo:" + rechargeReq.getProductNo() + ",error message:" + ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return res;
            }

            // Step3：判断服务产品是否为充值商品
            if (!ModelConstants.PRODUCT_TYPE_RECHARGE.equals(productInfRes.getData().getProductType())) {
                log.error("判断服务产品不是充值商品,poductType:" + productInfRes.getData().getProductType() + ",error message:" + ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return res;
            }

            // Step4：查询服务产品与机构是否关联
            BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, rechargeReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                    || null == orgProductRelRes.getData()) {
                log.error("查询服务产品与机构是否关联发生错误,org:" + org + ",productNo:" + rechargeReq.getProductNo() + ",error message:"+ ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                return res;
            }

            // Step5：根据对应配置信息中的服务名称，调用不同的微服务进行完成充值业务
            String url = "http://" + productInfRes.getData().getServiceApplicationPath() + "/getRecharge?org=" + org;
            log.info("调用微服务进行完成充值业务,url:"+url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<RechargeReq> entity = new HttpEntity<RechargeReq>(rechargeReq, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            String jsonStr = responseEntity.getBody();
            BaseResponse<RechargeRes> rechargeResult = JSONObject.parseObject(jsonStr,
                    new TypeReference<BaseResponse<RechargeRes>>() {});
            return rechargeResult;

        }catch (Exception e){
            log.error("虚拟充值异常,异常信息:"+e.getMessage());
            throw new RechargeException(ResponseCodeMsg.RECHARGE_INTER_EXCEPTION.getCode(),ResponseCodeMsg.RECHARGE_INTER_EXCEPTION.getMsg());
        }
    }
}
