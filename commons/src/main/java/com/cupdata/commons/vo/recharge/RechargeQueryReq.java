package com.cupdata.commons.vo.recharge;

import com.cupdata.commons.vo.BaseRequest;
import lombok.Data;

/**
 * @Author: DingCong
 * @Description: 充值查询vo
 * @CreateDate: 2018/3/13 13:20
 */
@Data
public class RechargeQueryReq extends BaseRequest{

    /**
     * 机构订单编号
     */
    private String orgOrderNo ;

}
