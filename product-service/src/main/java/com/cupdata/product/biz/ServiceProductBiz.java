package com.cupdata.product.biz;


import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.OrgProductRela;
import com.cupdata.sip.common.dao.entity.ServiceProduct;
import com.cupdata.sip.common.dao.mapper.OrgProductRelaMapper;
import com.cupdata.sip.common.dao.mapper.ServiceProductMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceProductBiz {

    @Autowired
    private ServiceProductMapper productMapper;

    private OrgProductRelaMapper orgProductRelaMapper;

    /**
     * 根据商品编号，查询商品信息
     * @param productNo
     * @return
     */
    public ProductInfoVo selectByProductNo(String productNo){

       ServiceProduct serviceProduct = productMapper.selectByProductNo(productNo);

        ProductInfoVo productInfoVo =new ProductInfoVo();

        BeanCopierUtils.copyProperties(serviceProduct,productInfoVo);

        return productInfoVo;
    }


    /**
     * 查询机构与商品关系记录
     * @param orgNo 机构编号
     * @param productNo 商品编号
     * @return
     */
    public OrgProductRelVo selectReal(String orgNo, String productNo) {
        OrgProductRelVo orgProductRelVo = new OrgProductRelVo();

        OrgProductRela orgProductRela = orgProductRelaMapper.selectRealByorgNoAndproductNo(orgNo,productNo);

        BeanCopierUtils.copyProperties(orgProductRela,orgProductRelVo);

        return orgProductRelVo;
    }
}
