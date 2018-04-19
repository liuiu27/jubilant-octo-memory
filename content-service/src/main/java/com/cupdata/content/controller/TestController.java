package com.cupdata.content.controller;

import com.cupdata.content.vo.request.PayPageVO;
import com.cupdata.content.vo.request.SupVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/testOne")
    public String testOne(){
        PayPageVO a =new PayPageVO();
        SupVO<PayPageVO> b =new SupVO<>();
        b.setData(a);

        //throw new ContentException();
        return "error.html";
       // return "redirect:http://www.oschina.net";
    }



}
