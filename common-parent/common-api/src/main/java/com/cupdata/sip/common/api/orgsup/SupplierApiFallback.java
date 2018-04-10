package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.BaseResponse;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;

import java.util.List;

/**
 * @author Tony
 * @date 2018/04/09
 */
public class SupplierApiFallback implements ISupplierApi {


    @Override
    public BaseResponse<List<SupplierInfVo>> selectAll() {
        return new BaseResponse(ResponseCodeMsg.FAIL.getCode(),ResponseCodeMsg.FAIL.getMsg());
    }

    @Override
    public BaseResponse<SupplierInfVo> findSupByNo(String supplierNo) {
        return new BaseResponse(ResponseCodeMsg.FAIL.getCode(),ResponseCodeMsg.FAIL.getMsg());
    }
}
