package com.cupdata.commons.vo;


import lombok.Data;
/**
 * @Auth: LinYong
 * @Description:基础响应Vo
 * @Date: 16:26 2017/12/19
 */
@Data
public class BaseResponse<T extends BaseData> {

    private String responseCode;

    private String responseMsg;

    private T data;

    BaseResponse(T data){
        responseCode = "000000";
        responseMsg = "success";
        this.data = data;
    }

}
