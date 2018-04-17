package com.cupdata.content.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author junliang
 * @date 2018/04/17
 */

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    /**
     *
     * @param request
     * @param ex
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ContentException.class})
    public String handle(HttpServletRequest request, Exception ex ) {
        log.error(ex.getMessage(),ex.getCause());
        return null;
    }
}
