package com.cupdata.content.dao;

import com.cupdata.content.domain.ServiceOrderContent;
import java.util.List;

public interface ServiceOrderContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServiceOrderContent record);

    ServiceOrderContent selectByPrimaryKey(Long id);

    List<ServiceOrderContent> selectAll();

    int updateByPrimaryKey(ServiceOrderContent record);
}