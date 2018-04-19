package com.cupdata.sip.common.api.voucher.response;

import lombok.Data;

import java.util.Date;

/**
 * @Author: DingCong
 * @Description: 券码详情列表
 * @CreateDate: 2018/2/27 19:44
 */
@Data
public class ElectronicVoucherLib {

    /**
     * 导入批次id
     */
    private Long batchId;
    /**
     * 商户id
     */
    private Long supplyerId;
    /**
     * 券码类别id
     */
    private Long categoryId;
    /**
     * 可发放机构编号
     */
    private String orgNos;

    /**
     * 发放券码编号
     */
    private String ticketNo;

    /**
     * startDate - 有效开始时间[格式：yyyyMMdd]
     */
    private String startDate;

    /**
     * endDate - 有效结束时间[格式：yyyyMMdd]
     */
    private String endDate;

    /**
     * 发放状态：已发放，未发放，回收
     */
    private String sendStatus;

    /**
     * 最终发放机构编号
     */
    private String orgNo;

    /**
     * 机构订单编号
     */
    private String orgOrderNo;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     * 发放日期
     */
    private Date sendDate;

    /**
     * 回收时间
     */
    private Date recoveryDate;
}
