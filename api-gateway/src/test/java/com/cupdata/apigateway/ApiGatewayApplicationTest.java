package com.cupdata.apigateway;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


//下面两个注解用于标记为springboot 的测试，需要初始化spring上下文环境
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiGatewayApplicationTest {


    @Test
    public  void test(String[] args) {
        Integer a=127,b=127;
        System.out.println(a==b);
        Integer c=124,d=124;
        System.out.println(c==d);

        int s=128,f=128;
        System.out.println(s==f);

    }

}
