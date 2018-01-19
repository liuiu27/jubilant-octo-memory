package com.cupdata.commons.vo.orgsupplier;

import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.vo.BaseData;
import lombok.Data;

import java.util.List;

/**
 * @author LinYong
 * @Description: 机构信息列表vo
 * @create 2018-01-16 20:29
 */
@Data
public class OrgInfListVo extends BaseData {
    /**
     *
     */
    private List<OrgInf> orgInfList;
}
