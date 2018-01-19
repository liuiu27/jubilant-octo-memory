package com.cupdata.orgsupplier.controller;

import java.util.List;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.SupplierInfListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.orgsupplier.ISupplierController;
import com.cupdata.commons.model.ServiceSupplier;
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
		supplierInfListVoRes.getData().setSuppliersInfList(ServiceSupplierBiz.selectAll(null));
		return supplierInfListVoRes;
	}
}
