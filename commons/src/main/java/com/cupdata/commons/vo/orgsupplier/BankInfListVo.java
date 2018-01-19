package com.cupdata.commons.vo.orgsupplier;

import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.vo.BaseData;
import lombok.Data;

import java.util.List;

/**
 * @author LinYong
 * @Description:
 * @create 2018-01-16 20:29
 */
@Data
public class BankInfListVo extends BaseData {
    /**
     *
     */
    private List<BankInf> bankInfList;
}
