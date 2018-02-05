package com.cupdata.voucher.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cupdata.commons.api.voucher.IVoucherController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.constant.TimeConstants;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;
import com.cupdata.commons.vo.voucher.DisableVoucherRes;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.commons.vo.voucher.WriteOffVoucherReq;
import com.cupdata.commons.vo.voucher.WriteOffVoucherRes;
import com.cupdata.voucher.feign.OrderFeignClient;
import com.cupdata.voucher.feign.OrgFeignClient;
import com.cupdata.voucher.feign.ProductFeignClient;
import com.cupdata.voucher.utils.ExecuteThreadPool;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description: 券码相关的controller
 * @Date: 16:45 2017/12/20
 */
@Slf4j
@RestController
public class VoucherController implements IVoucherController {
	@Autowired
	private ProductFeignClient productFeignClient;

	@Autowired
	private OrgFeignClient orgFeignClient;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderFeignClient orderFeignClient;

	// @Autowired
	// private NotifyFeignClient notifyFeignClient;

	@Autowired
	private ExecuteThreadPool executeThreadPool;

	@GetMapping("/test")
	public BaseResponse test() {
		log.info("VoucherController getVoucher is begin...");
		throw new ErrorException("10000","异常测试");
	}
	@Override
	public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value = "org", required = true) String org,
			@RequestBody GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse response) throws ErrorException{
		log.info("VoucherController getVoucher is begin...");
		 //响应信息
		BaseResponse<GetVoucherRes> res = new BaseResponse();// 默认为成功

		// Step1：判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空
		if (null == voucherReq || StringUtils.isBlank(voucherReq.getOrgOrderNo())
				|| StringUtils.isBlank(voucherReq.getProductNo()) || StringUtils.isBlank(voucherReq.getOrderDesc())) {
			log.error("params is null.......  errorCode is " + ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
			res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
			res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg());
			return res;
		}

		// Step2：查询服务产品信息
		BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(voucherReq.getProductNo());
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
				|| null == productInfRes.getData()) {// 如果查询失败
			log.error("procduct-service  findByProductNo result is null......  productNo is" + voucherReq.getProductNo()
					+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
			return res;
		}

		// Step3：判断服务产品是否为券码商品
		if (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().getProduct().getProductType())) {
			log.error("Not a voucher product.....poductType is" + productInfRes.getData().getProduct().getProductType()
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
		String url = "http://" + productInfRes.getData().getProduct().getServiceApplicationPath() + "/getVoucher?org="
				+ org;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<GetVoucherReq> entity = new HttpEntity<GetVoucherReq>(voucherReq, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
		String jsonStr = responseEntity.getBody();
		BaseResponse<GetVoucherRes> getVoucherResult = JSONObject.parseObject(jsonStr,
				new TypeReference<BaseResponse<GetVoucherRes>>() {
				});
		return getVoucherResult;
	}

	@Override
	public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value = "org", required = true) String org,
			@RequestBody DisableVoucherReq disableVoucherReq, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("VoucherController disableVoucher is begin....");
		BaseResponse<DisableVoucherRes> res = new BaseResponse<>();
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
		BaseResponse<ProductInfVo> productInfRes = productFeignClient
				.findByProductNo(voucherOrderVo.getData().getVoucherOrder().getProductNo());
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
				|| null == productInfRes.getData()) {// 如果查询失败
			log.error("procduct-service findByProductNo is null productNo is "
					+ voucherOrderVo.getData().getVoucherOrder().getProductNo() + "errorCode is "
					+ ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
			return res;
		}

		// Step4：判断服务产品是否为券码商品
		if (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().getProduct().getProductType())) {
			log.error("type is not " + ModelConstants.PRODUCT_TYPE_VOUCHER + "productType is"
					+ productInfRes.getData().getProduct().getProductType() + " errorCode is "
					+ ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
			return res;
		}

		// Step5：查询服务产品与机构是否关联
		BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org,
				voucherOrderVo.getData().getVoucherOrder().getProductNo());
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
				|| null == orgProductRelRes.getData()) {
			log.error("procduct-service select org and product is null org is" + org + "org" + "productNo is " + org,
					voucherOrderVo.getData().getVoucherOrder().getProductNo() + "errorCode is"
							+ ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
			res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
			res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
			return res;
		}

		// Step6 验证券码号是否一致
		if (!voucherOrderVo.getData().getVoucherOrder().getVoucherCode().equals(disableVoucherReq.getVoucherCode())) {
			log.error("voucher is not equals! errorCode is " + ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
		}

		// Step7：根据券码商品配置信息中的服务名称，调用不同的微服务禁用券码
		// http://trvok-service/trvok/trvok/disableVoucher
		String url = "http://" + productInfRes.getData().getProduct().getServiceApplicationPath()
				+ "/disableVoucher?org=" + org;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<DisableVoucherReq> entity = new HttpEntity<DisableVoucherReq>(disableVoucherReq, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
		String jsonStr = responseEntity.getBody();
		BaseResponse<DisableVoucherRes> disableVoucherRes = JSONObject.parseObject(jsonStr,
				new TypeReference<BaseResponse<DisableVoucherRes>>() {
				});
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(disableVoucherRes.getResponseCode())) {
			log.error("disableVoucher is fail url is" + url + "errorCode is " + disableVoucherRes.getResponseCode());
			res.setResponseCode(disableVoucherRes.getResponseCode());
			res.setResponseMsg(disableVoucherRes.getResponseMsg());
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
		voucherOrderVo.getData().getVoucherOrder().setEffStatus(ModelConstants.VOUCHER_STATUS_INVALID);
		voucherOrderVo = orderFeignClient.updateVoucherOrder(voucherOrderVo.getData());
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())
				|| null == voucherOrderVo.getData() || null == voucherOrderVo.getData().getOrder()
				|| null == voucherOrderVo.getData().getVoucherOrder()) {
			log.error("order-service updateVoucherOrder is fail! errorCode is "
					+ ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
			res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
			res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
			return res;
		}
		return disableVoucherRes;
	}

	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher(@RequestParam(value = "sup", required = true) String sup,
			@RequestBody WriteOffVoucherReq writeOffVoucherReq, HttpServletRequest request,
			HttpServletResponse response) {
		BaseResponse<WriteOffVoucherRes> res = new BaseResponse<>();
		// Step1：判断参数是否合法
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
				|| null == voucherOrderVo.getData() || null == voucherOrderVo.getData().getOrder()
				|| null == voucherOrderVo.getData().getVoucherOrder()) {
			log.error("order-service getVoucherOrderByVoucher query result is null ! errorCode is "
					+ voucherOrderVo.getResponseCode());
			res.setResponseCode(voucherOrderVo.getResponseCode());
			res.setResponseMsg(voucherOrderVo.getResponseMsg());
			return res;
		}
		// Step3：判断是否存在券码号
		if (!writeOffVoucherReq.getVoucherCode().equals(voucherOrderVo.getData().getVoucherOrder().getVoucherCode())) {
			log.error("voucherCode is not exist ! errorCode is " + ResponseCodeMsg.FAIL.getCode());
			res.setResponseCode(ResponseCodeMsg.FAIL.getCode());
			res.setResponseMsg(ResponseCodeMsg.FAIL.getMsg());
			return res;
		}
		// Step4：数据库 修改券码
		voucherOrderVo.getData().getVoucherOrder().setUserName(writeOffVoucherReq.getUserName());
		voucherOrderVo.getData().getVoucherOrder().setUserMobileNo(writeOffVoucherReq.getUserMobileNo());
		voucherOrderVo.getData().getVoucherOrder().setUseTime(DateTimeUtil.stringToDate(writeOffVoucherReq.getUseTime(), TimeConstants.DATE_PATTERN_2));
		voucherOrderVo.getData().getVoucherOrder().setUsePlace(writeOffVoucherReq.getUsePlace());
		voucherOrderVo.getData().getVoucherOrder().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_USE);
		voucherOrderVo = orderFeignClient.updateVoucherOrder(voucherOrderVo.getData());
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderVo.getResponseCode())
				|| null == voucherOrderVo.getData()) {
			log.error("updateVoucherOrder is error errorCode is " + voucherOrderVo.getResponseCode());
			res.setResponseCode(voucherOrderVo.getResponseCode());
			res.setResponseMsg(voucherOrderVo.getResponseMsg());
			return res;
		}
		// Step5：判断是否需要通知
		if (ModelConstants.IS_NOTIFY_YES.equals(voucherOrderVo.getData().getOrder().getIsNotify())) {
			// 异步通知
			executeThreadPool.addNotifyTaskToPool(voucherOrderVo.getData().getOrder().getOrderNo());
		}
		return res;
	}

	/*
	 * @Override public BaseResponse<GetVoucherRes>
	 * createByProductNo(@RequestParam(required = true) String org, @RequestBody
	 * GetVoucherReq voucherReq, HttpServletRequest request, HttpServletResponse
	 * response) { //响应信息 BaseResponse<GetVoucherRes> res = new
	 * BaseResponse();//默认为成功
	 * 
	 * //判断请求参数是否合法-机构订单编号是否为空、服务产品编号是否为空、订单描述是否为空 if (null == voucherReq ||
	 * StringUtils.isBlank(voucherReq.getOrgOrderNo()) ||
	 * StringUtils.isBlank(voucherReq.getProductNo()) ||
	 * StringUtils.isBlank(voucherReq.getOrderDesc())) {
	 * res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
	 * res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg()); return res; }
	 * 
	 * //查询服务产品信息 BaseResponse<ProductInfVo> productInfRes =
	 * productFeignClient.findByProductNo(voucherReq.getProductNo()); if
	 * (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
	 * || null == productInfRes.getData()){//如果查询失败
	 * res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
	 * res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg()); return res; }
	 * 
	 * //判断服务产品是否为券码商品 if
	 * (!ModelConstants.PRODUCT_TYPE_VOUCHER.equals(productInfRes.getData().
	 * getProduct().getProductType())){
	 * res.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
	 * res.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg()); return res; }
	 * 
	 * //查询服务产品与机构是否关联 BaseResponse<OrgProductRelVo> orgProductRelRes =
	 * productFeignClient.findRel(org, voucherReq.getProductNo()); if
	 * (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode()
	 * ) || null == orgProductRelRes.getData()){
	 * res.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
	 * res.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
	 * return res; }
	 * 
	 * //创建券码订单 BaseResponse<VoucherOrderVo> voucherOrderRes =
	 * orderFeignClient.createVoucherOrder(org, voucherReq.getOrgOrderNo(),
	 * voucherReq.getOrderDesc(), voucherReq.getProductNo()); if
	 * (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
	 * || null == voucherOrderRes.getData() || null ==
	 * voucherOrderRes.getData().getOrder() || null ==
	 * voucherOrderRes.getData().getVoucherOrder()){
	 * res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
	 * res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg()); return res;
	 * }
	 * 
	 * //调用不同供应商的接口，获取券码并更新保存主订单/券码订单）
	 * 
	 * VoucherObtainProxy proxy = new
	 * VoucherObtainProxy(voucherOrderRes.getData().getOrder(),
	 * voucherOrderRes.getData().getVoucherOrder());
	 * proxy.execute(voucherOrderRes.getData().getOrder(),
	 * voucherOrderRes.getData().getVoucherOrder());
	 * 
	 * 
	 * return null; }
	 * 
	 * @Override public BaseResponse<BaseData> disableVoucher(String org,
	 * DisableVoucherReq disableVoucherReq, HttpServletRequest request,
	 * HttpServletResponse response) { //响应信息 BaseResponse<BaseData> res = new
	 * BaseResponse();//默认为成功
	 * 
	 * //判断请求参数是否合法- 订单编号 券码 是否为空 if(null == disableVoucherReq ||
	 * StringUtils.isBlank(disableVoucherReq.getOrgOrderNo()) ||
	 * StringUtils.isBlank(disableVoucherReq.getVoucherCode())) {
	 * res.setResponseCode(ResponseCodeMsg.ILLEGAL_ARGUMENT.getCode());
	 * res.setResponseMsg(ResponseCodeMsg.ILLEGAL_ARGUMENT.getMsg()); return res; }
	 * //TODO 调用不同供应商的接口，禁用券码 return null; }
	 */

}
