package com.cupdata.commons.exception;

import lombok.Getter;
import lombok.Setter;

public class BizException extends RuntimeException {

    @Getter
    @Setter
    private String errorCode;

    @Setter
    @Getter
    private String errorMessage;

    public BizException(String errorCode,String errorMessage) {
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
    }

}
