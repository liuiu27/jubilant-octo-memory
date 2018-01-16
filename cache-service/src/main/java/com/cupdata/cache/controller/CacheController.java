package com.cupdata.cache.controller;

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
	public String getSysConfig(@PathVariable("bankCode") String bankCode, @PathVariable("paraName") String paraName) {
		log.info("cacheController getSysConfig is begin ... bankCode is" + bankCode + "paraName is" + paraName) ;
		return CacheManager.getSysConfig(bankCode, paraName);
	}
	
	/**
	 * 获取银行信息
	 * @param bankCode
	 * @return
	 */
	public BankInf getBankInf(@PathVariable("bankCode") String bankCode) {
		log.info("cacheController getBankInf is begin ... bankCode is" + bankCode) ;
		return CacheManager.getBankInf(bankCode);
	}
	
	/**
	 * 获取机构信息
	 * @param orgNo
	 * @return
	 */
	public OrgInf getOrgInf(@PathVariable("orgNo") String orgNo) {
		log.info("cacheController getOrgInf is begin ... orgNo is" + orgNo) ;
		return CacheManager.getOrgInf(orgNo);
	}
	
	/**
	 * 获取供应商信息
	 * @param supplierNo
	 * @return
	 */
	public ServiceSupplier getSupplier(@PathVariable("supplierNo") String supplierNo) {
		log.info("cacheController getOrgInf is begin ... supplierNo is" + supplierNo) ;
		return CacheManager.getSupplier(supplierNo);
	}
	
	
}
