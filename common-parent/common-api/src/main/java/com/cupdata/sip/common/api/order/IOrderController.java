package com.cupdata.sip.common.api.order;


import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.request.CreateContentOrderVo;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.OrderContentVo;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:订单服务接口
 * @Date: 11:28 2017/12/14
 */
@RequestMapping("/order")
public interface IOrderController {
    /**
     * 根据券码产品等信息，创建券码订单
     * @param createVoucherOrderVo 创建券码订单参数vo
     * @return
     */
    @PostMapping("/createVoucherOrder")
    BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody CreateVoucherOrderVo createVoucherOrderVo);
    
    /**
     * 修改订单
     * @param voucherOrderVo
     * @return
     */
    @PostMapping("/updateVoucherOrder")
    BaseResponse updateVoucherOrder(@RequestBody VoucherOrderVo voucherOrderVo);
    
    /**
     * 根据订单号获取订单及券码信息
     * @param orderNo
     * @return
     */
    @GetMapping("/getVoucherOrderByOrderNo/{orderNo}")
    BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(@PathVariable("orderNo") String orderNo);

    /**
     * 根据订单号获取订单及充值订单信息
     * @param orderNo
     * @return
     */
    @GetMapping("/getRechargeOrderByOrderNo/{orderNo}")
    BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(@PathVariable("orderNo") String orderNo);

    /**
     * 根据机构号以及机构订单号，查询券码订单
     * @param orgNo 机构编号
     * @param orgOrderNo 机构订单号
     * @return
     */
    @GetMapping("/getVoucherOrder/{orgNo}/{orgOrderNo}")
    BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(@PathVariable("orgNo") String orgNo, @PathVariable("orgOrderNo") String orgOrderNo);

    /**
     * 根据机构号以及机构订单号,查询充值订单
     * @param orgNo
     * @param orgOrderNo
     * @return
     */
    @GetMapping("/getRechargeOrder/{orgNo}/{orgOrderNo}")
    BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(@PathVariable("orgNo") String orgNo, @PathVariable("orgOrderNo") String orgOrderNo);

    /**
     * 根据供应商标识 供应商编号 券码号 获取 券码表 和订单表信息
     * @param sup
     * @param supplierOrderNo
     * @param voucherCode
     * @return
     */
    @GetMapping("/getVoucherOrderByVoucher/{sup}/{supplierOrderNo}/{voucherCode}")
    BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(@PathVariable("sup") String sup, @PathVariable("supplierOrderNo") String supplierOrderNo, @PathVariable("voucherCode") String voucherCode);

    /**
     * 根据充值产品等信息，创建充值订单
     * @param createRechargeOrderVo 创建充值订单参数vo
     * @return
     */
    @PostMapping("/createRechargeOrder")
    BaseResponse<RechargeOrderVo> createRechargeOrder(@RequestBody CreateRechargeOrderVo createRechargeOrderVo);

    /**
     * 修改充值订单
     * @param rechargeOrderVo
     * @return
     */
    @PostMapping("/updateRechargeOrder")
    BaseResponse updateRechargeOrder(@RequestBody RechargeOrderVo rechargeOrderVo);

    /**
     *更新主订单
     * @param order
     */
    @PostMapping("/updateServiceOrder")
    BaseResponse updateServiceOrder(@RequestBody OrderInfoVo order);

    /**
     * 获取主订单
     * @param orderStatus
     * @param supplierFlag
     * @param orderSubType
     * @return
     */
   @GetMapping("/selectMainOrderList/{orderStatus}/{supplierFlag}/{orderSubType}")
   BaseResponse<List<OrderInfoVo>> selectMainOrderList(@PathVariable("orderStatus") String orderStatus, @PathVariable("supplierFlag") String supplierFlag, @PathVariable("orderSubType") String orderSubType);

    /**
     * 内容引入订单查询
     * @return
     */
   @GetMapping("/queryContentOrder/{orderNo}") 
   BaseResponse<OrderContentVo> queryContentOrder(@PathVariable("orderNo") String orderNo);

    /**
     * 创建内容订单
     *
     * @return 响应
     */
    @PostMapping("/createContentOrder")
    BaseResponse<OrderContentVo> createContentOrder(@RequestBody CreateContentOrderVo createContentOrderVo);

    /**
     * 更新内容订单
     *
     * @return
     */
    @PostMapping("/updateContentOrder")
    BaseResponse updateContentOrder(@RequestBody OrderContentVo orderContentVo);

}
