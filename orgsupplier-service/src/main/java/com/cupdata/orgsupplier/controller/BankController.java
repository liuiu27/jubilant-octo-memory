package com.cupdata.orgsupplier.controller;

import com.cupdata.commons.api.orgsupplier.IBankController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.BankInfVo;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.orgsupplier.biz.BankInfBiz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:56 2017/12/20
 */
@RestController
public class BankController implements IBankController{
	
	@Autowired
	private BankInfBiz bankInfBiz;
	
    @Override
    public BaseResponse<BankInfVo> findBankByBankCode(@PathVariable("bankCode") String bankCode) {
        return null;
    }

	@Override
	public List<BankInfVo> selectAll() {
		return bankInfBiz.selectAll(null);
	}
}
