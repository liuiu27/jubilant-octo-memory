package com.cupdata.orgsupplier.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.orgsupplier.dao.OrgInfDao;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */
@Slf4j
@Service
public class OrgInfBiz extends BaseBiz<OrgInf> {
    @Autowired
    private OrgInfDao orgInfDao;


    @Override
    public BaseDao<OrgInf> getBaseDao() {
        return orgInfDao;
    }
    
	public BaseResponse<OrgInfVo> findOrgByNo(String orgNo) {
		BaseResponse<OrgInfVo> BaseResponse =  new BaseResponse<OrgInfVo>();
    	OrgInf orgInf = orgInfDao.findOrgByNo(orgNo);
    	if(null != orgInf) {
    		OrgInfVo orgInfVo = new OrgInfVo();
    		orgInfVo.setOrgInf(orgInf);
    		BaseResponse.setData(orgInfVo);
    		BaseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
    		BaseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
    	}else {
    		log.error("findOrgByNo orgNo = " + orgNo + " result is null");
    	}
		return BaseResponse;
	}

}
