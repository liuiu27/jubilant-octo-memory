package com.cupdata.commons.api.order;

import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.VoucherOrderVo;
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
     * 根据券码产品，创建券码订单
     * @param orgNo 机构编号
     * @param orgOrderNo 机构订单号
     * @param orderDesc 订单描述
     * @param productNo 券码商品编号
     * @return
     */
    @PostMapping("/voucher-order/create")
    public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody String orgNo, @RequestBody String orgOrderNo, @RequestBody String orderDesc, @RequestBody String productNo);
}
