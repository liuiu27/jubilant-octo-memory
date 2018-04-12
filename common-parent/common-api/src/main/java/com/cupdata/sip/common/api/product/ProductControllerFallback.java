package com.cupdata.sip.common.api.product;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;

/**
 * @author junliang
 * @date 2018/04/12
 */
public class ProductControllerFallback implements IProductController {
    @Override
    public BaseResponse<ProductInfoVo> findByProductNo(String productNo) {
        return null;
    }

    @Override
    public BaseResponse<OrgProductRelVo> findRel(String orgNo, String productNo) {
        return null;
    }
}
