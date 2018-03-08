package com.cupdata.ihuyi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DingCong
 * @Description:
 * @CreateDate: 2018/3/8 13:13
 */

@Slf4j
@Controller
@RequestMapping("/ihuyiRecharge")
public class ihuyiRecharge {

    @PostMapping("/orgTest")
    public String orgTest(@RequestParam(value="data", required=true) String data, @RequestParam(value="sign", required=true) String sign) {
        log.info("模拟机构接口，SIP将充值结果发送给机构");
        log.info("data数据为："+data);
        return "SUCCESS";
    }
}
