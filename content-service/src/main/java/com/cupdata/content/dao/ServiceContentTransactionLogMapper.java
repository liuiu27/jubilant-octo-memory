package com.cupdata.content.dao;

import com.cupdata.content.domain.ServiceContentTransactionLog;
import java.util.List;

public interface ServiceContentTransactionLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServiceContentTransactionLog record);

    ServiceContentTransactionLog selectByPrimaryKey(Long id);

    List<ServiceContentTransactionLog> selectAll();

    int updateByPrimaryKey(ServiceContentTransactionLog record);

    ServiceContentTransactionLog selectByTranNo(String tranNo);

}