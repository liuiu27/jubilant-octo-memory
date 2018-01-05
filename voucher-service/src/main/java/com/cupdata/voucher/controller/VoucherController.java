package com.cupdata.voucher.controller;

import com.cupdata.commons.api.voucher.IVoucherController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.voucher.*;
import com.cupdata.voucher.feign.OrderFeignClient;
import com.cupdata.voucher.feign.ProductFeignClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description: 券码相关的controller
 * @Date: 16:45 2017/12/20
 */
@RestController
public class VoucherController implements IVoucherController{
    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BaseResponse<GetVoucherRes> getVoucher(String org, @RequestBody GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response) {
        //响应信息
        BaseResponse<GetVoucherRes> res = new BaseResponse();//默认为成功

        //Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
        if (null == voucherReq || StringUtils.isBlank(voucherReq.getOrgOrderNo()) || StringUtils.isBlank(voucherReq.getProductNo()) || StringUtils.isBlank(voucherReq.getOrderDesc())) {
            res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
            res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
            return res;
        }

        //Step2：查询服务产品信息
        BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(voucherReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode()) || null == productInfRes.getData()){//如果查询失败
            res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return res;
        }

        //Step3：判断服务产品是否为券码商品
        if (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().getProduct().getProductType())){
            res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return res;
        }

        //Step4：查询服务产品与机构是否关联
        BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, voucherReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode()) || null == orgProductRelRes.getData()){
            res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
            return res;
        }

        //Step5：根据券码商品配置信息中的服务名称，调用不同的微服务获取券码
        //http://trvok-service/trvok/trvok/getVoucher
        String url = "http://" + productInfRes.getData().getProduct().getServiceApplicationPath() + "/getVoucher";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<GetVoucherReq> entity = new HttpEntity<GetVoucherReq>(voucherReq, headers);

        Map<String, String> params = new HashMap<String, String>();
        params.put("org", org);

        ResponseEntity<BaseResponse> responseEntity = restTemplate.postForEntity(url, entity, BaseResponse.class, params);
        BaseResponse<GetVoucherRes> getVoucherResult = responseEntity.getBody();
        return getVoucherResult;
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

/*    @Override
    public BaseResponse<GetVoucherRes> createByProductNo(@RequestParam(required = true) String org, @RequestBody GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response) {
        //响应信息
        BaseResponse<GetVoucherRes> res = new BaseResponse();//默认为成功

        //判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
        if (null == voucherReq || StringUtils.isBlank(voucherReq.getOrgOrderNo()) || StringUtils.isBlank(voucherReq.getProductNo()) || StringUtils.isBlank(voucherReq.getOrderDesc())) {
            res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
            res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
            return res;
        }

        //查询服务产品信息
        BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(voucherReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode()) || null == productInfRes.getData()){//如果查询失败
            res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return res;
        }

        //判断服务产品是否为券码商品
        if (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().getProduct().getProductType())){
            res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return res;
        }

        //查询服务产品与机构是否关联
        BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, voucherReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode()) || null == orgProductRelRes.getData()){
            res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
            return res;
        }

        //创建券码订单
        BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(org, voucherReq.getOrgOrderNo(), voucherReq.getOrderDesc(), voucherReq.getProductNo());
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
            return res;
        }

        //调用不同供应商的接口，获取券码并更新保存主订单/券码订单）

        VoucherObtainProxy proxy = new VoucherObtainProxy(voucherOrderRes.getData().getOrder(), voucherOrderRes.getData().getVoucherOrder());
        proxy.execute(voucherOrderRes.getData().getOrder(), voucherOrderRes.getData().getVoucherOrder());


        return null;
    }

	@Override
	public BaseResponse<BaseData> disableVoucher(String org, DisableVoucherReq disableVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		  //响应信息
        BaseResponse<BaseData> res = new BaseResponse();//默认为成功

        //判断请求参数是否合法- 订单编号 券码 是否为空
		if(null == disableVoucherReq || StringUtils.isBlank(disableVoucherReq.getOrgOrderNo()) || StringUtils.isBlank(disableVoucherReq.getVoucherCode())) {
			 res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
	         res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
	         return res;
		}
		//TODO 调用不同供应商的接口，禁用券码
		return null;
	}*/

}
