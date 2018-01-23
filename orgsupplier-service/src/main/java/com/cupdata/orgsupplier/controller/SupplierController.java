package com.cupdata.orgsupplier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.orgsupplier.ISupplierController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.SupplierInfListVo;
import com.cupdata.orgsupplier.biz.ServiceSupplierBiz;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 13:56 2017/12/20
 */
@RestController
public class SupplierController implements ISupplierController {

	@Autowired
	private ServiceSupplierBiz ServiceSupplierBiz;
	
	@Override
	public BaseResponse<SupplierInfListVo> selectAll() {
		BaseResponse<SupplierInfListVo> supplierInfListVoRes = new BaseResponse<>();
		SupplierInfListVo supplierInfListVo = new SupplierInfListVo();
		supplierInfListVo.setSuppliersInfList(ServiceSupplierBiz.selectAll(null));
		supplierInfListVoRes.setData(supplierInfListVo);
		return supplierInfListVoRes;
	}
}
