package com.cupdata.sip.bestdo;

import com.cupdata.sip.bestdo.biz.BestdoBiz;
import com.cupdata.sip.bestdo.vo.response.MerInfoRes;
import com.cupdata.sip.common.dao.entity.ServiceSupplier;
import com.cupdata.sip.common.dao.mapper.ServiceSupplierMapper;
import com.cupdata.sip.common.dao.mapper.TestMapper;
import com.cupdata.sip.common.lang.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Tony
 * @date 2018/04/09
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BestdoBizTest {


    @Autowired
    private BestdoBiz bestdoBiz;


    @Test
    public void getMerLists() {

        List<MerInfoRes> merLists = bestdoBiz.getMerLists().getData();

        log.info(merLists.size()+"");

    }

    @Autowired
    private TestMapper testMapper;

    @Test
    public void test() {

        com.cupdata.sip.common.dao.entity.Test a = new com.cupdata.sip.common.dao.entity.Test();

        EntityUtils.setEntityInfo(a,EntityUtils.cfields);
        testMapper.insert(a);

    }

    @Autowired
    private ServiceSupplierMapper serviceSupplierDao;


    @Test
    public void test2() {
        ServiceSupplier supByNo = serviceSupplierDao.findSupByNo("2018010500001234");
        log.info(supByNo.getSupplierNo());

    }


}
