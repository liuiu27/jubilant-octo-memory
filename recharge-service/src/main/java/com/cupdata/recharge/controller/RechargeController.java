package com.cupdata.recharge.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cupdata.commons.api.recharge.IRechargeController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import com.cupdata.recharge.feign.ProductFeignClient;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auth: LinYong
 * @Description: 充值业务相关control
 * @Date: 10:34 2017/12/21
 */
@Slf4j
@RestController
public class RechargeController implements IRechargeController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 充值业务逻辑在这里进行判断处理,通过不同的机构来进行调用不同的服务进行充值业务
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value = "org" ,required = true) String org,
                                              @RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {

        log.info("开始访问充值服务......");
        //响应信息:默认为成功
        BaseResponse<RechargeRes> res = new BaseResponse<RechargeRes>();

        // Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
        if (null == rechargeReq || StringUtils.isNotBlank(rechargeReq.getProductNo())
                || StringUtils.isNotBlank(rechargeReq.getOrderDesc())
                || StringUtils.isNotBlank(rechargeReq.getOrgOrderNo())){
            log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
            res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
            res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
            return res;
        }

        // Step2：查询服务产品信息
        BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(rechargeReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                || null == productInfRes.getData()) {// 如果查询失败
            log.error("procduct-service  findByProductNo result is null......  productNo is" + rechargeReq.getProductNo()
                    + " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return res;
        }

        // Step3：判断服务产品是否为充值商品
        if (!ModelConstants.PRODUCT_TYPE_RECHARGE.equals(productInfRes.getData().getProduct().getProductType())) {
            log.error("Not a recharge product.....poductType is" + productInfRes.getData().getProduct().getProductType()
                    + " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return res;
        }

        // Step4：查询服务产品与机构是否关联
        BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, rechargeReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                || null == orgProductRelRes.getData()) {
            log.error("procduct-service findRel result is null...org is" + org + "productNo is "
                    + rechargeReq.getProductNo() + " errorCode is "
                    + ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
            return res;
        }

        // Step5：根据对应配置信息中的服务名称，调用不同的微服务进行完成充值业务
        String url = "http://" + productInfRes.getData().getProduct().getServiceApplicationPath() + "/recharge?org="
                + org;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<RechargeReq> entity = new HttpEntity<RechargeReq>(rechargeReq, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String jsonStr = responseEntity.getBody();
        BaseResponse<RechargeRes> recharResult = JSONObject.parseObject(jsonStr,
                new TypeReference<BaseResponse<RechargeRes>>() {
                });
        return recharResult;
    }
}
