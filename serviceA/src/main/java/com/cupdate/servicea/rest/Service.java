package com.cupdate.servicea.rest;

import com.cupdata.commapi.ServiceA;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Service implements ServiceA {

    @Override
    public String hello() {
        return "hello A";
    }
}
