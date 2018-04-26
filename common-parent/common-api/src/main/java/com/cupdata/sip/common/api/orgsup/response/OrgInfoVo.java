package com.cupdata.sip.common.api.orgsup.response;

import lombok.Data;

/**
 * @author Tony
 * @date 2018/04/11
 */
@Data
public class OrgInfoVo {
    /**
     * 机构号
     */
    private String orgNo;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构描述
     */
    private String orgDesc;

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
     * CUPD:银数机构；BANK：银行机构
     */
    private String orgType;

    /**
     * 只有当ORG_TYPE为BANK时，此字段非空表示该机构所属的银行
     */
    private String bankCode;

    /**
     * 机构公钥
     */
    private String orgPubKey;

    /**
     * 平台公钥
     */
    private String sipPubKey;

    /**
     * 平台私钥
     */
    private String sipPriKey;

    /**
     * IP白名单
     */
    private String whiteIps;

    /**
     * 0：无效；1：有效。
     */
    private String validStatus;

    /**
     * 内容引入机构退款地址
     */
    private String contentOrderRefundUrl;

    /**
     * 内容引入机构订单查询地址
     */
    private String contentOrderQueryUrl;

}
