package com.cupdata.ihuyi.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 10:43 2018/4/20
 */
@Data
public class OrderNotifyCompleteVo {

    private String orderNo;

    private String notifyUrl;

    private Date completeDate;

    private int notifyTimes;

    private String notifyStatus;

    private String nodeName;


}
