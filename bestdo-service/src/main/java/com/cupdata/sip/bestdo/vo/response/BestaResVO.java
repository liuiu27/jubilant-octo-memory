package com.cupdata.sip.bestdo.vo.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony
 * @date 2018/04/04
 */
@Getter
@Setter
public class BestaResVO<T> {

    @JSONField(serialize=false)
    transient String resCode;

    @JSONField(serialize=false)
    transient String resInfo;

    T data;

}
