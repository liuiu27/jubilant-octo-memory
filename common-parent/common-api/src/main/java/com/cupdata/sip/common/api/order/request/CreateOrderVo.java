package com.cupdata.sip.common.api.order.request;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月19日 下午7:53:34
*/
@Data
public class CreateOrderVo {
	 /**
     * 机构编号
     */
    private String orgNo;

    /**
     * 机构订单号
     */
    private String orgOrderNo;

    /**
     * 产品编号
     */
    private String productNo;

    /**
     * 订单描述
     */
    private String orderDesc;
    
    /**
     * 通知Url
     */
    private String notifyUrl;
    
    /**
     * 供应商订单号
     */
    private String supOrderNo;
}
