package com.cupdata.product.controller;

import com.cupdata.commons.api.product.IProductController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.model.OrgProductRela;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.product.biz.OrgProductRelaBiz;
import com.cupdata.product.biz.ServiceProductBiz;
import com.cupdata.product.biz.ServiceProductConfigBiz;
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
    private ServiceProductBiz productBiz;

    @Resource
    private ServiceProductConfigBiz productConfigBiz;

    @Resource
    private OrgProductRelaBiz orgProductRelaBiz;

    @Override
    public BaseResponse<ProductInfVo> findByProductNo(@PathVariable("productNo") String productNo) {
        BaseResponse<ProductInfVo> productRes = new BaseResponse();
        ServiceProduct product = productBiz.selectByProductNo(productNo);
        if (null == product) {//商品信息未查询到
            productRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            productRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
            return productRes;
        }

        //查询商品配置信息
        ServiceProductConfig productConfig = null;
        if (null != product.getConfigId()){
            productConfig = productConfigBiz.select(product.getConfigId());
        }

        ProductInfVo productInfVo = new ProductInfVo();
        productInfVo.setProduct(product);//商品信息
        productInfVo.setProductConfig(productConfig);//商品配置信息
        productRes.setData(productInfVo);

        return productRes;
    }

    @Override
    public BaseResponse<OrgProductRelVo> findRel(@PathVariable("orgNo") String orgNo, @PathVariable("productNo") String productNo) {
        BaseResponse<OrgProductRelVo> orgProductRelRes = new BaseResponse();
        OrgProductRela orgProductRela = orgProductRelaBiz.selectReal(orgNo, productNo);
        if (null == orgProductRela){
            orgProductRelRes.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            orgProductRelRes.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
            return orgProductRelRes;
        }

        OrgProductRelVo orgProductRelVo = new OrgProductRelVo();
        orgProductRelVo.setOrgProductRela(orgProductRela);
        orgProductRelRes.setData(orgProductRelVo);
        return orgProductRelRes;
    }
}
