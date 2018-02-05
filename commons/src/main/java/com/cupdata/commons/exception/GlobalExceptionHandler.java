package com.cupdata.commons.exception;

import com.cupdata.commons.vo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {Exception.class})
    public BaseResponse handle(HttpServletRequest request, Exception ex) {

        log.error(ex.getLocalizedMessage());
        return new BaseResponse();
    }

    @ExceptionHandler(value = {BizException.class})
    public BaseResponse handle(HttpServletRequest request, BizException ex) throws Exception {

        log.error(ex.getErrorCode());
        log.error(ex.getErrorMessage());
        return new BaseResponse();
    }


}
