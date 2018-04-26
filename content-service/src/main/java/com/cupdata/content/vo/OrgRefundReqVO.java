package com.cupdata.content.vo;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/25
 */
@Data
public class OrgRefundReqVO {

    String timestamp;

    String sipOrderNo;

    String refundDate;

    String refundAmt;

    String refundInfo;

}
