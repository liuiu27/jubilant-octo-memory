package com.cupdata.ihuyi.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.vo.notify.OrderNotifyComplete;
import com.cupdata.commons.vo.notify.OrderNotifyWait;
import com.cupdata.ihuyi.dao.OrderNotifyCompleteDao;
import com.cupdata.ihuyi.dao.OrderNotifyWaitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: DingCong
 * @Description:
 * @Date: 9:23 2018/4/11
 */

@Service
public class NotifyBiz extends BaseBiz<OrderNotifyWait> {

    protected static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NotifyBiz.class);

    @Autowired
    private OrderNotifyCompleteDao orderNotifyCompleteDao;
    
    @Autowired
    private OrderNotifyWaitDao orderNotifyWaitDao;

    @Override
    public BaseDao<OrderNotifyWait> getBaseDao() {
        return null;
    }

    /**
     * 将通知数据添加至Complete表中
     * @return
     */
    public void addNotifyComplete(OrderNotifyComplete notifyComplete){
        log.info("将通知数据添加至NotifyComplete表中");
        Long l = orderNotifyCompleteDao.insert(notifyComplete);
        log.info("添加数据条数:"+l);
    }

    /**
     * 将通知数据添加至Notify表中
     * @return
     */
   public void addNotifyWait(OrderNotifyWait orderNotifyWait){
       log.info("通知数据添加至Notify_wait表");
       Long l = orderNotifyWaitDao.insert(orderNotifyWait);
       log.info("添加数据条数:"+l);
    }



}
