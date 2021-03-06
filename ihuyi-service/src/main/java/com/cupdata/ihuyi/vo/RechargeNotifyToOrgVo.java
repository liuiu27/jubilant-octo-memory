package com.cupdata.ihuyi.vo;

import lombok.Data;

/**
 * 机构充值结果接收vo
 */
@Data
public class RechargeNotifyToOrgVo {

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 机构订单号
     */
    private String orgOrderNo;

    /**
     * 充值状态(1：充值中；F：充值失败；S：充值成功；)
     */
    private String rechargeStatus;

    /**
     * 时间戳
     */
    private String timestamp;
}
