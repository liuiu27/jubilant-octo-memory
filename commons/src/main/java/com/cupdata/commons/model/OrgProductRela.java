package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description: 机构与服务产品关系表
 * @Date: 21:31 2017/12/20
 */
@Data
public class OrgProductRela extends BaseModel{
    /**
     *
     */
    private String orgNo;

    /**
     *
     */
    private String productNo;

    /**
     *
     */
    private Long orgPrice;
}
