package com.cupdata.content.controller;

import com.cupdata.content.vo.PayPageRequestVO;
import com.cupdata.content.vo.SupVO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

	@ResponseBody
    @GetMapping("/testOne")
    public Object testOne(){
        PayPageRequestVO a =new PayPageRequestVO();
        SupVO<PayPageRequestVO> b =new SupVO<>();
        b.setData(a);

        return b;
    }


    @ResponseBody
    @PostMapping(name="/two",produces = "application/json")
    public Object testTwo(@RequestBody @Validated SupVO<PayPageRequestVO> requestVOSupVO){


        return  "qqq";
    }

}
