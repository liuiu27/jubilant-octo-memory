package com.cupdata.commons.constant;

/**
 * @Auth: LinYong
 * @Description:响应码以及响应信息
 * @Date: 11:14 2017/12/20
 */
public enum ResponseCodeMsg {
    /**
     * 000000 - 成功
     */
    SUCCESS("000000", "成功"),

    /**
     * 100002 - 密文有误，解密失败
     */
    ENCRYPTED_DATA_ERROR("100002", "密文数据有误，解密失败"),

    /**
     * 100003 - 签名不正确
     */
    ILLEGAL_SIGN("100003", "签名不正确"),

    /**
     * EEEEEE - 失败
     */
    FAIL("EEEEEE", "失败");

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

    private ResponseCodeMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
