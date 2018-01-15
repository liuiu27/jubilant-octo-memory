package com.cupdata.notify.biz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.notify.dao.OrderNotifyCompleteDao;
import com.cupdata.notify.dao.OrderNotifyWaitDao;
import com.cupdata.notify.utils.NotifyUtil;
/**
 * @Auth: liwei
 * @Description:
 * @Date: 15:23 2018/1/11
 */

@Service
public class NotifyBiz extends BaseBiz<OrderNotifyWait> {
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
    
	public void notifyToOrg(OrderNotifyWait orderNotifyWait) {
		//查询是否有正在等待的通知
		OrderNotifyWait orderNotifyWaitVo = orderNotifyWaitBiz.select(orderNotifyWait);
		//为空则没有  向机构发送通知  不为空则等待任务调度器 通知
		if(null == orderNotifyWaitVo) {
			String str ="";
			//发送通知  先发送3次通知  
			for(int i=0;i<3;i++){
				str = NotifyUtil.httpToOrg(orderNotifyWaitVo);
				if(!StringUtils.isBlank(str)) {
					//通知成功    初始  OrderNotifyComplete 保存数据库
					OrderNotifyComplete orderNotifyComplete = NotifyUtil.initOrderNotifyComplete(orderNotifyWait);
					orderNotifyCompleteDao.insert(orderNotifyComplete);
					return;
				}
			}
			//通知失败    初始  OrderNotifyWait 保存数据库
			orderNotifyWait = NotifyUtil.initOrderNotifyWait(orderNotifyWait);
			orderNotifyWaitBiz.insert(orderNotifyWait);
		}
	}

}
