package com.cupdata.commapi;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auth: LinYong
 * @Description:订单服务接口
 * @Date: 11:28 2017/12/14
 */
public interface OrderService {
    /**
     *
     * @return
     */
    @GetMapping("/hello")
    String hello();
}
