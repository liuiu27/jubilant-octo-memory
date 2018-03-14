package com.cupdata.commons.vo.recharge;

import com.cupdata.commons.vo.BaseData;
import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 充值结果查询
 * @CreateDate: 2018/3/12 18:42
 */
@Data
public class RechargeResQuery extends BaseData{

    /**
     * 平台订单
     */
    private String orderNo;

    /**
     * 机构唯一订单号
     */
    private String orgOrderNo;

    /**
     * 服务产品编号
     */
    private String productNo;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 充值账号
     */
    private String account;

    /**
     * 订单描述
     */
    private String orderDesc;

    /**
     * 充值状态
     */
    private Character rechargeStatus;

}
