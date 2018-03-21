package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月9日 下午1:57:45
*/
@Getter
@Setter
public class CreateContentOrderVo {
	   /**
     * 机构编号
     */
	@NotBlank
    private String orgNo;

    /**
     * 机构订单号
     */
	@NotBlank
    private String orgOrderNo;

    /**
     * 券码产品编号
     */
	@NotBlank
    private String productNo;

    /**
     * 订单描述
     */
	@NotBlank
    private String orderDesc;
    
    /***
     * 内容引入请求参数
     */
	@NotBlank
    private ContentJumpReq contentJumpReq;
    
}
