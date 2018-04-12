package com.cupdata.sip.common.api.product.response;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/12
 */
@Data
public class OrgProductRelVo {

    /**
     *
     */
    private String orgNo;

    /**
     *
     */
    private String productNo;

    /**
     * 单位：人民币-分
     */
    private Integer orgPrice;
}
