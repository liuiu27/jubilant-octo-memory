package com.cupdata.commapi;


import org.springframework.web.bind.annotation.GetMapping;


public interface ServiceA {

    @GetMapping("/hello")
    String hello();

}
