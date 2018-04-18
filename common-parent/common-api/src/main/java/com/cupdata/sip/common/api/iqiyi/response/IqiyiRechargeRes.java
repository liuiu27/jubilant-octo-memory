package com.cupdata.sip.common.api.iqiyi.response;

import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 爱奇艺充值响应vo
 * @CreateDate: 2018/2/6 14:52
 */
@Data
public class IqiyiRechargeRes {

    /**
     * 响应码
     * A00000：充值成功，其他响应码表示非充值成功
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

}
