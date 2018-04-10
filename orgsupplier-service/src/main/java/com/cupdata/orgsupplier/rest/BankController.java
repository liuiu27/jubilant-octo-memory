//package com.cupdata.orgsupplier.controller;
//
//import com.cupdata.commons.api.orgsupplier.IBankController;
//import com.cupdata.commons.constant.ResponseCodeMsg;
//import com.cupdata.commons.exception.ErrorException;
//import com.cupdata.commons.model.BankInf;
//import com.cupdata.commons.vo.BaseResponse;
//import com.cupdata.commons.vo.orgsupplier.BankInfListVo;
//import com.cupdata.commons.vo.orgsupplier.BankInfVo;
//import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
//import com.cupdata.orgsupplier.biz.BankInfBiz;
//
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @Auth: LinYong
// * @Description:
// * @Date: 14:56 2017/12/20
// */
//@Slf4j
//@RestController
//public class BankController implements IBankController{
//
//	@Autowired
//	private BankInfBiz bankInfBiz;
//
//    @Override
//    public BaseResponse<BankInfVo> findBankByBankCode(@PathVariable("bankCode") String bankCode) {
//    	log.info("BankController findBankByBankCode is begin bankCode is" + bankCode);
//    	try {
//	    	Map<String, Object> paramMap = new HashMap<>();
//			paramMap.put("bankCode", bankCode);
//			BankInf bankInf = bankInfBiz.selectSingle(paramMap);
//			//返回响应报文
//			BaseResponse<BankInfVo> bankInfVoRes = new BaseResponse<>();
//			BankInfVo bankInfVo = new BankInfVo();
//			bankInfVo.setBankInf(bankInf);
//			bankInfVoRes.setData(bankInfVo);
//			return bankInfVoRes;
//    	 } catch (Exception e) {
// 			log.error("error is " + e.getMessage());
// 			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
// 		}
//    }
//
//	@Override
//	public BaseResponse<BankInfListVo> selectAll() {
//		log.info("BankController selectAll is begin");
//		try {
//			List<BankInf> bankInfList = bankInfBiz.selectAll(null);
//			//返回响应报文
//			BaseResponse<BankInfListVo> bankInfListVoRes = new BaseResponse<>();
//			BankInfListVo bankInfListVo = new BankInfListVo();
//			bankInfListVo.setBankInfList(bankInfList);
//			bankInfListVoRes.setData(bankInfListVo);
//			return bankInfListVoRes;
//		 } catch (Exception e) {
// 			log.error("error is " + e.getMessage());
// 			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
//	 	}
//	}
//}
