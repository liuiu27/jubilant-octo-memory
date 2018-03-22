package com.cupdata.commons.exception;


import com.alibaba.fastjson.JSONException;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse<List<Map>> MethodArgumentNotValidHandler(HttpServletRequest request,
                                                                                   MethodArgumentNotValidException exception) {
        List<Map> invalids = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            Map invalid = new HashMap(3);
            invalid.put("Message",error.getDefaultMessage());
            invalid.put("Field",error.getField());
            invalid.put("RejectedValue",error.getRejectedValue());
            invalids.add(invalid);
        }

        return new BaseResponse(ResponseCodeMsg.PARAM_INVALID.getCode(),ResponseCodeMsg.PARAM_INVALID.getMsg(),invalids);
    }
    @ExceptionHandler(value = JSONException.class)
    public BaseResponse<?> JSONExceptionHandler(HttpServletRequest request,JSONException exception) {
        log.error(exception.getLocalizedMessage());
        return new BaseResponse(ResponseCodeMsg.PARAM_INVALID.getCode(),ResponseCodeMsg.PARAM_INVALID.getMsg(),exception.getMessage());
    }


    @ExceptionHandler(value = {Exception.class})
    public BaseResponse handle(HttpServletRequest request, Exception ex) {

        log.error(ex.getLocalizedMessage());
        return new BaseResponse(ResponseCodeMsg.PARAM_INVALID.getCode(),ResponseCodeMsg.PARAM_INVALID.getMsg(),ex.getMessage());
    }

    @ExceptionHandler(value = {BizException.class})
    public BaseResponse handle(HttpServletRequest request, BizException ex) {

        log.error(ex.getErrorCode());
        log.error(ex.getErrorMessage());
        return new BaseResponse();
    }




}
