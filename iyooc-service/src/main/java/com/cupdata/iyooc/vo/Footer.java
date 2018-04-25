package com.cupdata.iyooc.vo;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 14:05 2018/4/24
 */
@Data
public class Footer {

    /**
     * 响应状态码  000000：成功,其它响应状态码均为不成功的状态，在响应描述信息中会有相关提示
     */
    private String respCode;

    /**
     * 响应描述信息
     */
    private String respMessage;

    /**
     * 响应状态	接口业务处理正常为true，异常为false
     */
    private boolean respStatus;
}
