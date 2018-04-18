package com.cupdata.sip.common.lang.exception;

import com.alibaba.fastjson.JSONException;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import lombok.Data;
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


    /**
     * 校验参数异常
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse<List<Map>> MethodArgumentNotValidHandler(HttpServletRequest request,
                                                                                   MethodArgumentNotValidException exception) {
        List<Map> invalids = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            Map<String,Object> invalid = new HashMap(3);
            invalid.put("Message",error.getDefaultMessage());
            invalid.put("Field",error.getField());
            invalid.put("RejectedValue",error.getRejectedValue());
            invalids.add(invalid);
        }
        log.error(exception.getMessage());

        return new BaseResponse(ResponseCodeMsg.PARAM_INVALID.getCode(),ResponseCodeMsg.PARAM_INVALID.getMsg(),invalids);
    }

    /**
     * JSON 解析错误
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = JSONException.class)
    public BaseResponse JSONExceptionHandler(HttpServletRequest request,JSONException exception) {

        log.error(exception.getMessage(),exception.getCause());
        return new BaseResponse(ResponseCodeMsg.PARAM_INVALID.getCode(),ResponseCodeMsg.PARAM_INVALID.getMsg());
    }

    /**
     * 未捕获的运行时异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public BaseResponse handle(HttpServletRequest request, RuntimeException ex ) {
        log.error(ex.getMessage(),ex.getCause());
        return new BaseResponse(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
    }

    /**
     * 异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public BaseResponse handle(HttpServletRequest request, Exception ex ) {
        log.error(ex.getMessage(),ex.getCause());
        return new BaseResponse(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
    }

    @ExceptionHandler(value = BestdoException.class)
    public BaseResponse exceptionHandler(HttpServletRequest request,BestdoException exception) {
        return new BaseResponse(exception.getCode(),exception.getMessage());
    }

    /**
     * 券码异常捕获
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = VoucherException.class)
    public BaseResponse voucherExceptionHandler(HttpServletRequest request,VoucherException exception) {
        log.error(exception.getMessage(),exception.getCause());
        return new BaseResponse(exception.getCode(),exception.getMessage());
    }


    /**
     * 充值异常捕获
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = RechargeException.class)
    public BaseResponse rechargeExceptionHandle(HttpServletRequest request,RechargeException exception){
        log.error(exception.getMessage(),exception.getCause());
        return new BaseResponse(exception.getCode(),exception.getMessage());
    }




    @Data
    class BaseResponse<T>{

        private String responseCode;

        private String responseMsg;

        private T data;

        public BaseResponse(){
            responseCode = ResponseCodeMsg.SUCCESS.getCode();
            responseMsg = ResponseCodeMsg.SUCCESS.getMsg();
        }

        public BaseResponse(T data){
            responseCode = ResponseCodeMsg.SUCCESS.getCode();
            responseMsg = ResponseCodeMsg.SUCCESS.getMsg();
            this.data = data;
        }

        public BaseResponse(String responseCode, String responseMsg){
            this.responseCode =responseCode;
            this.responseMsg = responseMsg;
        }
        public BaseResponse(String responseCode, String responseMsg, T data){
            this.responseCode =responseCode;
            this.responseMsg = responseMsg;
            this.data =data;
        }


    }

}
