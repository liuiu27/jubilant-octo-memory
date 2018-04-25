package com.cupdata.content.vo.request;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/25
 */
@Data
public class OrgPayNotifyVO {

    String timestamp;

    String resultCode;

    String sipOrderNo;

    String orderAmt;

    String orgOrderNo;

    String payTime;

}
