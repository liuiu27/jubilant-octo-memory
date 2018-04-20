package com.cupdata.sip.common.api.cdd.response;


/**
 * @author liwei
 * @date   2018/1/29
 */
import lombok.Data;

@Data
public class CddCodeRes{
	/**
	 * 结果代码，详见上表注释
	 */
    private String status;
    
    /**
     * 结果说明
     */
    private String msg;
    
    /**
     * 批次号，确认赠送时需要传入
     */
    private String batchId;
    
    /***
     * 接口调用方传的流水号
     */
    private String sn;
    
    /**
     * 申请礼包结果集
     */
    private String plist;
    
    /**
     * 该礼包订单ID
     */
    private String pid;
    
    /**
     * 该礼包下的券结果集
     */
    private String yzmList;
    
    /**
     * 券的验证码
     */
    private String yzm;
    
    /**
     * 券的ID（一张券的ID唯一，商户验券时如果需要同步结果，以券ID为标识）
     */
    private String ticketId;
    
    /**
     * 券的有效起始时间
     */
    private String dateBeginTime;
    
    /**
     * 券的有效截止时间
     */
    private String dateOutTime;
    
    /**
     * 礼包ID(双方约定好，由车点点提供)
     */
    private String packageId;
}	
