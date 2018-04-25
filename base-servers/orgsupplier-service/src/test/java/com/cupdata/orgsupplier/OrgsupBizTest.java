package com.cupdata.orgsupplier;

import com.cupdata.orgsupplier.biz.ServiceSupplierBiz;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Tony
 * @date 2018/04/09
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrgsupBizTest {

    @Autowired
    private ServiceSupplierBiz biz;

    @Test
    public void test() {

        SupplierInfVo supByNo = biz.findSupByNo("2018010500001234");

        log.info(supByNo.getSupplierNo());
        Assert.assertEquals(supByNo.getSupplierNo(), "2018010500001234");

    }

}
