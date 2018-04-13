package com.cupdata.sip.common.api;


import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:基础响应Vo
 * @Date: 16:26 2017/12/19
 */
@Data
public class BaseResponse<T> {

    private String responseCode ="000000";
    private String responseMsg = "SUCCESS!";

    private T data;

    public BaseResponse(){

    }

    public BaseResponse(T data){
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
