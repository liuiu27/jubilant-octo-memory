package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.api.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: LinYong
 * @Description:机构、服务供应商服务接口
 * @Date: 13:11 2017/12/14
 */
@RequestMapping("supplier")
public interface ISupplierApi {
	  /**
     * 查询所有供应商信息
     */
    @GetMapping("/selectAll")
    BaseResponse selectAll();

    /**
     * 根据商户编号，查询商户信息
     * @return
     */
    @GetMapping("/findSupByNo/{supplierNo}")
    BaseResponse<SupplierInfVo> findSupByNo(@PathVariable("supplierNo") String supplierNo);

}
