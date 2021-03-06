package com.cupdata.sip.common.lang.constant;

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
     * 100001 - 参数无效
     */
    PARAM_INVALID("100001","参数无效"),

    /**
     * 100002 - 密文有误，解密失败
     */
    ENCRYPTED_DATA_ERROR("100002", "密文数据有误，解密失败"),

    /**
     * 100003 - 签名不正确
     */
    ILLEGAL_SIGN("100003", "签名不正确"),
    
    /**
     * 100004 -  获取信息失败
     */
	FAILED_TO_GET("100004" ,"获取券码失败"),

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
     * 100009 - 核销码不存在或已经使用
     */
     REQUEST_TIMEOUT("100010" ,"系统响应超时"),

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
     * 系统配置表查询结果为空
     */
    CONFIG_QUERY_EMPTY("999901","系统配置表查询结果为空"),
    
    /**
     * 供应商订单号重复
     */
    SUPORDERNO_EXISTS("999902","订单号重复"),
    
    /**
     * 999999 - 查询机构信息失败
     */
    ILLEGAL_PARTNER("999999" ,"合作机构信息获取失败"),

    /**
     * 800000 - 3DES加密数据获取失败
     */
    FAIL_TO_GET_3DES("800000", "3DES加密数据获取失败"),

    /**
     * 800001 - 3DES加密数据获取失败
     */
    FAIL_TO_GET_3DES_IV("800001", "3DES偏移加密数据获取失败"),

    /**
     * 800002 - 订单更新失败
     */
    ORDER_UPDATE_ERROR("800002", "订单更新失败"),

    /**
     * EEEEEE - 失败
     */
    FAIL("EEEEEE", "失败"),

    /**
     * 800003 - 券码类型无效
     */
    VOUCHER_TYPE_INVALID("800003","该类券码状态为无效"),

    /**
     * 800004 - 券码列表获没有可用券码
     */
    NO_VOUCHER_AVALIABLE("800004","券码列表没有可用券码"),

    /**
     * 800005 - 流水号无效
     */
    NO_TRANNO_AINVALID("800005","流水号无效"),

    /**
     * 800006 - 查询订单异常
     */
    QUERY_ORDER_EXCEPTION("800006","查询订单异常"),

    /**
     * 800007 - 获取本地券码失败
     */
    FAIL_GAT_LOCAL_VOUCHER("800007","获取券码失败"),

    /**
     * 800008 - 查询订单异常查询商品信息为空
     */
    QUERY_PRODUCT_INF_NULL("800008","查询商品信息为空"),

    /**
     * 800009 - 根据商户编号获取商户失败
     */
    GET_SUPPLIER_FAIL_BY_NO("800009","根据商户编号获取商户失败"),

    /**
     * 200001 - 调用虚拟充值接口发生异常
     */
    RECHARGE_INTER_EXCEPTION("200001","调用虚拟充值接口发生异常"),

    /**
     * 200002 - 调用虚拟充值结果查询接口发生异常
     */
    RECHARGE_RES_QUERY_EXCEPTION("200002","调用虚拟充值结果查询接口发生异常"),

    /**
     * 200003 - 调用腾讯充值服务发生异常
     */
    TENCENT_EXCEPTION("200003","调用腾讯充值服务发生异常"),

    /**
     * 200004 - 调用爱奇艺充值接口发生异常
     */
    IQIYI_EXCEPTION("200004","调用爱奇艺充值接口发生异常"),

    /**
     * 200005 - 调用互亿话费充值接口发生异常
     */
    IHUYI_PHONE_RECHARGE_EXCEPTION("200005","调用互亿话费充值接口发生异常"),

    /**
     * 200006 - 互亿话费充值结果推送发生异常
     */
    IHUYI_PUSH_PHONE_RECHARGE_RES_EXCEPTION("200006","互亿话费充值结果推送发生异常"),

    /**
     * 200007 - 调用互亿流量充值接口发生异常
     */
    IHUYI_TRAFFIC_RECHARGE_EXCEPTION("200007","调用互亿流量充值接口发生异常"),

    /**
     * 200008 - 互亿流量充值结果推送发生异常
     */
    IHUYI_PUSH_TRAFFIC_RECHARGE_RES_EXCEPTION("200008","互亿流量充值结果推送发生异常"),

    /**
     * 200009 - 充值失败
     */
    RECHARGE_FAIL("200009","充值失败");






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
