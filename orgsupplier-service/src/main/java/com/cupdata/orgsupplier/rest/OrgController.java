package com.cupdata.orgsupplier.rest;

import com.cupdata.orgsupplier.biz.OrgInfBiz;
import com.cupdata.sip.common.api.orgsup.IOrgController;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.lang.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class OrgController implements IOrgController {

    @Autowired
    private OrgInfBiz orgInfBiz;


    @Override
    public BaseResponse<OrgInfoVo> findOrgByNo(@PathVariable("orgNo") String orgNo) {

        OrgInfoVo orgInfoVo = orgInfBiz.findOrgByNo(orgNo);

        BaseResponse<OrgInfoVo> orgInfoVoBaseResponse = new BaseResponse<>();

        orgInfoVoBaseResponse.setData(orgInfoVo);

        return orgInfoVoBaseResponse;
    }

    @Override
    public BaseResponse<List<OrgInfoVo>> selectAll() {

        List<OrgInfoVo> orgInfoVos = orgInfBiz.selectAll();

        BaseResponse<List<OrgInfoVo>> listBaseResponse = new BaseResponse<>();

        listBaseResponse.setData(orgInfoVos);

        return listBaseResponse;
    }
}
