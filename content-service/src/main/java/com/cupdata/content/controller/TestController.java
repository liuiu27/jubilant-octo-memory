//package com.cupdata.content.controller;
//
//import com.cupdata.commons.vo.BaseResponse;
//import com.cupdata.content.biz.SupplierBiz;
//import com.cupdata.content.domain.ServiceContentTransactionLog;
//import com.cupdata.content.vo.request.PayPageVO;
//import com.cupdata.content.vo.request.SupVO;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.view.RedirectView;
//
//import javax.annotation.Resource;
//
//@Controller
//@RequestMapping("/test")
//public class TestController {
//
//    @Resource
//    private SupplierBiz supplierBiz;
//
//    private static BaseResponse baseResponse = new BaseResponse();
//
//
//   // @ResponseBody
//    @GetMapping("/testOne")
//    public Object testOne(){
//        PayPageVO a =new PayPageVO();
//        SupVO<PayPageVO> b =new SupVO<>();
//        b.setData(a);
//
//        return "redirect:http://www.oschina.net";
//    }
//
//
//    @ResponseBody
//    @PostMapping(path="/two",produces = "application/json")
//    public Object testTwo(@RequestBody @Validated SupVO<PayPageVO> requestVOSupVO){
//        RedirectView redirectView = new RedirectView();
//
//        return  "success!";
//    }
//
//    @PostMapping(path="/payRequest",produces = "application/json")
//    public String payRequest(@RequestBody @Validated SupVO<PayPageVO> requestVOSupVO){
//
//        //Step1 验证本流水是否有效
//        ServiceContentTransactionLog transactionLog = supplierBiz.checkTransaction(requestVOSupVO.getTranNo());
//
//        //Step2 验证是否有对应交易订单。并对其进行检验。
//        //Step3 创建交易订单，并保存参数
//        supplierBiz.createContentOrder(requestVOSupVO.getTranNo());
//
//        //Step4 获取对应的支付接口。
//        String sourcePath =supplierBiz.getSourcePath(requestVOSupVO.getTranNo());
//
//        //Step5 拼接参数。
//
//        StringBuffer ret = new StringBuffer("redirect:");
//        ret.append(sourcePath).append("?");
//        return ret.toString();
//    }
//
//
//    @PostMapping("/cancelPay")
//    public BaseResponse cancelPay(){
//
//
//
//        return baseResponse;
//    }
//
//}
