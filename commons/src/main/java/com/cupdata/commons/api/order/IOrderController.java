package com.cupdata.commons.api.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;

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
     * 根据机构号以及机构订单号，查询券码订单
     * @param orgNo 机构编号
     * @param orgOrderNo 机构订单号
     * @return
     */
    @GetMapping("/getVoucherOrder/{orgNo}/{orgOrderNo}")
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(@PathVariable("orgNo") String orgNo, @PathVariable("orgOrderNo") String orgOrderNo);
    
    /**
     * 根据供应商标识 供应商编号 券码号 获取 券码表 和订单表信息
     * @param sup
     * @param supplierOrderNo
     * @param voucherCode
     * @return
     */
    @GetMapping("/getVoucherOrderByVoucher/{sup}/{supplierOrderNo}/{voucherCode}")
    public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(@PathVariable("sup") String sup, @PathVariable("supplierOrderNo") String supplierOrderNo,@PathVariable("voucherCode") String voucherCode);   
    
    
}
