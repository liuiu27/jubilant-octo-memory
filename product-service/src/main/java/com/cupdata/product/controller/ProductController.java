package com.cupdata.product.controller;

import com.cupdata.commons.api.product.IProductController;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.product.biz.ProductBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auth: LinYong
 * @Description:服务产品controller
 * @Date: 21:12 2017/12/14
 */

@RestController
public class ProductController implements IProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Resource
    private ProductBiz productBiz;

    @Override
    public ServiceProduct findProductById(@PathVariable("id") Long id) {
        return null;
    }
}
