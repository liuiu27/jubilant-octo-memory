package com.cupdata.sip.common.lang.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: DingCong
 * @Description:
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
     * 券码异长描述
     * @param errorCode
     * @param errorMessage
     */
    public VoucherException(String errorCode,String errorMessage) {
        this.code=errorCode;
        this.message=errorMessage;
    }
}
