package com.cupdata.ihuyi.vo;

import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 互亿充值响应结果
 * @CreateDate: 2018/3/6 16:57
 */
@Data
public class IhuyiRechargeRes {

    /**
     * 状态（1 为提交成功）
     */
    private int code;

    /**
     * 消息描述
     */
    private String message;

    /**
     * 任务 id,提交失败则没有
     */
    private String taskid;
}
