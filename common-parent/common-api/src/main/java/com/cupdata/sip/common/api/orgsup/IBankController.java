package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.orgsup.response.BankInfoVo;
import com.cupdata.sip.common.lang.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author junliang
 * @date 2018/04/11
 */
@RequestMapping("/bank")
public interface IBankController {

    /**
     * 根据银行号，查询银行信息
     * @return
     */
    @GetMapping("/findBankByBankCode/{bankCode}")
    BaseResponse<BankInfoVo> findBankByBankCode(@PathVariable("bankCode") String bankCode);

    /**
     * 查询所有银行信息
     */
    @GetMapping("/selectAll")
    BaseResponse<List<BankInfoVo>> selectAll();

}
