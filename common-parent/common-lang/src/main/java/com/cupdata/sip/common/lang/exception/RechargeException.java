package com.cupdata.sip.common.lang.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: DingCong
 * @Description: 充值服务运行时期自定义异常
 * @@Date: Created in 10:57 2018/4/18
 */
public class RechargeException extends RuntimeException{

    /**
     * 异常编码
     */
    @Getter
    @Setter
    private String code;

    /**
     * 异常信息
     */
    @Setter
    @Getter
    private String message;

    /**
     * 异常描述
     * @param errorCode
     * @param errorMessage
     */
    public RechargeException(String errorCode,String errorMessage) {
        this.code=errorCode;
        this.message=errorMessage;
    }

}
