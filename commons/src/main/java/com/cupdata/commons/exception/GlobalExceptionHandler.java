package com.cupdata.commons.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cupdata.commons.vo.BaseResponse;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年2月2日 下午5:48:32
*/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	
	
	
	@ExceptionHandler(value=Exception.class)  
	public BaseResponse ExceptionHandler(HttpServletRequest request,  
			Exception exception) throws Exception   {  
		log.error("wocojfioas");
		return new BaseResponse("200","系统错误");
		
	}
	 @ExceptionHandler(value=ErrorException.class)  
	 public BaseResponse ErrorExceptionHandler(HttpServletRequest request,  
			 ErrorException exception) throws Exception   {  
		 log.error("wocojfioas");
		 return new BaseResponse(exception.getErrorCode(),exception.getMessage());
		 
	    }
	 
	 
	
}
