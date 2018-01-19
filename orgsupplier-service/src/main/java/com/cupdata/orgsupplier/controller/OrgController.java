package com.cupdata.orgsupplier.controller;

import java.util.List;

import com.cupdata.commons.vo.orgsupplier.OrgInfListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.orgsupplier.IOrgController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.orgsupplier.biz.OrgInfBiz;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:机构相关的controller
 * @Date: 14:50 2017/12/20
 */
@Slf4j
@RestController
public class OrgController implements IOrgController {
	@Autowired OrgInfBiz orgInfBiz;
	
    @Override
    public BaseResponse<OrgInfVo> findOrgByNo(@PathVariable("orgNo") String orgNo) {
    	log.info("OrgController findOrgByNo is begin ,param  orgNo is" + orgNo);
    	return orgInfBiz.findOrgByNo(orgNo);
    }

	@Override
	public BaseResponse<OrgInfListVo> selectAll() {
		BaseResponse<OrgInfListVo> orgInfListVoRes = new BaseResponse();
		orgInfListVoRes.getData().setOrgInfList(orgInfBiz.selectAll(null));
		return orgInfListVoRes;
	}
}
