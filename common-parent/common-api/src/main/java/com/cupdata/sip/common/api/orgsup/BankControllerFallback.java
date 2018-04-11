package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.BankInfoVo;

import java.util.List;

/**
 * @author junliang
 * @date 2018/04/11
 */
public class BankControllerFallback implements IBankController {

    @Override
    public BaseResponse<BankInfoVo> findBankByBankCode(String bankCode) {
        return null;
    }

    @Override
    public BaseResponse<List<BankInfoVo>> selectAll() {
        return null;
    }
}
