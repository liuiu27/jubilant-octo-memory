package com.cupdata.sip.common.api.orgsup.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony
 * @date 2018/04/09
 */
@Getter
@Setter
public class SupplierInfVo {

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
