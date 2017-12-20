package com.cupdata.commons.api.orgsupplier;

import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:机构、服务供应商服务接口
 * @Date: 13:11 2017/12/14
 */
public interface IOrgSupplierController {
    /**
     *根据服务产品id，查询产品信息
     * @return
     */
    @GetMapping("/org/{orgNo}")
    public BaseResponse<OrgInfVo> findOrgByNo(@PathVariable("orgNo") String orgNo);


}
