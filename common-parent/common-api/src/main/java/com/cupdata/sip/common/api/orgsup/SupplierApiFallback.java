package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;

import java.util.List;

/**
 * @author Tony
 * @date 2018/04/09
 */
public class SupplierApiFallback implements ISupplierApi {


    @Override
    public BaseResponse<List<SupplierInfVo>> selectAll() {
        return null;
    }

    @Override
    public BaseResponse<SupplierInfVo> findSupByNo(String supplierNo) {
        return null;
    }
}
