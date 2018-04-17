package com.cupdata.content.dto;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/17
 */
@Data
public class ContentTransactionLogDTO {

    /**
     * 交易流水编号
     */
    private String tranNo;

    /**
     * 商品编号
     */
    private String productNo;

    /**
     * 交易类型
     */
    private String tranType;

    /**
     * 交易描述
     */
    private String tranDesc;

    /**
     * 机构编号
     */
    private String orgNo;

    /**
     * 商户编号
     */
    private String supNo;

    /**
     * 请求信息
     */
    private String requestInfo;

}
