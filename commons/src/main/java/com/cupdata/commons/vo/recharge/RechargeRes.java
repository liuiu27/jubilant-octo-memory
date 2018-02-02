package com.cupdata.commons.vo.recharge;

import com.cupdata.commons.vo.BaseData;
import com.cupdata.commons.vo.BaseResponse;
import lombok.Data;

/**
 * 
 * @ClassName: RechargeProductRes
 * @Description: 充值响应参数vo
 * @author LinYong
 * @date 2017年6月30日 下午2:22:53
 */
@Data
public class RechargeRes extends BaseData {

    /**
     *平台订单号
     */
    private String orderNo;

    /**
     * 充值状态
     */
    private String rechargeStatus;

}
