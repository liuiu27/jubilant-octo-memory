package com.cupdata.trvok.biz;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.cdd.CddCodeReq;
import com.cupdata.commons.vo.cdd.CddCodeRes;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年1月29日 下午3:11:45
*/
@Slf4j
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
			resStr.replaceAll("Status", "status");
			resStr.replaceAll("Msg", "msg ");
			resStr.replaceAll("BatchId ", "batchId ");
			resStr.replaceAll("Sn", "sn");
			resStr.replaceAll("PList", "pList");
			resStr.replaceAll("Pid", "pid");
			resStr.replaceAll("YzmList", "yzmList");
			resStr.replaceAll("Yzm", "yzm");
			resStr.replaceAll("TicketId", "ticketId");
			resStr.replaceAll("DateBeginTime", "dateBeginTime");
			resStr.replaceAll("DateOutTime", "dateOutTime");
			JSONObject resJson = JSONObject.parseObject(resStr);
			CddCodeRes cddCodeRes = new CddCodeRes();
			cddCodeRes = resJson.toJavaObject(CddCodeRes.class);
			res.setData(cddCodeRes);
			if(!"200".equals(cddCodeRes.getStatus())) {
				log.error("Call CDD interface is error reqstr is" + resStr);
			}
		} catch (Exception e) {
			log.error("error msg is " + e.getMessage());
			res.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			res.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
		}
		return res;
	}
	public static void main(String[] args) {
//		CddCodeReq cddCodeReq = new CddCodeReq();
//		cddCodeReq.setApiKey(APIKEY);
//		cddCodeReq.setMobile(CddUtil.aesUrlEncode("18611111111", APISECRET));
//		cddCodeReq.setApiST(DateTimeUtil.getTenTimeStamp());
//		cddCodeReq.setApiSign(APIKEY + APISECRET + cddCodeReq.getApiST());
//		cddCodeReq.setSn("123456791118");
//		cddCodeReq.setPackageID("13718");
//		cddCodeReq.setOpenCode("ZJ");
//		cddCodeReq.setNum("1");
//		cddCodeReq.setAgencyID("3513");
//		cddCodeReq.setOrderType("0");
//		getVoucherCode(cddCodeReq);
	}
}
