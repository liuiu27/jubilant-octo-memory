package com.cupdata.sip.common.api.orgsup;

import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.lang.BaseResponse;

import java.util.List;

/**
 * @author Tony
 * @date 2018/04/11
 */
public class OrgControllerFallback implements IOrgController {
    @Override
    public BaseResponse<OrgInfoVo> findOrgByNo(String orgNo) {
        return null;
    }

    @Override
    public BaseResponse<List<OrgInfoVo>> selectAll() {
        return null;
    }
}
