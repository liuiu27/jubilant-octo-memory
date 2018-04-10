//package com.cupdata.orgsupplier.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cupdata.commons.api.orgsupplier.IOrgController;
//import com.cupdata.commons.constant.ResponseCodeMsg;
//import com.cupdata.commons.exception.ErrorException;
//import com.cupdata.commons.vo.BaseResponse;
//import com.cupdata.commons.vo.orgsupplier.OrgInfListVo;
//import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
//import com.cupdata.orgsupplier.biz.OrgInfBiz;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @Auth: LinYong
// * @Description:机构相关的controller
// * @Date: 14:50 2017/12/20
// */
//@Slf4j
//@RestController
//public class OrgController implements IOrgController {
//	@Autowired OrgInfBiz orgInfBiz;
//
//    @Override
//    public BaseResponse<OrgInfVo> findOrgByNo(@PathVariable("orgNo") String orgNo) {
//    	log.info("OrgController findOrgByNo is begin ,param  orgNo is" + orgNo);
//    	try {
//    		return orgInfBiz.findOrgByNo(orgNo);
//    	} catch (Exception e) {
//			log.error("error is " + e.getMessage());
//			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
//		}
//
//    }
//
//	@Override
//	public BaseResponse<OrgInfListVo> selectAll() {
//		log.info("OrgController selectAll is begin");
//		try {
//			BaseResponse<OrgInfListVo> orgInfListVoRes = new BaseResponse();
//			OrgInfListVo orgInfListVo = new OrgInfListVo();
//			orgInfListVo.setOrgInfList(orgInfBiz.selectAll(null));
//			orgInfListVoRes.setData(orgInfListVo);
//			return orgInfListVoRes;
//		} catch (Exception e) {
//			log.error("error is " + e.getMessage());
//			throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
//		}
//	}
//}
