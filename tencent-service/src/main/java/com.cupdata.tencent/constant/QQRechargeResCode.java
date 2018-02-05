package com.cupdata.tencent.constant;

/**
 * @Author: DingCong
 * @Description: 腾讯相关业务状态码
 * @CreateDate: 2018/2/2 15:56
 */

public enum QQRechargeResCode {

    /**
     * 成功
     */
    SUCCESS("0","充值成功"),

    /**
     * QQ号不合法
     */
    QQNUMBER_ILLEGAL("-1","QQ号码不合法"),


    /**
     * 用户不合法
     */
    QQMEMBER_RECHARGE_FAIL("-2","QQ会员充值失败"),

    /**
     * 充值出现异常
     */
    QQRECHARGE_EXCEPTION("-3","QQ充值出现异常");


    /**
     * 响应码
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

    private QQRechargeResCode(String code, String msg) {
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
