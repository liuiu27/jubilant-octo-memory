package com.cupdata.commons.api.orgsupplier;

import java.util.List;

import com.cupdata.commons.vo.orgsupplier.BankInfListVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.BankInfVo;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:55 2017/12/20
 */
@RequestMapping("/bank")
public interface IBankController {
    /**
     * 根据银行号，查询银行信息
     * @return
     */
    @GetMapping("/findBankByBankCode/{bankCode}")
    public BaseResponse<BankInfVo> findBankByBankCode(@PathVariable("bankCode") String bankCode);
    
    /**
     * 查询所有银行信息
     */
    @GetMapping("/selectAll")
    public BaseResponse<BankInfListVo> selectAll();
}
