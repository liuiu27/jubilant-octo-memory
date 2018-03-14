package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 14:03 2017/12/20
 */
@Data
public class ServiceSupplier extends BaseModel{
    /**
     * 供应商编号
     */
    private String supplierNo;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商描述
     */
    private String supplierDesc;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 供应商类型
     */
    private String supplierType;

    /**
     * 供应商公钥字符串
     */
    private String supplierPubKey;

    /**
     * 平台公钥字符串
     */
    private String sipPubKey;

    /**
     * 平台私钥字符串
     */
    private String sipPriKey;

    /**
     * 供应商标识
     */
    private String supplierFlag;
}
