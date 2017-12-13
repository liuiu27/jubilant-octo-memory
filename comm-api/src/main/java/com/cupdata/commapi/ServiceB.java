package com.cupdata.commapi;


import org.springframework.web.bind.annotation.GetMapping;


public interface ServiceB {

    @GetMapping("/hello")
    String hello();

}
