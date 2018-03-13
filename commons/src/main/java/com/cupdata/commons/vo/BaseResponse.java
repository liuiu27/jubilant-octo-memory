package com.cupdata.commons.vo;


import com.cupdata.commons.constant.ResponseCodeMsg;
import lombok.Data;
/**
 * @Auth: LinYong
 * @Description:基础响应Vo
 * @Date: 16:26 2017/12/19
 */
@Data
public class BaseResponse<T> {

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
    
    public  BaseResponse(String responseCode,String responseMsg){
        responseCode =responseCode;
        responseMsg = responseMsg;
    }
    public  BaseResponse(String responseCode,String responseMsg,T data){
        this.responseCode =responseCode;
        this.responseMsg = responseMsg;
        this.data =data;
    }

}
