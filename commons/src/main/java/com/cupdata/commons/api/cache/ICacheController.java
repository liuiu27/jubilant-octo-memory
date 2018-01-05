package com.cupdata.commons.api.cache;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceSupplier;

@RequestMapping("/cache")
public interface ICacheController {
	
	@PostMapping("getSysConfig") 
	public String getSysConfig(String bankCode, String paraName);
	
	@PostMapping("getBankInf")
	public BankInf getBankInf(String bankCode);
	
	@PostMapping("getOrgInf")
	public OrgInf getOrgInf(String orgNo);
	
	@PostMapping("getSupplier")
	public ServiceSupplier getSupplier(String supplierNo);
}
