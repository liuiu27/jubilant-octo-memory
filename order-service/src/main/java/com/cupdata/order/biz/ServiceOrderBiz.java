package com.cupdata.order.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.OrgProductRela;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.order.dao.ServiceOrderDao;
import com.cupdata.order.dao.ServiceOrderVoucherDao;
import com.cupdata.order.util.OrderUtils;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceOrderBiz extends BaseBiz<ServiceOrder> {
    @Autowired
    private ServiceOrderDao orderDao;

    @Autowired
    private ServiceOrderVoucherDao orderVoucherDao;

    @Override
    public BaseDao<ServiceOrder> getBaseDao() {
        return orderDao;
    }

    /**
     * 根据券码产品，创建券码订单
     * @param voucherProduct 券码商品
     * @return
     */
    public ServiceOrderVoucher createVoucherOrder(String orgNo, String orgOrderNo, String orderDesc, ServiceProduct voucherProduct, OrgProductRela orgProductRela){
        //初始化主订单记录
        ServiceOrder order = OrderUtils.initServiceOrder(orgNo, orgOrderNo, orderDesc, voucherProduct, orgProductRela);
        orderDao.insert(order);//插入主订单

        //初始化券码订单
        ServiceOrderVoucher voucherOrder = OrderUtils.initVoucherOrder(order, voucherProduct.getProductNo());
        orderVoucherDao.insert(voucherOrder);//插入券码订单

        return voucherOrder;
    }
}
