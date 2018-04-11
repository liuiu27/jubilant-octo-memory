package com.cupdata.sip.bestdo;

import com.cupdata.sip.bestdo.feign.SupplierFeignClient;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.api.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Tony
 * @date 2018/04/11
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Testfeign {

    @Autowired
    private SupplierFeignClient supplierFeignClient;

    @Test
    public void test() {

        BaseResponse<SupplierInfVo> sup = supplierFeignClient.findSupByNo("20180409S23333376");

        SupplierInfVo data = sup.getData();

        log.info(data.getSupplierNo());

    }


}
