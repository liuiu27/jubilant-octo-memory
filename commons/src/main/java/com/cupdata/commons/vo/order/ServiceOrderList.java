package com.cupdata.commons.vo.order;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.vo.BaseData;
import lombok.Data;

import java.util.List;

/**
 * @Author: DingCong
 * @Description:
 * @CreateDate: 2018/3/14 9:52
 */

@Data
public class ServiceOrderList extends BaseData {

    private List<ServiceOrder> serviceOrderList;
}
