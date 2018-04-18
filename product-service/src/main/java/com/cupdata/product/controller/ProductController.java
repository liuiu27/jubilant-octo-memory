package com.cupdata.product.controller;


import com.cupdata.product.biz.ServiceProductBiz;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.IProductController;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.ErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Override
    public BaseResponse<ProductInfoVo> findByProductNo(@PathVariable("productNo") String productNo) {
    	log.info("ProductController findByProductNo is begin productNo is " + productNo);
    	try {
	        BaseResponse<ProductInfoVo> productRes = new BaseResponse();
			ProductInfoVo product  = productBiz.selectByProductNo(productNo);
	        if (null == product) {//商品信息未查询到
	            productRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
	            productRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
	            return productRes;
	        }

	        productRes.setData(product);
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
			OrgProductRelVo orgProductRelVo = productBiz.selectReal(orgNo, productNo);
	        if (null == orgProductRelVo){
	            orgProductRelRes.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
	            orgProductRelRes.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
	            return orgProductRelRes;
	        }

	        orgProductRelRes.setData(orgProductRelVo);
	        return orgProductRelRes;
    	} catch (Exception e) {
			log.error("error is " + e.getMessage());
			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
    }
}
