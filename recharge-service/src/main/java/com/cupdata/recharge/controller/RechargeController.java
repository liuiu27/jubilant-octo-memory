package com.cupdata.recharge.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cupdata.commons.api.recharge.IRechargeController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeQueryReq;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import com.cupdata.commons.vo.recharge.RechargeResQuery;
import com.cupdata.recharge.feign.OrderFeignClient;
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
    private OrderFeignClient orderFeignClient;

    @Autowired
    private RestTemplate restTemplate;

	/**
	 *充值业务逻辑在这里进行判断处理,通过不同的机构来进行调用不同的服务进行充值业务
	 * @param org
	 * @param rechargeReq
	 * @param request
	 * @param response
	 * @return
	 */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value = "org" ,required = true) String org,
                                              @RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {
    	try {
	        log.info("开始访问充值服务......org:"+org+",ProductNo:"+rechargeReq.getProductNo());
	        //响应信息:默认为成功
	        BaseResponse<RechargeRes> res = new BaseResponse<RechargeRes>();
	
	        // Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
	        if (null == rechargeReq || StringUtils.isBlank(rechargeReq.getProductNo())
	                || StringUtils.isBlank(rechargeReq.getOrderDesc())
	                || StringUtils.isBlank(rechargeReq.getOrgOrderNo())){
	            log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
	            res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
	            res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
	            return res;
	        }
	
	        // Step2：查询服务产品信息：查看机构是否存在此服务产品
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
	
	        // Step4：查询服务产品与机构是否关联：验证机构是否有该产品权限
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
	        String url = "http://" + productInfRes.getData().getProduct().getServiceApplicationPath() + "/getRecharge?org="
	                + org;
	        log.info("请求充值业务路径为:"+url);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	        HttpEntity<RechargeReq> entity = new HttpEntity<RechargeReq>(rechargeReq, headers);
	        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        String jsonStr = responseEntity.getBody();
	        BaseResponse<RechargeRes> rechargeResult = JSONObject.parseObject(jsonStr,
	                new TypeReference<BaseResponse<RechargeRes>>() {
	                });
	        return rechargeResult;
    	} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
    }

    /**
     * 虚拟充值查询接口
     * @param org
     * @param req
     * @return
     */
	@Override
	public BaseResponse<RechargeResQuery> rechargeQuery(@RequestParam(value = "org" ,required = true) String org, @RequestBody RechargeQueryReq req) {
		log.info("开始访问虚拟充值业务接口...org:"+org+",OrgOrderNo:"+req.getOrgOrderNo());
        BaseResponse<RechargeResQuery> rechargeResQuery = new BaseResponse<RechargeResQuery>();
	    try{
	        //step1.判断请求参数是否合法
            if (StringUtils.isBlank(org) || CommonUtils.isNullOrEmptyOfObj(req)){
                rechargeResQuery.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
                rechargeResQuery.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                log.error(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
                return rechargeResQuery;
            }

            //step2.开始查询订单
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.getRechargeOrderByOrgNoAndOrgOrderNo(org, req.getOrgOrderNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())) {
                rechargeResQuery.setResponseCode(rechargeOrderRes.getResponseCode());
                rechargeResQuery.setResponseMsg(rechargeOrderRes.getResponseMsg());
                log.error("查询充值订单controller,调用订单服务查询充值订单失败");
                return rechargeResQuery;
            }

            //step3.根据订单查询产品信息
            BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(rechargeOrderRes.getData().getRechargeOrder().getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                    || CommonUtils.isNullOrEmptyOfObj(productInfRes.getData())) {
                rechargeResQuery.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                rechargeResQuery.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                log.error("查询服务产品信息失败,ProductNo:" + rechargeOrderRes.getData().getRechargeOrder().getProductNo());
                return rechargeResQuery;
            }

            // Step4.判断服务产品是否为充值商品
            if (!ModelConstants.PRODUCT_TYPE_RECHARGE.equals(productInfRes.getData().getProduct().getProductType())) {
                rechargeResQuery.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                rechargeResQuery.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                log.error("服务产品类型异常,ProductType:" + productInfRes.getData().getProduct().getProductType());
                return rechargeResQuery;
            }

            // Step5.查询服务产品与机构是否关联：验证机构是否有该产品权限
            BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, rechargeOrderRes.getData().getRechargeOrder().getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                    || CommonUtils.isNullOrEmptyOfObj(orgProductRelRes.getData())) {
                rechargeResQuery.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                rechargeResQuery.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                log.error("服务产品与机构关联异常,OrgNo:"+orgProductRelRes.getData().getOrgProductRela().getOrgNo());
                return rechargeResQuery;
            }

            // Step6.调用微服务来查询充值订单
            String url = "http://" + productInfRes.getData().getProduct().getServiceApplicationPath() + "/rechargeQuery?org=" + org;
            log.info("请求充值查询业务路径为:"+url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<RechargeQueryReq> entity = new HttpEntity<RechargeQueryReq>(req, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            String jsonStr = responseEntity.getBody();
            BaseResponse<RechargeResQuery> rechargeResult = JSONObject.parseObject(jsonStr, new TypeReference<BaseResponse<RechargeResQuery>>() {});
            return rechargeResult;
        }catch (Exception e){
            log.error("error is " + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
	}
}
