package com.cupdata.content.vo;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/24
 */

@Data
public class OrgPayVO {

    String timestamp;
    String orderAmt;
    String sipOrderNo;
    String sipOrderTime;
    String orderTitle;
    String orderInfo;
    String timeOut;
    String payBackUrl;
    String productNum;
    String orderShow;
    String userId;
    String userName;

    public OrgPayVO(){

    }
}
