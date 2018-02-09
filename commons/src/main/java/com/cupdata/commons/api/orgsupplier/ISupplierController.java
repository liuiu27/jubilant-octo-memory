package com.cupdata.commons.api.orgsupplier;

import java.util.List;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfListVo;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.orgsupplier.SupplierInfListVo;
import com.cupdata.commons.vo.orgsupplier.SupplierInfVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.model.ServiceSupplier;

/**
 * @Auth: LinYong
 * @Description:机构、服务供应商服务接口
 * @Date: 13:11 2017/12/14
 */
@RequestMapping("/supplier")
public interface ISupplierController {
	  /**
     * 查询所有供应商信息
     */
    @GetMapping("/selectAll")
    public BaseResponse<SupplierInfListVo> selectAll();

    /**
     * 根据商户编号，查询商户信息
     * @return
     */
    @GetMapping("/findSupByNo/{supplierNo}")
    public BaseResponse<SupplierInfVo> findSupByNo(@PathVariable("supplierNo") String supplierNo);

}
