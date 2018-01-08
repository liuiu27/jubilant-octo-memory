package com.cupdata.commons.api.order;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.CreateVoucherOrderVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auth: LinYong
 * @Description:订单服务接口
 * @Date: 11:28 2017/12/14
 */
public interface IOrderController {
    /**
     * 根据券码产品等信息，创建券码订单
     * @param createVoucherOrderVo 创建券码订单参数vo
     * @return
     */
    @PostMapping("/createVoucherOrder")
    public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody CreateVoucherOrderVo createVoucherOrderVo);

    /**
     * 根据机构号以及机构订单号，查询券码订单
     * @param orgNo 机构编号
     * @param orgOrderNo 机构订单号
     * @return
     */
    @GetMapping("/getVoucherOrder/{orgNo}/{orgOrderNo}")
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(@PathVariable("orgNo") String orgNo, @PathVariable("orgOrderNo") String orgOrderNo);

}
