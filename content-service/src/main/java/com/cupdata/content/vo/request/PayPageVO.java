package com.cupdata.content.vo.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class PayPageVO {

    /**
     *时间戳
     */
    @NotBlank(message="")
    String timestamp;
    /**
     *订单金额
     */
    @NotBlank
    String orderAmt;
    /**
     *供应商订单号
     */
    @NotBlank
    String supOrderNo;
    /**
     *供应商订单时间
     */
    @NotBlank
    String supOrderTime;
    /**
     *商品名称
     */
    @NotBlank
    String productName;
    /**
     *订单信息
     */
    @NotBlank
    String orderInfo;
    /**
     *支付超时时间
     */
    @NotBlank
    String timeOut;

    /**
     *平台支付完成回跳地址
     */
    @NotBlank
    String payBackUrl;

    /**
     * 支付通知地址
     */
    @NotBlank
    String notifyUrl;

    /**
     * 商品数量
     */
    @NotBlank
    String productNum;

    /**
     * 订单页面需展示信息
     */
    String orderShow;

    /**
     * 交易流水号
     */
    String sipTranNo;

    /**
     * 订单标题
     */
    String orderTitle;
}
