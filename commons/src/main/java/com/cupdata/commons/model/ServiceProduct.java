package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:服务产品表
 * @Date: 16:58 2017/12/14
 */
@Data
public class ServiceProduct extends BaseModel {

    /**
     *服务产品类型
     * RECHARGE：充值；VOUCHER：券码；
     */
    private String productType;

    /**
     * 服务产品子类型
     */
    private String productSubType;

    /**
     *供应商编号
     */
    private String supplierNo;

    /**
     *服务产品编号
     */
    private String productNo;

    /**
     *服务产品名称
     */
    private String productName;

    /**
     *服务产品描述
     */
    private String productDesc;

    /**
     *供应商价格
     * 单位：人民币-分
     */
    private Long supplierPrice;

    /**
     *充值开通时长
     * 针对充值产品；单位：月
     */
    private Long rechargeDuration;

    /**
     *充值金额
     * 针对充值产品；单位：元
     */
    private Long rechargeAmt;

    /**
     *充值流量
     * 针对充值产品业务（流量充值）；单位：M兆
     */
    private Long rechargeTraffic;

    /**
     *充值数量
     * 针对充值产品业务；单位：个，仅在一些特殊服务产品中使用
     */
    private Long rechargeNumber;

    /**
     *供应商参数
     */
    private String supplierParam;

    /**
     * 服务应用路径
     */
    private String serviceApplicationPath;
}