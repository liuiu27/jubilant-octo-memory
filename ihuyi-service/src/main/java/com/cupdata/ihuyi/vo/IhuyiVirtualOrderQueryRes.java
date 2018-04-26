package com.cupdata.ihuyi.vo;

import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 互亿虚拟充值订单查询结果vo
 * @CreateDate: 2018/3/14 15:31
 */
@Data
public class IhuyiVirtualOrderQueryRes {

    /**
     * 代码
     */
    private int code;

    /**
     * 商家订单ID(花积分订单id)
     */
    private String orderid;

    /**
     * 系统任务id(互亿订单id)
     */
    private String taskid;

    /**
     * 产品ID
     */
    private String productid;
    /**
     * 充值状态0：已收单 1：充值中 2：充值成功 3：充值失败 -1：未知状态
     */
    private int status;

    /**
     * 充值账号
     */
    private String account;

    /**
     * 购买数量
     */
    private int quantity;

    /**
     * 扩展参数
     */
    private String extend;

    /**
     * 商家回调url
     */
    private String callback;

    /**
     * 商家⾃定义参数，回调回传
     */
    private String return_;

    /**
     * 买家真实ip
     */
    private String buyerip;

    /**
     * 订单金额
     */
    private float money;

    /**
     * 订单时间
     */
    private int submit_time;
}
