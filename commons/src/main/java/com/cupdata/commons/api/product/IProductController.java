package com.cupdata.commons.api.product;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auth: LinYong
 * @Description:服务产品服务接口
 * @Date: 13:09 2017/12/14
 */
public interface IProductController {
    /**
     * 根据服务产品编号，查询服务产品信息
     * @param productNo 服务产品编号
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/product/{productNo}")
    public BaseResponse<ProductInfVo> findByProductNo(@PathVariable("productNo") String productNo);

    /**
     *  根据服务产品编号以及机构编号，查询机构/服务产品关系信息
     * @param orgNo 机构编号
     * @param productNo 服务产品编号
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/org-product-real/{orgNo}/{productNo}")
    public BaseResponse<OrgProductRelVo> findRel( @PathVariable("orgNo") String orgNo, @PathVariable("productNo") String productNo);
}
