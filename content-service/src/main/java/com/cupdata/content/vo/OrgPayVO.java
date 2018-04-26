package com.cupdata.content.vo;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/24
 */

@Data
public class OrgPayVO {

    private String timestamp;
    private String orderAmt;
    private String sipOrderNo;
    private String sipOrderTime;
    private String orderTitle;
    private String orderInfo;
    private String timeOut;
    private String payBackUrl;
    private String productNum;
    private String orderShow;
    private String userId;
    private String userName;
    private String productNo;

    public OrgPayVO(){

    }
}
