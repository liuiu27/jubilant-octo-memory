package com.cupdata.commons.api.orgsupplier;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:机构、服务供应商服务接口
 * @Date: 13:11 2017/12/14
 */
public interface IOrgController {
    /**
     * 根据机构编号，查询机构信息
     * @return
     */
    @GetMapping("/org/{orgNo}")
    public BaseResponse<OrgInfVo> findOrgByNo(@PathVariable("orgNo") String orgNo);

}
