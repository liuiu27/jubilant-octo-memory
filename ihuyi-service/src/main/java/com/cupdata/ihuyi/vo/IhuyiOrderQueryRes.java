package com.cupdata.ihuyi.vo;

import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 互亿虚拟充值订单查询结果vo
 * @CreateDate: 2018/3/12 19:25
 */

@Data
public class IhuyiOrderQueryRes {
    /**
     * 返回代码
     */
    private int code;

    /**
     * 客户订单id
     */
    private String orderid;

    /**
     * 任务 id
     */
    private String taskid;

    /**
     * 充值状态
     * 0：已收单
     * 1：充值中
     * 2：充值成功
     * 3：充值失败
     * -1：未知状态
     */
    private int status;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 套餐大小(话费：元，流量：M)
     */
    private int size;

    /**
     * 提交时间
     */
    private String submit_time;

    /**
     * 最后处理时间
     */
    private String result_time;

}
