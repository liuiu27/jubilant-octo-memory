package com.cupdata.content.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author junliang
 * @date 2018/04/17
 */
@Getter
@Setter
public class ContentException extends RuntimeException {

    String page="error";

    String tips="非常抱歉，找不到您的页面了……";

    String code="404";
}
