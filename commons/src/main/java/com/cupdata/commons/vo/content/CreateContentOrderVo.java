package com.cupdata.commons.vo.content;

import lombok.Data;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月9日 下午1:57:45
*/
@Data
public class CreateContentOrderVo {
	   /**
     * 机构编号
     */
    private String orgNo;

    /**
     * 机构订单号
     */
    private String orgOrderNo;

    /**
     * 券码产品编号
     */
    private String productNo;

    /**
     * 订单描述
     */
    private String orderDesc;
    
    /***
     * 内容引入请求参数
     */
    private ContentJumpReq contentJumpReq;
    
}
