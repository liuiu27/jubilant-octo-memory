package com.cupdata.product.biz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TripBiz {


    @Autowired
    private RestTemplate restTemplate;


    public void doGet(String url) {

        //发送请求
        //String ret= restTemplate.getForObject(url+"?a=[a]&b=[b]",String.class,"1",2);

    }
}
