package com.cupdata.content.controller;

import com.cupdata.content.vo.PayPageRequestVO;
import com.cupdata.content.vo.SupVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {



    @GetMapping("/testOne")
    public Object testOne(){
        PayPageRequestVO a =new PayPageRequestVO();
        SupVO<PayPageRequestVO> b =new SupVO<>();
        b.setData(a);

        return b;

    }


}
