package com.cupdata.commons.api.cache;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.BankInfVo;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.orgsupplier.SupplierInfVo;
import com.cupdata.commons.vo.sysconfig.SysConfigVo;
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
	public BaseResponse<SysConfigVo> getSysConfig(@PathVariable("bankCode") String bankCode, @PathVariable("paraName") String paraName);
	
	@GetMapping("/getBankInf/{bankCode}")
	public BaseResponse<BankInfVo> getBankInf(@PathVariable("bankCode") String bankCode);
	
	@GetMapping("/getOrgInf/{orgNo}")
	public BaseResponse<OrgInfVo> getOrgInf(@PathVariable("orgNo") String orgNo);
	
	@GetMapping("/getSupplier/{supplierNo}")
	public BaseResponse<SupplierInfVo> getSupplier(@PathVariable("supplierNo") String supplierNo);
}
