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
import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceSupplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CacheController implements ICacheController{
	
	/**
	 * 获取系统配置
	 * @param bankCode
	 * @param paraName
	 * @return
	 */
	public BaseResponse<SysConfigVo> getSysConfig(@PathVariable("bankCode") String bankCode, @PathVariable("paraName") String paraName) {
		log.info("cacheController getSysConfig is begin ... bankCode is" + bankCode + "paraName is" + paraName) ;
		BaseResponse<SysConfigVo> sysConfigVoRes = new BaseResponse();
		sysConfigVoRes.getData().setSysConfig(CacheManager.getSysConfig(bankCode, paraName));
		return sysConfigVoRes;
	}
	
	/**
	 * 获取银行信息
	 * @param bankCode
	 * @return
	 */
	public BaseResponse<BankInfVo> getBankInf(@PathVariable("bankCode") String bankCode) {
		log.info("cacheController getBankInf is begin ... bankCode is" + bankCode) ;
		BaseResponse<BankInfVo> bankInfVoRes = new BaseResponse();
		bankInfVoRes.getData().setBankInf(CacheManager.getBankInf(bankCode));
		return bankInfVoRes;
	}
	
	/**
	 * 获取机构信息
	 * @param orgNo
	 * @return
	 */
	public BaseResponse<OrgInfVo> getOrgInf(@PathVariable("orgNo") String orgNo) {
		log.info("cacheController getOrgInf is begin ... orgNo is" + orgNo) ;
		BaseResponse<OrgInfVo> orgInfVoRes = new BaseResponse();
		orgInfVoRes.getData().setOrgInf(CacheManager.getOrgInf(orgNo));
		return orgInfVoRes;
	}
	
	/**
	 * 获取供应商信息
	 * @param supplierNo
	 * @return
	 */
	public BaseResponse<SupplierInfVo> getSupplier(@PathVariable("supplierNo") String supplierNo) {
		log.info("cacheController getOrgInf is begin ... supplierNo is" + supplierNo) ;
		BaseResponse<SupplierInfVo> supplierInfVoRes = new BaseResponse();
		supplierInfVoRes.getData().setSuppliersInf(CacheManager.getSupplier(supplierNo));
		return supplierInfVoRes;
	}
}
