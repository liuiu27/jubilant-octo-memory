package com.cupdata.sip.common.api.iyooc.response;

import lombok.Data;

import java.util.List;

/**
 * @Auther: DingCong
 * @Description: 停车缴费信息
 * @@Date: Created in 17:25 2018/4/24
 */
@Data
public class ParkingFeeReqRes {

    /**
     * 状态
     * 0：失败
     * 1：成功
     * 2：操作超时
     */
    private int status;

    /**
     * 响应信息
     */
    private String message;
    private String plateNum;            //	车牌号
    private String serialNum;        // 	平台流水号
    private String parkName;        // 	停车场名称
    private String parkNo;                // 	停车场编号
    private String inputTime;            //	入场时间(YYYYMMDDHHMMSS)
    private String currentTime;    //	当前时间(YYYYMMDDHHMMSS)
    private String parkingDuration;    //	停车总时长（单位：秒）
    private String amount;                        //	停车总金额（单位：分）
    private String totalAmount;//已缴总金额（单位=分），无优惠金额填写0
    private String discAmount;//优惠金额
    private String paidAmount;//实缴金额=停车总金额-优惠金额，无优惠金额填写停车总金额（单位=分）
    private String alreadyPayTime;        //	已支付时长
    private String alreadyPayAmount;    //	已支付金额
    private String payTimeOut;//缴费完成出场超时时间，主要用于提示用户，超过另计费
    private List<DetailsPaidList> detailsPaid;                //	已支付账单明细list




}
