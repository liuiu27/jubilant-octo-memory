package com.cupdata.content.controller;

import com.cupdata.content.vo.request.PayPageVO;
import com.cupdata.content.vo.request.SupVO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class TestController {


   // @ResponseBody
    @GetMapping("/testOne")
    public Object testOne(){
        PayPageVO a =new PayPageVO();
        SupVO<PayPageVO> b =new SupVO<>();
        b.setData(a);

        return "redirect:http://www.oschina.net";
    }


    @ResponseBody
    @PostMapping(path="/two",produces = "application/json")
    public Object testTwo(@RequestBody @Validated SupVO<PayPageVO> requestVOSupVO){


        return  "success!";
    }

    @PostMapping(path="/payRequest",produces = "application/json")
    public String payRequest(@RequestBody @Validated PayPageVO payPageVO){




        return "";
    }

}
