package com.cupdata.sip.bestdo;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tony
 * @date 2018/03/30
 */
@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class BestdoServiceTest {


    private final static String TEST_URL = "http://test.cupd.bestdo.com";

    //@Autowired
    //@Qualifier("restTemplate")
    private RestTemplate restTemplate;


    @Test
    public void testMerDetail() {
        //merItemId
        //1020125	高尔夫练习场
        //1020279	羽毛球
        //1020102	银联数据-健身
        //1020105	银联数据-网球
        //1020103	银联数据-游泳
        //1020107	银联数据-星级酒店游泳
        restTemplate =new RestTemplate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("source", "CUPD");
        jsonObject.put("tradeCode", "VENUElIST");
        jsonObject.put("merItemId", "1020125");

        String ret = restTemplate.getForObject(TEST_URL + "/mer/item/detail/merDetail?merItemInfo={merItemInfo}", String.class, jsonObject.toJSONString());
        log.info(ret);

    }


}
