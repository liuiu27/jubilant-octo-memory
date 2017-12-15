package com.cupdata.order.biz;


import com.cupdata.commapi.biz.BaseBiz;
import com.cupdata.order.dao.ServiceOrderDao;
import com.cupdata.order.domain.ServiceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderBiz extends BaseBiz<ServiceOrder> {


}
