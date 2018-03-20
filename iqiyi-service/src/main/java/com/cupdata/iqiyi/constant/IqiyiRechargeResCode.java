package com.cupdata.iqiyi.constant;

/**
 * @Author: DingCong
 * @Description:
 * @CreateDate: 2018/3/2 10:59
 */

public enum IqiyiRechargeResCode {

    /**
     * 成功
     */
    SUCCESS("0","充值成功"),

    /**
     * 充值出现异常
     */
    RECHARGE_EXCEPTION("-1","账号充值出现异常");


    /**
     * 响应码
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

    private IqiyiRechargeResCode(String code, String msg) {
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
