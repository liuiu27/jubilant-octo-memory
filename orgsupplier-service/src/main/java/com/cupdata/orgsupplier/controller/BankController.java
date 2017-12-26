package com.cupdata.orgsupplier.controller;

import com.cupdata.commons.api.orgsupplier.IBankController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:56 2017/12/20
 */
@RestController
public class BankController implements IBankController{
    @Override
    public BaseResponse<OrgInfVo> findBankByBankCode(@PathVariable("bankCode") String bankCode) {
        return null;
    }
}
