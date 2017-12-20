package com.cupdata.commons.model;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 22:48 2017/12/19
 */
@Data
public class OrgInf extends BaseModel{
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
