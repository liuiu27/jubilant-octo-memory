package com.cupdata.commons.api.order;

import com.cupdata.commons.model.ServiceOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:订单服务接口
 * @Date: 11:28 2017/12/14
 */
public interface IOrderController {

    /**
     *根据订单id，查询订单信息
     * @return
     */
    @GetMapping("/order/{id}")
    public ServiceOrder findOrderById(@PathVariable("id") Long id);
}
