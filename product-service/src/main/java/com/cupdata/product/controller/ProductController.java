package com.cupdata.product.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.commons.api.product.IProductController;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.model.OrgProductRela;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.product.biz.OrgProductRelaBiz;
import com.cupdata.product.biz.ServiceProductBiz;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: LinYong
 * @Description:服务产品controller
 * @Date: 21:12 2017/12/14
 */
@Slf4j
@RestController
public class ProductController implements IProductController {

    @Resource
    private ServiceProductBiz productBiz;

    @Resource
    private OrgProductRelaBiz orgProductRelaBiz;

    @Override
    public BaseResponse<ProductInfVo> findByProductNo(@PathVariable("productNo") String productNo) {
    	log.info("ProductController findByProductNo is begin productNo is " + productNo);
    	try {
	        BaseResponse<ProductInfVo> productRes = new BaseResponse();
	        ServiceProduct product = productBiz.selectByProductNo(productNo);
	        if (null == product) {//商品信息未查询到
	            productRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
	            productRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
	            return productRes;
	        }
	
	        ProductInfVo productInfVo = new ProductInfVo();
	        productInfVo.setProduct(product);//商品信息
	        productRes.setData(productInfVo);
	
	        return productRes;
    	} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
    }

    @Override
    public BaseResponse<OrgProductRelVo> findRel(@PathVariable("orgNo") String orgNo, @PathVariable("productNo") String productNo) {
    	log.info("ProductController findRel is begin productNo is " + productNo + "orgNo is " + orgNo);
    	try {
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
    	} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
    }
}
