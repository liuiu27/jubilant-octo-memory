package com.cupdata.ihuyi.constant;

/**
 * @Author: DingCong
 * @Description:
 * @CreateDate: 2018/3/2 10:59
 */

public enum IhuyiRechargeResCode {

    /**
     * 成功
     */
    SUCCESS("0","充值成功"),

    /**
     * 获取互亿话费充值url失败
     */
    FAIL_TO_GET_URL("100004","互亿话费充值url获取失败"),

    /**
     * 互亿充值失败
     */
    FAIL_TO_RECHARGE("100006","互亿充值失败"),

    /**
     * 获取互亿api_id失败
     */
    FAIL_TO_GET_API_ID("100005","获取互亿api_id失败");




    /**
     * 响应码
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

    private IhuyiRechargeResCode(String code, String msg) {
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
