package com.cupdata.content.controller;

import com.cupdata.content.exception.ContentException;
import com.cupdata.content.vo.request.PayPageVO;
import com.cupdata.content.vo.request.TestSupVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @PostMapping("/testOne")
    public String testOne(){
        PayPageVO a =new PayPageVO();
        TestSupVO<PayPageVO> b =new TestSupVO<>();
        b.setData(a);

        throw new ContentException();
        //return "error";
       // return "redirect:http://www.oschina.net";
    }



}
