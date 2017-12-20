package com.cupdata.commons.api.orgsupplier;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:55 2017/12/20
 */
public interface IBankController {
    /**
     * 根据银行号，查询银行信息
     * @return
     */
    @GetMapping("/bank/{bankCode}")
    public BaseResponse<OrgInfVo> findBankByBankCode(@PathVariable("bankCode") String bankCode);
}
