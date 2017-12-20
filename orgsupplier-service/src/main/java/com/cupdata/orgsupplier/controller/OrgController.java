package com.cupdata.orgsupplier.controller;

import com.cupdata.commons.api.orgsupplier.IOrgController;
import com.cupdata.commons.api.orgsupplier.ISupplierController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:机构相关的controller
 * @Date: 14:50 2017/12/20
 */
public class OrgController implements IOrgController {
    @Override
    public BaseResponse<OrgInfVo> findOrgByNo(@PathVariable("orgNo") String orgNo) {
        return null;
    }
}
