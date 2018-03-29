package com.cupdata.ihuyi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class givenOrg {

    @RequestMapping("/givenOrg")
    public void simulateOrgInterface(HttpServletRequest req){

        log.info("本地模拟通机构接口.....");
        String data = req.getParameter("data");
        log.info(data);
    }


}
