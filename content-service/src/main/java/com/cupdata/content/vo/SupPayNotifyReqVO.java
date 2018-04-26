package com.cupdata.content.vo;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/25
 */
@Data
public class SupPayNotifyReqVO {

    String timestamp;

    String resultCode;

    String supOrderNo;

    String orderAmt;

    String sipOrderNo;

}
