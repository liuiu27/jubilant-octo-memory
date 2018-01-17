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
     * 100004 - 获取信息失败
     */
	FAILED_TO_GET("100004" ,"获取信息失败"),

    /**
     * 100005 - 明文数据加密失败
     */
    DATA_DECRYPT_ERROR("100005" ,"明文数据加密失败"),

    /**
     * 100006 - 明文数据签名失败
     */
    DATA_SIGN_ERROR("100006" ,"明文数据签名失败"),

    /**
     * 100007 - 时间戳超时
     */
    TIMESTAMP_TIMEOUT("100007" ,"时间戳超时"),
    
    /**
     * 100008 - 时间戳有误
     */
    TIMESTAMP_ERROR("100008" ,"时间戳有误"),
    
    /**
     * 100009 - 核销码不存在或已经使用
     */
     VOUCHER_NOT_EXIST("100009" ,"核销码不存在或已经使用"),

	 /**
     * 900000 - 系统故障
     */
    SYSTEM_ERROR("900000","系统故障"),
    
    /**
     * 999994 - 订单创建失败
     */
    ORDER_CREATE_ERROR("999994", "订单创建失败"),

    /**
     * 999995 - 该机构未分配该服务产品
     */
    ORG_PRODUCT_REAL_NOT_EXIT("999995", "该机构未分配该服务产品"),

    /**
     * 999996 - 服务商品不存在
     */
    PRODUCT_NOT_EXIT("999996", "服务商品不存在"),

    /**
     * 999997 - 必填参数为空
     */
    ILLEGAL_ARGUMENT("999997", "必填参数为空"),
    
    /**
     * 查询结果为空
     */
    RESULT_QUERY_EMPTY("999998","查询结果为空"),
    
    /**
     * 999999 - 查询机构信息失败
     */
    ILLEGAL_PARTNER("999999" ,"合作机构信息获取失败"),
    
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
