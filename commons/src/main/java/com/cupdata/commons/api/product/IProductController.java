package com.cupdata.commons.api.product;

import com.cupdata.commons.model.ServiceProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:服务产品服务接口
 * @Date: 13:09 2017/12/14
 */
public interface IProductController {
    /**
     *根据服务产品id，查询产品信息
     * @return
     */
    @GetMapping("/product/{id}")
    public ServiceProduct findProductById(@PathVariable("id") Long id);
}
