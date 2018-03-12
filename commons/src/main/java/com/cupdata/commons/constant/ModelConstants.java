package com.cupdata.commons.constant;

/**
 * @Auth: LinYong
 * @Description:数据库实体字段的常量信息
 * @Date: 11:13 2017/12/20
 */
public class ModelConstants {
    /**
     * 机构类型-CUPD：银数机构
     */
    public static String ORG_TYPE_CUPD = "CUPD";

    /**
     * 机构类型-BANK：银行机构
     */
    public static String ORG_TYPE_BANK = "BANK";

    /**
     * 机构状态-0：无效
     */
    public static String ORG_STATUS_INVALID = "0";

    /**
     * 机构状态-1：有效
     */
    public static String ORG_STATUS_VALID = "1";

    /**
     * 商品类型-RECHARGE：充值产品   RECHARGE
     */
    public static String PRODUCT_TYPE_RECHARGE = "RECHARGE";

    /**
     * 商品类型-VOUCHER：券码产品 VOUCHER
     */
    public static String PRODUCT_TYPE_VOUCHER = "VOUCHER";
    
    /**
     * 商品类型-CONTENT：内容引入产品  CONTENT
     */
    public static String PRODUCT_TYPE_CONTENT = "CONTENT";

    /**
     * 订单状态-0：未支付
     */
    public static Character ORDER_STATUS_INITIAL = '0';

    /**
     * 订单状态-1：处理中
     */
    public static Character ORDER_STATUS_HANDING = '1';

    /**
     * 订单状态-S：订单成功
     */
    public static Character ORDER_STATUS_SUCCESS = 'S';

    /**
     * 订单状态-F：订单失败
     */
    public static Character ORDER_STATUS_FAIL = 'F';

    /**
     * 是否需要异步通知-0：不需要
     */
    public static Character IS_NOTIFY_NO = '0';

    /**
     * 是否需要异步通知-1：需要
     */
    public static Character IS_NOTIFY_YES = '1';

    /**
     * 通知状态-初始状态未通知
     */
    public static Character NOTIFY_STATUS_INITIAL = '0';

    /**
     * 通知状态-通知失败
     */
    public static Character NOTIFY_STATUS_FAIL = '1';

    /**
     * 通知状态-通知成功
     */
    public static Character NOTIFY_STATUS_SUCCESS = '2';

    /**
     * 通知状态-通知中
     */
    public static Character NOTIFY_STATUS_ING = '3';

    /**
     * 服务供应商标识-TRVOK：空港易行
     */
    public static String SUPPLIER_FLAG_TRVOK = "TRVOK";
    
    /**
     * 券码使用状态 ：未使用
     */
    public static Character VOUCHER_USE_STATUS_UNUSED = '0';
    
    /**
     * 券码使用状态 ：已使用
     */
    public static Character VOUCHER_USE_STATUS_USE = '1';
    
    /**
     * 券码有效状态 ： 有效
     */
    public static Character VOUCHER_STATUS_EFF = '0';
    
    /**
     * 券码有效状态 ： 禁用
     */
    public static Character VOUCHER_STATUS_INVALID = '1';
    
    //=================   内容引入交易类型  ============================
    
    /**
     * 内容引入交易类型  未登录
     */
    public static Character CONTENT_TYPE_NOT_LOGGED = '0'; 
    
    
    
}
