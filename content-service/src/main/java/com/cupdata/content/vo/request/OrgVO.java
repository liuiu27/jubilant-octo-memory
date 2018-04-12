package com.cupdata.content.vo.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrgVO<T> {

    /**
     * 机构标识
     */
    String org;
    /**
     * 签名
     */
    String sign;
    /**
     * 交易流水
     */
    String tranNo;

    /**
     * 加密以及待签名字段
     */
    @Valid
    T data;

}
