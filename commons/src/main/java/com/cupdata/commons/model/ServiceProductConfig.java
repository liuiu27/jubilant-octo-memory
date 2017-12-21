package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:服务产品配置表
 * @Date: 21:24 2017/12/20
 */
@Data
public class ServiceProductConfig extends BaseModel{
    /**
     *
     */
    private String supplierNo;

    /**
     *
     */
    private String configDesc;

    /**
     *
     */
    private String businessId;

    /**
     *
     */
    private String supplierApiUrl;

    /**
     *
     */
    private String md5SecretKey;

    /**
     *
     */
    private String rsaSupPubKey;

    /**
     *
     */
    private String rsaSipPubKey;

    /**
     *
     */
    private String rsaSipPriKey;
}
