package com.cupdata.sip.common.lang.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author junliang
 * @date 2018/04/12
 */
public class BestdoException extends RuntimeException {

    @Getter
    @Setter
    private String code;

    @Setter
    @Getter
    private String message;

    public BestdoException(String errorCode,String errorMessage) {
        this.code=errorCode;
        this.message=errorMessage;
    }
}
