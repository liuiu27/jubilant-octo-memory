package com.cupdata.ihuyi.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 10:47 2018/4/20
 */
@Data
public class OrderNotifyWaitVo {

    private String orderNo;

    private String notifyUrl;

    private Date nextNotifyDate;

    private int notifyTimes;

    private String notifyStatus;

    private String nodeName;

}
