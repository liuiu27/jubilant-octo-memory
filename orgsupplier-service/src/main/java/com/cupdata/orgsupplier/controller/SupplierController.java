package com.cupdata.orgsupplier.controller;

import com.cupdata.commons.api.orgsupplier.ISupplierController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.model.ServiceSupplier;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.SupplierInfListVo;
import com.cupdata.commons.vo.orgsupplier.SupplierInfVo;
import com.cupdata.orgsupplier.biz.ServiceSupplierBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 13:56 2017/12/20
 */
@Slf4j
@RestController
public class SupplierController implements ISupplierController {

	@Autowired
	private ServiceSupplierBiz ServiceSupplierBiz;
	
	@Override
	public BaseResponse<SupplierInfListVo> selectAll() {
		log.info("SupplierController selectAll is begin");
		try {
			BaseResponse<SupplierInfListVo> supplierInfListVoRes = new BaseResponse<>();
			SupplierInfListVo supplierInfListVo = new SupplierInfListVo();
			supplierInfListVo.setSuppliersInfList(ServiceSupplierBiz.selectAll(null));
			supplierInfListVoRes.setData(supplierInfListVo);
			return supplierInfListVoRes;
		} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
	}

	@Override
	public BaseResponse<SupplierInfVo> findSupByNo(@PathVariable("supplierNo") String supplierNo) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("supplierNo", supplierNo);
		ServiceSupplier serviceSupplier = ServiceSupplierBiz.selectSingle(paramMap);

		SupplierInfVo  supplierInfVo = new SupplierInfVo();
		supplierInfVo.setSuppliersInf(serviceSupplier);

		BaseResponse<SupplierInfVo> supplierInfVoRes = new BaseResponse<>();
		supplierInfVoRes.setData(supplierInfVo);
		return supplierInfVoRes;
	}
}
