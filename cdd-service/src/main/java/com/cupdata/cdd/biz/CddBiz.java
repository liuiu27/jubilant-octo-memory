package com.cupdata.cdd.biz;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.cdd.requset.CddCodeReq;
import com.cupdata.sip.common.api.cdd.response.CddCodeRes;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.HttpUtil;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年1月29日 下午3:11:45
*/
@Slf4j
@Service
public class CddBiz {
	
	/**
	 * 获取券码
	 */
	public BaseResponse<CddCodeRes> getVoucherCode(CddCodeReq cddCodeReq,String url) {
		log.info("getVoucherCode is begin...params url is " + url + "cddCodeReq is " + cddCodeReq.toString());
		BaseResponse<CddCodeRes> res = new  BaseResponse<CddCodeRes>();
		try {
			String params = "ApiKey=" + cddCodeReq.getApiKey() + "&ApiST=" + cddCodeReq.getApiST()
			+ "&ApiSign=" + cddCodeReq.getApiSign() + "&Mobile=" + cddCodeReq.getMobile()
			+ "&sn=" + cddCodeReq.getSn() + "&PackageID=" + cddCodeReq.getPackageID()
			+ "&OpenCode=" + cddCodeReq.getOpenCode() + "&Num=" + cddCodeReq.getNum()
			+ "&AgencyID=" + cddCodeReq.getAgencyID() + "&OrderType=" + cddCodeReq.getOrderType();
			log.info("requestUrl is " + url);
			String resStr = HttpUtil.doPost(url + "?" + params, "");
			log.info("result is " + resStr);
			//参数格式转换
			resStr = resStr.replaceAll("Status", "status").replaceAll("Msg", "msg")
					.replaceAll("BatchId","batchId ").replaceAll("Sn", "sn")
					.replaceAll("PList", "pList").replaceAll("Pid", "pid")
					.replaceAll("YzmList", "yzmList").replaceAll("Yzm", "yzm")
					.replaceAll("TicketId", "ticketId").replaceAll("DateBeginTime", "dateBeginTime")
					.replaceAll("DateOutTime", "dateOutTime")
					.replaceAll("PackageId", "packageId");
			JSONObject jsonStr = JSONObject.parseObject(resStr);
			
			if(!"200".equals(jsonStr.get("status").toString())&&!"201".equals(jsonStr.get("status").toString())
					&&!"202".equals(jsonStr.get("status").toString())){
				log.error("Call CDD interface is error reqstr is" + resStr);
				res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
				res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			}else {
				JSONArray pListArray = (JSONArray) JSONArray.parseArray(jsonStr.getString("pList"));
				JSONObject pListObject = JSONObject.parseObject(pListArray.get(0).toString());
				JSONArray yzmListArray = (JSONArray) JSONArray.parseArray(pListObject.getString("yzmList"));
				JSONObject yzmListObject = JSONObject.parseObject(yzmListArray.get(0).toString());
				CddCodeRes cddCodeRes = new CddCodeRes();
				cddCodeRes.setSn(jsonStr.get("sn").toString());
				cddCodeRes = yzmListObject.toJavaObject(CddCodeRes.class);
				res.setData(cddCodeRes);
			}
			
		} catch (Exception e) {
			log.error("error msg is " + e.getMessage());
			res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
		return res;
	}
//	public static void main(String[] args) {
//		CddCodeReq cddCodeReq = new CddCodeReq();
//		cddCodeReq.setApiKey("e504fa8ec6a242f09f983557f3c72bfc");
//		cddCodeReq.setMobile(CddUtil.aesUrlEncode("18611111111", "b55c936cd48048499ffb5d71227711f8"));
//		cddCodeReq.setApiST(DateTimeUtil.getTenTimeStamp());
//		cddCodeReq.setApiSign("e504fa8ec6a242f09f983557f3c72bfc" + "b55c936cd48048499ffb5d71227711f8" + cddCodeReq.getApiST());
//		cddCodeReq.setSn("123456791118");
//		cddCodeReq.setPackageID("13718");
//		cddCodeReq.setOpenCode("ZJ");
//		cddCodeReq.setNum("1");
//		cddCodeReq.setAgencyID("3513");
//		cddCodeReq.setOrderType("0");
//		CddBiz biz = new CddBiz();
//		biz.getVoucherCode(cddCodeReq, "http://testorg.chediandian.com/api/ticket/SendGift");
//	}
}
