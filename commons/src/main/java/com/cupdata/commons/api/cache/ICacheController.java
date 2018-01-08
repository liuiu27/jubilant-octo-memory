package com.cupdata.commons.api.cache;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceSupplier;

@RequestMapping("/cache")
public interface ICacheController{
	
	@GetMapping("/getSysConfig/{bankCode}/{paraName}")
	public String getSysConfig(@PathVariable("bankCode") String bankCode, @PathVariable("paraName") String paraName);
	
	@GetMapping("/getBankInf/{bankCode}")
	public BankInf getBankInf(@PathVariable("bankCode") String bankCode);
	
	@GetMapping("/getOrgInf/{orgNo}")
	public OrgInf getOrgInf(@PathVariable("orgNo") String orgNo);
	
	@GetMapping("/getSupplier/{supplierNo}")
	public ServiceSupplier getSupplier(@PathVariable("supplierNo") String supplierNo);
}
