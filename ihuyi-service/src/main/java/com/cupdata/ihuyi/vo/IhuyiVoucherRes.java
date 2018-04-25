package com.cupdata.ihuyi.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: DingCong
 * @Description: 互亿券码响应结果
 * @CreateDate: 2018/3/12 10:46
 */
@Data
public class IhuyiVoucherRes {

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

    /**
     * 卡密，购买失败则没有
     */
    private List<IhuyiGiftCardInfo> cards;


    //互亿礼品券信息列表
    @Data
    public static class IhuyiGiftCardInfo {
        /**
         * 卡号（DES3加密内容）
         */
        private String no;

        /**
         * 卡密（DES3加密内容）
         */
        private String pwd;

        /**
         * 时间戳
         */
        private int expired;

        /**
         * 其他信息
         */
        private String other;
    }

}
