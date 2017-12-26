package com.cupdata.orgsupplier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.orgsupplier.IOrgController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.orgsupplier.dao.OrgInfDao;

/**
 * @Auth: LinYong
 * @Description:机构相关的controller
 * @Date: 14:50 2017/12/20
 */
@RestController
public class OrgController implements IOrgController {
	@Autowired OrgInfDao orgInfDao;
	
    @Override
    public BaseResponse<OrgInfVo> findOrgByNo(@PathVariable("orgNo") String orgNo) {
    	BaseResponse<OrgInfVo> BaseResponse =  new BaseResponse<OrgInfVo>();
    	OrgInf orgInf = orgInfDao.findOrgByNo(orgNo);
    	OrgInfVo orgInfVo = new OrgInfVo();
    	orgInfVo.setOrgInf(orgInf);
    	if(null != orgInfVo) {
    		BaseResponse.setData(orgInfVo);
    		BaseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
    		BaseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
    	}
		return BaseResponse;
    }
}
