package com.cupdata.sip.common.api.product;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author junliang
 * @date 2018/04/11
 */
@RequestMapping("product")
public interface IProductController {

    /**
     * 根据服务产品编号，查询服务产品信息
     * @param productNo 服务产品编号
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "findByProductNo/{productNo}")
    BaseResponse<ProductInfoVo> findByProductNo(@PathVariable("productNo") String productNo);

    /**
     *  根据服务产品编号以及机构编号，查询机构/服务产品关系信息
     * @param orgNo 机构编号
     * @param productNo 服务产品编号
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/org-product-real/{orgNo}/{productNo}")
    BaseResponse<OrgProductRelVo> findRel(@PathVariable("orgNo") String orgNo, @PathVariable("productNo") String productNo);

}
