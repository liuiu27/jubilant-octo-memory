package com.cupdata.sip.common.api.order.response;

import lombok.Data;

/**
 * @author 作者: liwei
 * @createDate 创建时间：2018年4月19日 下午2:37:24
 */
@Data
public class OrderContentVo {

    /**
     * 主订单id
     */
    private Long orderId;

    /**
     * 产品编号
     */
    private String productNo;

    /**
     * 机构编号
     */
    private String orgNo;

    /**
     * 商户编号
     */
    private String supNo;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 渠道用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 订单时间
     */
    private String orderTime;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 订单详细
     */
    private String orderInfo;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单金额
     */
    private Integer orderAmt;

    /**
     * 订单积分
     */
    private Integer orderBonus;

    /**
     * 实际支付积分
     */
    private Integer payAmt;

    /**
     * 实际支付积分
     */
    private Integer payBonus;

    /**
     * 实际支付权益
     */
    private Integer payRight;

    /**
     * 支付成功通知
     */
    private String payNotifyUrl;

    /**
     * 主订单信息
     */
    private OrderInfoVo orderInfoVo = new OrderInfoVo();

}
