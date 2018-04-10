package com.cupdata.orgsupplier.biz;

import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.dao.entity.ServiceSupplier;
import com.cupdata.sip.common.dao.mapper.ServiceSupplierMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Slf4j
@Service
public class ServiceSupplierBiz  {

    @Autowired
    private ServiceSupplierMapper serviceSupplierDao;


    public List<SupplierInfVo> selectAll(){

        List<ServiceSupplier> serviceSuppliers = serviceSupplierDao.selectAll();



        List<SupplierInfVo> supplierInfVos = new ArrayList<>(serviceSuppliers.size());

        serviceSuppliers.forEach(supplier -> {
            SupplierInfVo supplierInfVo =new SupplierInfVo();
            BeanCopierUtils.copyProperties(supplier,supplierInfVo);
            supplierInfVos.add(supplierInfVo);
        });

        return supplierInfVos;

    }

    public SupplierInfVo findSupByNo(String supplierNo) {
        ServiceSupplier serviceSupplier = serviceSupplierDao.findSupByNo(supplierNo);

        SupplierInfVo supplierInfVo =new SupplierInfVo();
        BeanCopierUtils.copyProperties(serviceSupplier,supplierInfVo);

        return supplierInfVo;
    }
}
