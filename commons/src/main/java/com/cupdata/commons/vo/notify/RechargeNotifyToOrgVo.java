package com.cupdata.commons.vo.notify;

import com.cupdata.commons.vo.BaseRequest;
import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 充值结果通知机构vo
 * @CreateDate: 2018/3/7 14:41
 */
@Data
public class RechargeNotifyToOrgVo extends BaseRequest {

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 机构订单号
     */
    private String orgOrderNo;

    /**
     * 充值状态(1：充值中；F：充值失败；S：充值成功；)
     */
    private Character rechargeStatus;


}
