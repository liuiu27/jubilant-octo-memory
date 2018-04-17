package com.cupdata.notify.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.notify.dao.OrderNotifyCompleteDao;
import com.cupdata.notify.dao.OrderNotifyWaitDao;
import com.cupdata.notify.utils.NotifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @Auth: liwei
 * @Description:
 * @Date: 15:23 2018/1/11
 */

@Service
public class NotifyBiz extends BaseBiz<OrderNotifyWait> {

    protected static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NotifyBiz.class);

    @Autowired
    private OrderNotifyCompleteDao orderNotifyCompleteDao;
    
    @Autowired
    private OrderNotifyWaitDao orderNotifyWaitBiz;

    @Override
    public BaseDao<OrderNotifyWait> getBaseDao() {
        return orderNotifyWaitBiz;
    }
    
    public void orderNotifyCompleteInsert(OrderNotifyComplete orderNotifyComplete) {
    	orderNotifyCompleteDao.insert(orderNotifyComplete);
    }
    
	public void notifyToOrg3Times(VoucherOrderVo voucherOrderVo, OrgInfVo orgInfVo) {
			String str ="";
			//发送通知  先发送3次通知 
			for(int i=0;i<3;i++){
				if(NotifyUtil.httpToOrg(voucherOrderVo,orgInfVo)) {
					//通知成功    初始  OrderNotifyComplete 保存数据库
					OrderNotifyComplete orderNotifyComplete = NotifyUtil.initOrderNotifyComplete(voucherOrderVo.getOrder().getOrderNo(),voucherOrderVo.getVoucherOrder().getQrCodeUrl());
                    orderNotifyCompleteDao.insert(orderNotifyComplete);
                    return;
                }
            }
        //通知失败    初始  OrderNotifyWait 保存数据库
        OrderNotifyWait orderNotifyWait = NotifyUtil.initOrderNotifyWait(voucherOrderVo.getOrder().getOrderNo(),voucherOrderVo.getVoucherOrder().getQrCodeUrl());
        orderNotifyWaitBiz.insert(orderNotifyWait);
    }

    /**
     * 充值业务通知
     * @param serviceOrder
     * @param orgInfVo
     */
	public void rechargeNotifyToOrg3Times(ServiceOrder serviceOrder, OrgInfVo orgInfVo) {
		String str ="";
		//发送通知  先发送3次通知
        for(int i=0;i<3;i++){
            if(NotifyUtil.rechargeHttpToOrg(serviceOrder,orgInfVo)) {
                //通知成功    初始  OrderNotifyComplete 保存数据库
                log.info("通知成功");
                OrderNotifyComplete orderNotifyComplete = NotifyUtil.initOrderNotifyComplete(serviceOrder.getOrderNo(),serviceOrder.getNotifyUrl());
                orderNotifyCompleteDao.insert(orderNotifyComplete);
                return;
            }
        }
        log.info("通知失败");
        //通知失败    初始  OrderNotifyWait 保存数据库
		OrderNotifyWait orderNotifyWait = NotifyUtil.initOrderNotifyWait(serviceOrder.getOrderNo(),serviceOrder.getNotifyUrl());
		orderNotifyWaitBiz.insert(orderNotifyWait);
	}

}
