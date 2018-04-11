package com.cupdata.orgsupplier.rest;

import com.cupdata.orgsupplier.biz.ServiceSupplierBiz;
import com.cupdata.sip.common.api.orgsup.ISupplierApi;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 13:56 2017/12/20
 */
@Slf4j
@RestController
public class SupplierController implements ISupplierApi {

	@Autowired
	private ServiceSupplierBiz ServiceSupplierBiz;

	@Override
	public BaseResponse<List<SupplierInfVo>> selectAll() {
		log.info("SupplierController selectAll is begin");

		BaseResponse<List<SupplierInfVo>> supplierInfListVoRes = new BaseResponse<>();
		List<SupplierInfVo> supplierInfVos = ServiceSupplierBiz.selectAll();
		supplierInfListVoRes.setData(supplierInfVos);
		return supplierInfListVoRes;

	}

	@Override
	public BaseResponse<SupplierInfVo> findSupByNo(@PathVariable("supplierNo") String supplierNo) {

		log.info("query supplierNo ,"+ supplierNo);

		BaseResponse<SupplierInfVo> supplierInfVoBaseResponse =new BaseResponse<>();
		SupplierInfVo supplierInfVo =ServiceSupplierBiz.findSupByNo(supplierNo);
		supplierInfVoBaseResponse.setData(supplierInfVo);
		return supplierInfVoBaseResponse;
	}

}
