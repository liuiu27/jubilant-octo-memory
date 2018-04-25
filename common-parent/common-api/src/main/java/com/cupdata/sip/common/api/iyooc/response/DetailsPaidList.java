package com.cupdata.sip.common.api.iyooc.response;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 17:28 2018/4/24
 */
@Data
public class DetailsPaidList {

    private String serialNum;			//	平台流水号
    private String paySerialNum;		// 	支付流水号
    private String inputTime;		    //	入场时间
    private String payTime;				//	支付时间
    private String payTimeCount;	    //	支付时长(秒)
    private String paidAmount;		    //	支付金额(分)
}
