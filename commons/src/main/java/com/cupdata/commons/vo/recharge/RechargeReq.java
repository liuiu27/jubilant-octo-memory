package com.cupdata.commons.vo.recharge;

import com.cupdata.commons.vo.BaseRequest;
import lombok.Data;

/**
 * @ClassName: RechargeProductReq
 * @Description: 虚拟充值接口请求vo
 * @author LinYong
 * @date 2017年6月30日 下午2:22:53
 */
@Data
public class RechargeReq extends BaseRequest{

	/**
	 *机构订单唯一性标识
	 */
	private String orgOrderNo;

	/**
	 * 服务产品编号
	 */
	private String productNo;

	/**
	 * 有效期
	 * 格式为yyyyMMdd
	 */
	private String expire;

	/**
	 *订单描述
	 */
	private String orderDesc;

	/**
	 * 手机号码
	 */
	private String mobileNo;

    /**
     *充值账号
     */
    private String account;

    /**
     *游戏大区名称
     */
    private String gameRegion;

    /**
     * 游戏服务名称
     */
    private String gameServer;

    /**
     *服务异步通知地址
     */
    private String notifyUrl;
}

