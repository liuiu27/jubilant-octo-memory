package com.cupdata.orgsupplier.rest;

import com.cupdata.orgsupplier.biz.BankInfBiz;
import com.cupdata.sip.common.api.orgsup.IBankController;
import com.cupdata.sip.common.api.orgsup.response.BankInfoVo;
import com.cupdata.sip.common.api.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@Slf4j
@RestController
public class BankController implements IBankController {

	@Autowired
	private BankInfBiz bankInfBiz;

    @Override
    public BaseResponse<BankInfoVo> findBankByBankCode(String bankCode) {

        BankInfoVo bankInfoVo = bankInfBiz.findBankByBankCode(bankCode);

        BaseResponse<BankInfoVo> bankInfoVoBaseResponse = new BaseResponse<>();

        bankInfoVoBaseResponse.setData(bankInfoVo);
        return bankInfoVoBaseResponse;
    }

    @Override
    public BaseResponse<List<BankInfoVo>> selectAll() {

        BaseResponse<List<BankInfoVo>> listBaseResponse = new BaseResponse<>();

        List<BankInfoVo> bankInfoVos = bankInfBiz.selectAll();

        listBaseResponse.setData(bankInfoVos);

        return listBaseResponse;
    }

}
