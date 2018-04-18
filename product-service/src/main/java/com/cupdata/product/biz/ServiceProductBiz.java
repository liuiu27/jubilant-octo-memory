package com.cupdata.product.biz;


import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.OrgProductRela;
import com.cupdata.sip.common.dao.entity.ServiceProduct;
import com.cupdata.sip.common.dao.mapper.OrgProductRelaMapper;
import com.cupdata.sip.common.dao.mapper.ServiceProductMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description: 产品service类
 * @Date: 20:20 2017/12/14
 */
@Slf4j
@Service
public class ServiceProductBiz {

    @Autowired
    private ServiceProductMapper productDao;

    @Autowired
    private OrgProductRelaMapper orgProductRelaDao;

    /**
     * 根据商品编号，查询商品信息
     * @param productNo
     * @return
     */
    public ProductInfoVo selectByProductNo(String productNo){
        log.info("根据商品编号查询商品信息,productNo:"+productNo);
        ServiceProduct serviceProduct = new ServiceProduct();
        serviceProduct.setProductNo(productNo);
        serviceProduct = productDao.selectOne(serviceProduct);//查询出单条产品信息
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
        log.info("根据机构号和产品编号查询关系信息,orgNo:"+orgNo+",productNo:"+productNo);
        OrgProductRela orgProductRela = new OrgProductRela();
        orgProductRela.setOrgNo(orgNo);
        orgProductRela.setProductNo(productNo);
        orgProductRela = orgProductRelaDao.selectOne(orgProductRela);
        OrgProductRelVo orgProductRelVo = new OrgProductRelVo();
        BeanCopierUtils.copyProperties(orgProductRela,orgProductRelVo);
        return orgProductRelVo;
    }
}
