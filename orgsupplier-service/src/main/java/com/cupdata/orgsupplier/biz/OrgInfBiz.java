package com.cupdata.orgsupplier.biz;

import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.dao.entity.OrgInf;
import com.cupdata.sip.common.dao.mapper.OrgInfMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junliang
 * @date 2018/04/11
 */
@Slf4j
@Service
public class OrgInfBiz {

    @Autowired
    private OrgInfMapper orgInfMapper;


    public OrgInfoVo findOrgByNo(String orgNo) {

        OrgInf org = orgInfMapper.findOrgByNo(orgNo);
        OrgInfoVo orgInfoVo =new OrgInfoVo();
        BeanCopierUtils.copyProperties(org,orgInfoVo);

        return orgInfoVo;

    }

    public List<OrgInfoVo> selectAll(){

        List<OrgInf> orgInfs = orgInfMapper.selectAll();

        List<OrgInfoVo> orgInfoVos = new ArrayList<>(orgInfs.size());

        orgInfs.forEach(orgInf -> {
            OrgInfoVo orgInfoVo =new OrgInfoVo();
            BeanCopierUtils.copyProperties(orgInf,orgInfoVo);
            orgInfoVos.add(orgInfoVo);
        });

        return orgInfoVos;

    }
}