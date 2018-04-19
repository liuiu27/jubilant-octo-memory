package com.cupdata.sip.common.lang.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: DingCong
 * @Description: 券码相关服务运行时期自定义异常
 * @@Date: Created in 14:10 2018/4/17
 */
public class VoucherException extends RuntimeException{

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
    public VoucherException(String errorCode,String errorMessage) {
        this.code=errorCode;
        this.message=errorMessage;
    }
}
