package com.cupdata.commons.vo;

import lombok.Data;

/**
 * @Auth: LinYong
 * @Description:基础请求Vo
 * @Date: 16:26 2017/12/19
 */
@Data
public class BaseRequest {
    /**
     * 时间戳
     * 时间戳精确到毫秒，格式为yyyyMMddHHmmssSSS+8位随机数，共计25位；
     * 防重放，时间戳控制3分钟内有效，并且只能使用一次
     */
    private String timesta	mp;
}
