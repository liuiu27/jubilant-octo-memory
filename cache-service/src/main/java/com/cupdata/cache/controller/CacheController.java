package com.cupdata.cache.controller;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.BankInfVo;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.orgsupplier.SupplierInfVo;
import com.cupdata.commons.vo.sysconfig.SysConfigVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.cupdata.cache.cacheUtils.CacheManager;
import com.cupdata.commons.api.cache.ICacheController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceSupplier;
import com.cupdata.commons.model.SysConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CacheController implements ICacheController {

	/**
	 * 获取系统配置
	 * 
	 * @param bankCode
	 * @param paraName
	 * @return
	 */
	public BaseResponse<SysConfigVo> getSysConfig(@PathVariable("bankCode") String bankCode,
			@PathVariable("paraName") String paraName) {
		log.info("cacheController getSysConfig is begin ... bankCode is" + bankCode + "paraName is" + paraName);
		try {
			BaseResponse<SysConfigVo> res = new BaseResponse<SysConfigVo>();
			SysConfig SysConfig = CacheManager.getSysConfig(bankCode, paraName);
			if (null == SysConfig) {
				res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
				res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
				return res;
			}
			SysConfigVo sysConfigVo = new SysConfigVo();
			sysConfigVo.setSysConfig(SysConfig);
			res.setData(sysConfigVo);
			return res;
		}catch(Exception e){
			log.error("getSysConfig error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
		
	}

	/**
	 * 获取银行信息
	 * 
	 * @param bankCode
	 * @return
	 */
	public BaseResponse<BankInfVo> getBankInf(@PathVariable("bankCode") String bankCode) {
		log.info("cacheController getBankInf is begin ... bankCode is" + bankCode);
		try {
			BaseResponse<BankInfVo> res = new BaseResponse<BankInfVo>();
			BankInf bankInf = CacheManager.getBankInf(bankCode);
			if (null == bankInf) {
				res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
				res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
				return res;
			}
			BankInfVo bankInfVo = new BankInfVo();
			bankInfVo.setBankInf(bankInf);
			res.setData(bankInfVo);
			return res;
		}catch(Exception e){
			log.error("getBankInf error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	/**
	 * 获取机构信息
	 * 
	 * @param orgNo
	 * @return
	 */
	public BaseResponse<OrgInfVo> getOrgInf(@PathVariable("orgNo") String orgNo) {
		log.info("cacheController getOrgInf is begin ... orgNo is" + orgNo);
		try {
			BaseResponse<OrgInfVo> res = new BaseResponse<OrgInfVo>();
			OrgInf orgInf = CacheManager.getOrgInf(orgNo);
			if (null == orgInf) {
				res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
				res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
				return res;
			}
			OrgInfVo orgInfVo = new OrgInfVo();
			orgInfVo.setOrgInf(orgInf);
			res.setData(orgInfVo);
			return res;
		}catch(Exception e){
			log.error("getOrgInf error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	/**
	 * 获取供应商信息
	 * 
	 * @param supplierNo
	 * @return
	 */
	public BaseResponse<SupplierInfVo> getSupplier(@PathVariable("supplierNo") String supplierNo) {
		log.info("cacheController getOrgInf is begin ... supplierNo is" + supplierNo);
		try {
			BaseResponse<SupplierInfVo> res = new BaseResponse<SupplierInfVo>();
			ServiceSupplier serviceSupplier = CacheManager.getSupplier(supplierNo);
			if (null == serviceSupplier) {
				res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
				res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
				return res;
			}
			SupplierInfVo supplierInfVo = new SupplierInfVo();
			supplierInfVo.setSuppliersInf(serviceSupplier);
			res.setData(supplierInfVo);
			return res;
		}catch(Exception e){
			log.error("getSupplier error is" + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}
}
