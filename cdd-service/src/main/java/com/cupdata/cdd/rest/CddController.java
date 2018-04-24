package com.cupdata.cdd.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.cdd.biz.CddBiz;
import com.cupdata.cdd.feign.ConfigFeignClient;
import com.cupdata.cdd.feign.OrderFeignClient;
import com.cupdata.cdd.feign.ProductFeignClient;
import com.cupdata.cdd.utils.CddUtil;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.cdd.ICddController;
import com.cupdata.sip.common.api.cdd.requset.CddCodeReq;
import com.cupdata.sip.common.api.cdd.response.CddCodeRes;
import com.cupdata.sip.common.api.config.response.SysConfigVO;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.ErrorException;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年2月1日 上午10:06:28
*/
@Slf4j
@RestController
public class CddController implements ICddController{
	
	
	@Autowired
	private CddBiz cddBiz;
	
	@Autowired 
	private OrderFeignClient orderFeignClient;
	
	@Autowired 
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private ConfigFeignClient configFeignClient;
	
    private static final String CDD_APIKEY = "CDD_APIKEY"; //身份标识
    
    private static final String CDD_APISECRET = "CDD_APISECRET"; //API私钥
    
    private static final String CDD_URL = "CDD_URL";//请求URL
    
    private static final String CDD_AGENCY_ID = "CDD_AGENCY_ID"; //机构ID，由车点点平台分配给第三方商户
	
	private static final String CDD_NUM = "CDD_NUM"; //礼包数量
	 
	private static final String CDD_ORDER_TYPR = "CDD_ORDER_TYPR";//订单类型
	 
	/**
	 * 获取券码
	 */
	@Override
	public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value="org", required=true) String org,@RequestBody GetVoucherReq voucherReq) throws ErrorException {
		log.info("getVoucher is begin ........ org is " + org + "voucherReq is " + voucherReq.toString());
		try {
			BaseResponse<GetVoucherRes>  res = new  BaseResponse<GetVoucherRes>();
			//从缓存中获取请求URL
			SysConfigVO sysConfigVo = configFeignClient.getSysConfig(CDD_URL,ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_URL);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			String  cddUrl = sysConfigVo.getParaValue();
			
			//获取身份标识
			sysConfigVo = configFeignClient.getSysConfig(CDD_APIKEY,ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_APIKEY);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			
			//初始化获取车点点请求实体
			CddCodeReq cddCodeReq = new CddCodeReq();
			cddCodeReq.setApiKey(sysConfigVo.getParaValue()); 
			
			//获取API私钥
			sysConfigVo = configFeignClient.getSysConfig(CDD_APISECRET,ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_APISECRET);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			
			
			//获取机构ID
			sysConfigVo = configFeignClient.getSysConfig(CDD_AGENCY_ID, ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_AGENCY_ID);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			cddCodeReq.setAgencyID(sysConfigVo.getParaValue());
			
			//获取礼包数量
			sysConfigVo = configFeignClient.getSysConfig(CDD_NUM,ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_NUM);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			cddCodeReq.setNum(sysConfigVo.getParaValue());
			//获取订单类型
			sysConfigVo = configFeignClient.getSysConfig(CDD_ORDER_TYPR,ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_ORDER_TYPR);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			cddCodeReq.setOrderType(sysConfigVo.getParaValue());//订单类型
			cddCodeReq.setApiST(DateTimeUtil.getTenTimeStamp());//时间戳
			
			sysConfigVo = configFeignClient.getSysConfig(CDD_APISECRET,ModelConstants.ORG_TYPE_CUPD);
			if(null == sysConfigVo) {
	    		log.error("config-service getSysConfig result is null  params is + " + ModelConstants.ORG_TYPE_CUPD +  CDD_APISECRET);
	    		res.setResponseCode(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getCode());
	    		res.setResponseMsg(ResponseCodeMsg.CONFIG_QUERY_EMPTY.getMsg());
	    		return res;
			}
			cddCodeReq.setMobile(CddUtil.aesUrlEncode(voucherReq.getMobileNo(), sysConfigVo.getParaValue()));
			cddCodeReq.setApiSign(cddCodeReq.getApiKey() + sysConfigVo.getParaValue() + cddCodeReq.getApiST());
			
			BaseResponse<ProductInfoVo> productInfo = new BaseResponse<>();
			//获取供应商产品
			productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
				log.error("product-service findByProductNo result is null  productNo is + " + voucherReq.getProductNo());
				res.setResponseCode(productInfo.getResponseCode());
				res.setResponseMsg(productInfo.getResponseMsg());
				return res;
			}
			cddCodeReq.setPackageID(productInfo.getData().getSupplierParam());//礼包ID
			
			//创建券码订单
			CreateVoucherOrderVo createVoucherOrderVo = new CreateVoucherOrderVo();
			createVoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
			createVoucherOrderVo.setOrgNo(org);
			createVoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
			createVoucherOrderVo.setProductNo(voucherReq.getProductNo());
		    BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createVoucherOrderVo);
		    if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())){
		        log.error("order-service error params is " + createVoucherOrderVo.toString());
		    	res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		        res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
		        return res;
		    } 
			cddCodeReq.setSn(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());//订单 流水号
			
			//获取券码
			BaseResponse<CddCodeRes> cddCodeRes = cddBiz.getVoucherCode(cddCodeReq, cddUrl);
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(cddCodeRes.getResponseCode()) || null == cddCodeRes.getData()){
		            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
		            return res;
		    } 
			//修改订单状态 保存券码
			voucherOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
			voucherOrderRes.getData().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
			voucherOrderRes.getData().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
			voucherOrderRes.getData().setVoucherCode(cddCodeRes.getData().getYzm());
			voucherOrderRes.getData().setStartDate(cddCodeRes.getData().getDateBeginTime());
			voucherOrderRes.getData().setEndDate(cddCodeRes.getData().getDateOutTime());
			BaseResponse baseResponse = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
		    if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())){
		        res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		        res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
		        return res;
		    } 
			//响应参数
			GetVoucherRes voucherRes = new GetVoucherRes();
			voucherRes.setExpire(cddCodeRes.getData().getDateOutTime());							
			voucherRes.setOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
			voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
			voucherRes.setVoucherCode(cddCodeRes.getData().getYzm());
			voucherRes.setStartDate(cddCodeRes.getData().getDateBeginTime());
			res.setData(voucherRes);
			return res;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	@Override
	public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
		// TODO Auto-generated method stub
		return null;
	}

}
