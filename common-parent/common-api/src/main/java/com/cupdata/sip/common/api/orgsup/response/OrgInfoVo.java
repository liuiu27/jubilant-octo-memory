package com.cupdata.sip.common.api.orgsup.response;

import lombok.Data;

/**
 * @author Tony
 * @date 2018/04/11
 */
@Data
public class OrgInfoVo {
    /**
     * 机构编号
     */
    private String orgNo;

    /**
     *
     */
    private String orgName;

    /**
     *
     */
    private String orgDesc;

    /**
     *
     */
    private String address;

    /**
     *
     */
    private String contacts;

    /**
     *
     */
    private String mobileNo;

    /**
     *
     */
    private String orgType;

    /**
     *
     */
    private String bankCode;

    /**
     *
     */
    private String orgPubKey;

    /**
     *
     */
    private String sipPubKey;

    /**
     *
     */
    private String sipPriKey;

    /**
     *
     */
    private String whiteIps;

    /**
     *有效状态
     * 0：无效；1：有效。
     */
    private Character validStatus;


}
