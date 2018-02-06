package com.cupdata.trvok.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.cdd.ICddController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.cdd.CddCodeReq;
import com.cupdata.commons.vo.cdd.CddCodeRes;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.sysconfig.SysConfigVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
import com.cupdata.commons.vo.voucher.DisableVoucherReq;
import com.cupdata.commons.vo.voucher.DisableVoucherRes;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import com.cupdata.commons.vo.voucher.WriteOffVoucherReq;
import com.cupdata.commons.vo.voucher.WriteOffVoucherRes;
import com.cupdata.trvok.biz.CddBiz;
import com.cupdata.trvok.feign.CacheFeignClient;
import com.cupdata.trvok.feign.OrderFeignClient;
import com.cupdata.trvok.feign.ProductFeignClient;
import com.cupdata.trvok.utils.CddUtil;

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
	private CacheFeignClient cacheFeignClient;
	
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
	public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value="org", required=true) String org,@RequestBody GetVoucherReq voucherReq, HttpServletRequest request,
			HttpServletResponse response) throws ErrorException {
		log.info("getVoucher is begin ........ org is " + org + "voucherReq is " + voucherReq.toString());
		BaseResponse<GetVoucherRes>  res = new  BaseResponse<GetVoucherRes>();
		try {
			//从缓存中获取请求URL
			BaseResponse<SysConfigVo> sysConfigVo = cacheFeignClient.getSysConfig("SIP", CDD_URL);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
				log.error("cache-service getSysConfig result is null  params is + " + " SIP " +  CDD_URL);
				res.setResponseCode(sysConfigVo.getResponseCode());
				res.setResponseMsg(sysConfigVo.getResponseMsg());
				return res;
			}
			String  cddUrl = sysConfigVo.getData().getSysConfig().getParaValue();
			
			//获取身份标识
			sysConfigVo = cacheFeignClient.getSysConfig("SIP", CDD_APIKEY);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
				log.error("cache-service getSysConfig result is null  params is + " + " SIP " +  CDD_APIKEY);
				res.setResponseCode(sysConfigVo.getResponseCode());
				res.setResponseMsg(sysConfigVo.getResponseMsg());
				return res;
			}
			
			//初始化获取车点点请求实体
			CddCodeReq cddCodeReq = new CddCodeReq();
			cddCodeReq.setApiKey(sysConfigVo.getData().getSysConfig().getParaValue()); 
			
			//获取API私钥
			sysConfigVo = cacheFeignClient.getSysConfig("SIP", CDD_APISECRET);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
				log.error("cache-service getSysConfig result is null  params is + " + " SIP " +  CDD_APISECRET);
				res.setResponseCode(sysConfigVo.getResponseCode());
				res.setResponseMsg(sysConfigVo.getResponseMsg());
				return res;
			}
			cddCodeReq.setMobile(CddUtil.aesUrlEncode(voucherReq.getMobileNo(), sysConfigVo.getData().getSysConfig().getParaValue()));
			cddCodeReq.setApiSign(cddCodeReq.getApiKey() + sysConfigVo.getData().getSysConfig().getParaValue() + cddCodeReq.getApiST());
			
			//获取机构ID
			sysConfigVo = cacheFeignClient.getSysConfig("SIP", CDD_AGENCY_ID);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
				log.error("cache-service getSysConfig result is null  params is + " + " SIP " +  CDD_AGENCY_ID);
				res.setResponseCode(sysConfigVo.getResponseCode());
				res.setResponseMsg(sysConfigVo.getResponseMsg());
				return res;
			}
			cddCodeReq.setAgencyID(sysConfigVo.getData().getSysConfig().getParaValue());
			
			//获取礼包数量
			sysConfigVo = cacheFeignClient.getSysConfig("SIP", CDD_NUM);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
				log.error("cache-service getSysConfig result is null  params is + " + " SIP " +  CDD_NUM);
				res.setResponseCode(sysConfigVo.getResponseCode());
				res.setResponseMsg(sysConfigVo.getResponseMsg());
				return res;
			}
			cddCodeReq.setNum(sysConfigVo.getData().getSysConfig().getParaValue());
			
			//获取订单类型
			sysConfigVo = cacheFeignClient.getSysConfig("SIP", CDD_ORDER_TYPR);
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(sysConfigVo.getResponseCode())) {
				log.error("cache-service getSysConfig result is null  params is + " + " SIP " +  CDD_ORDER_TYPR);
				res.setResponseCode(sysConfigVo.getResponseCode());
				res.setResponseMsg(sysConfigVo.getResponseMsg());
				return res;
			}
			cddCodeReq.setOrderType(sysConfigVo.getData().getSysConfig().getParaValue());//订单类型
			cddCodeReq.setApiST(DateTimeUtil.getTenTimeStamp());//时间戳
			
			BaseResponse<ProductInfVo> productInfo = new BaseResponse<>();
			//获取供应商产品
			productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
			if(!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
				log.error("product-service findByProductNo result is null  productNo is + " + voucherReq.getProductNo());
				res.setResponseCode(productInfo.getResponseCode());
				res.setResponseMsg(productInfo.getResponseMsg());
				return res;
			}
			cddCodeReq.setPackageID(productInfo.getData().getProduct().getSupplierParam());//礼包ID
			
			//创建券码订单
			CreateVoucherOrderVo createVoucherOrderVo = new CreateVoucherOrderVo();
			createVoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
			createVoucherOrderVo.setOrgNo(org);
			createVoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
			createVoucherOrderVo.setProductNo(voucherReq.getProductNo());
		    BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createVoucherOrderVo);
		    if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
		        log.error("order-service error params is " + createVoucherOrderVo.toString());
		    	res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		        res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
		        return res;
		    } 
			cddCodeReq.setSn(voucherOrderRes.getData().getOrder().getOrderNo());//订单 流水号
			cddCodeReq.setOpenCode("ZJ");//城市机构代码   //TODO  暂时无用字段
			
			//获取券码
			BaseResponse<CddCodeRes> cddCodeRes = cddBiz.getVoucherCode(cddCodeReq, cddUrl);
			if (!ResponseCodeMsg.SUCCESS.getCode().equals(cddCodeRes.getResponseCode()) || null == cddCodeRes.getData()){
		            res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		            res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
		            return res;
		    } 
			//修改订单状态 保存券码
			voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
			voucherOrderRes.getData().getVoucherOrder().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
			voucherOrderRes.getData().getVoucherOrder().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
			voucherOrderRes.getData().getVoucherOrder().setVoucherCode(cddCodeRes.getData().getYzm());
			voucherOrderRes.getData().getVoucherOrder().setStartDate(cddCodeRes.getData().getDateBeginTime());
			voucherOrderRes.getData().getVoucherOrder().setEndDate(cddCodeRes.getData().getDateOutTime());
			voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
		    if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrder() || null == voucherOrderRes.getData().getVoucherOrder()){
		        res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
		        res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
		        return res;
		    } 
			//响应参数
			GetVoucherRes voucherRes = new GetVoucherRes();
			voucherRes.setExpire(voucherReq.getExpire());
			voucherRes.setOrderNo(voucherOrderRes.getData().getOrder().getOrderNo());
			voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
			voucherRes.setVoucherCode(cddCodeRes.getData().getYzm());
			voucherRes.setStartDate(voucherOrderRes.getData().getVoucherOrder().getStartDate());
			res.setData(voucherRes);
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			throw new ErrorException("系统异常!");
		}
		return res;
	}

	@Override
	public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
