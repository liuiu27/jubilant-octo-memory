package com.cupdata.content.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView handle(HttpServletRequest request, ContentException ex ) {
        ModelAndView modelAndView =new ModelAndView();
        log.error(ex.getMessage(),ex.getCause());
        modelAndView.addObject("code",ex.getCode());
        modelAndView.addObject("tips",ex.getTips());
        modelAndView.setViewName(ex.getPage());
        return modelAndView;
    }

    /**
     *
     * @param request
     * @param ex
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public ModelAndView handle(HttpServletRequest request, Exception ex ) {
        ModelAndView modelAndView =new ModelAndView();
        modelAndView.setViewName("default_error");
        return modelAndView;
    }
}
