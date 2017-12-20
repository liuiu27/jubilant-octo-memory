package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:银行信息实体类
 * @Date: 14:58 2017/12/20
 */
@Data
public class BankInf extends BaseModel{
    /**
     * 银行编号
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;
}
