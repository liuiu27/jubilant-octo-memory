package com.cupdata.commons.api.order;

import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;
import com.cupdata.commons.vo.order.ServiceOrderList;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.recharge.CreateRechargeOrderVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
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
     * 根据订单编号获取主订单
     * @return
     */
    @PostMapping("/getServiceOrderByOrderNo/{orderNo}")
    public BaseResponse<ServiceOrder> getServiceOrderByOrderNo(@PathVariable("orderNo") String orderNo);
    /**
     * 根据券码产品等信息，创建券码订单
     * @param createVoucherOrderVo 创建券码订单参数vo
     * @return
     */
    @PostMapping("/createVoucherOrder")
    public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody CreateVoucherOrderVo createVoucherOrderVo);
    
    /**
     * 修改订单
     * @param voucherOrderVo
     * @return
     */
    @PostMapping("/updateVoucherOrder")
    public BaseResponse<VoucherOrderVo> updateVoucherOrder(@RequestBody VoucherOrderVo voucherOrderVo);
    
    /**
     * 根据订单号获取订单及券码信息
     * @param orderNo
     * @return
     */
    @GetMapping("/getVoucherOrderByOrderNo/{orderNo}")
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(@PathVariable("orderNo") String orderNo);

    /**
     * 根据订单号获取订单及充值订单信息
     * @param orderNo
     * @return
     */
    @GetMapping("/getRechargeOrderByOrderNo/{orderNo}")
    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(@PathVariable("orderNo") String orderNo);

    /**
     * 根据机构号以及机构订单号，查询券码订单
     * @param orgNo 机构编号
     * @param orgOrderNo 机构订单号
     * @return
     */
    @GetMapping("/getVoucherOrder/{orgNo}/{orgOrderNo}")
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(@PathVariable("orgNo") String orgNo, @PathVariable("orgOrderNo") String orgOrderNo);

    /**
     * 根据机构号以及机构订单号,查询充值订单
     * @param orgNo
     * @param orgOrderNo
     * @return
     */
    @GetMapping("/getRechargeOrder/{orgNo}/{orgOrderNo}")
    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(@PathVariable("orgNo") String orgNo, @PathVariable("orgOrderNo") String orgOrderNo);

    /**
     * 根据供应商标识 供应商编号 券码号 获取 券码表 和订单表信息
     * @param sup
     * @param supplierOrderNo
     * @param voucherCode
     * @return
     */
    @GetMapping("/getVoucherOrderByVoucher/{sup}/{supplierOrderNo}/{voucherCode}")
    public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(@PathVariable("sup") String sup, @PathVariable("supplierOrderNo") String supplierOrderNo,@PathVariable("voucherCode") String voucherCode);

    /**
     * 根据充值产品等信息，创建充值订单
     * @param createRechargeOrderVo 创建充值订单参数vo
     * @return
     */
    @PostMapping("/createRechargeOrder")
    public BaseResponse<RechargeOrderVo> createRechargeOrder(@RequestBody CreateRechargeOrderVo createRechargeOrderVo);

    /**
     * 修改充值订单
     * @param rechargeOrderVo
     * @return
     */
    @PostMapping("/updateRechargeOrder")
    public BaseResponse<RechargeOrderVo> updateRechargeOrder(@RequestBody RechargeOrderVo rechargeOrderVo);

    /**
     *更新主订单
     * @param order
     */
    @PostMapping("/updateServiceOrder")
    public Integer updateServiceOrder(@RequestBody ServiceOrder order);

    /**
     * 根据参数获取主单列表
     * @param orderStatus
     * @param orderSubType
     * @param supplierFlag
     * @return
     */
    @GetMapping("/getServiceOrderListByParam/{orderStatus}/{orderSubType}/{supplierFlag}")
    public BaseResponse<ServiceOrderList> getServiceOrderListByParam(@PathVariable("orderStatus") Character orderStatus, @PathVariable("orderSubType") String orderSubType,@PathVariable("supplierFlag") String supplierFlag);


    /**
     * 获取主订单
     * @param orderStatus
     * @param supplierFlag
     * @param orderSubType
     * @return
     */
   @GetMapping("/selectMainOrderList/{orderStatus}/{supplierFlag}/{orderSubType}")
    public List<ServiceOrder> selectMainOrderList(@PathVariable("orderStatus") Character orderStatus, @PathVariable("supplierFlag") String supplierFlag, @PathVariable("orderSubType") String orderSubType);

    /**
     * 内容引入订单查询
     * @param contentQueryOrderReq
     * @return
     */
    @PostMapping("/queryContentOrder")
    public BaseResponse<ContentQueryOrderRes> queryContentOrder(@RequestBody ContentQueryOrderReq contentQueryOrderReq);

    /**
     * 创建内容订单
     *
     * @return 响应
     */
    @PostMapping("/createContentOrder")
    BaseResponse createContentOrder();

    /**
     * 更新内容订单
     *
     * @return
     */
    @PostMapping("/updateContentOrder")
    BaseResponse updateContentOrder();

}
