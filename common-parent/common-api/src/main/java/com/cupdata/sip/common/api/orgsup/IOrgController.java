package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.api.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:机构、服务供应商服务接口
 * @Date: 13:11 2017/12/14
 */
@RequestMapping("/org")
public interface IOrgController {
    /**
     * 根据机构编号，查询机构信息
     * @return
     */
	@GetMapping("/findOrgByNo/{orgNo}")
	BaseResponse<OrgInfoVo> findOrgByNo(@PathVariable("orgNo") String orgNo);
    
	/**
	 *  查询所有机构信息
	 * @return
	 */
    @GetMapping("/selectAll")
    BaseResponse<List<OrgInfoVo>> selectAll();

}