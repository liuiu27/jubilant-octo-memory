package com.cupdata.sip.bestdo.vo.response;

import lombok.Data;

/**
 * @author Tony
 * @date 2018/04/04
 */
@Data
public class BestaResVO<T> {

    String resCode;
    String resInfo;
    T data;

}
