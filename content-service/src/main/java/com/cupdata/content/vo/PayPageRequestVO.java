package com.cupdata.content.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class PayPageRequestVO {

    /**
     *时间戳
     */
    @NotBlank
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
}
