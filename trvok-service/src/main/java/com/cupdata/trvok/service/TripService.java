package com.cupdata.trvok.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.MD5Util;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.trvok.TrovkActivatReq;
import com.cupdata.commons.vo.trvok.TrovkActivatRes;
import com.cupdata.commons.vo.trvok.TrovkCodeReq;
import com.cupdata.commons.vo.trvok.TrovkCodeRes;
import com.cupdata.commons.vo.trvok.TrovkDisableReq;
import com.cupdata.commons.vo.trvok.TrovkDisableRes;
import com.cupdata.commons.vo.trvok.TrvokAirportReq;
import com.cupdata.commons.vo.trvok.TrvokAirportRes;
import com.cupdata.commons.vo.trvok.TrvokAirportRes.LoungeDetail;
import com.cupdata.commons.vo.trvok.TrvokAreaReq;
import com.cupdata.commons.vo.trvok.TrvokAreaRes;
import com.cupdata.commons.vo.trvok.TrvokAreaRes.AirportSummary;
import com.cupdata.trvok.utils.TripUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TripService {
    
	
	//TODO  以下部分变量需要从数据库中查询获取
    private static String KONGGANG_AREA_SIGN_KEY = "AS-DF-KA-11-07-D1";//获取空港区域秘钥
 	private static String KONGGANG_REQUST_URL = "http://apitest.airportcip.com:89/";//空港请求区域信息URL
 	private static String KONGGANG_IMG_URL = "http://api.airportcip.com:8180/upload/"; //图片路径
 	private static String KONGGANG_PARTNER = "1993";	//合作方ID
 	private static String KONGGANG_WCF_SIGN_KEY = "A4-DF-A0-11-07-D5";//WC秘钥
    
	/**
	 * 获取空港区域信息
	 * @param type
	 * @return
	 */
	public BaseResponse<TrvokAreaRes> getTrvokArea(TrvokAreaReq trvokAreaReq){
		log.info("begin execut getTrvokArea.......param : type is " + trvokAreaReq.getAreaType());
		BaseResponse<TrvokAreaRes> baseResponse = new BaseResponse<TrvokAreaRes>();
		try {
			String params = "random=" + TripUtil.getRandom() + "&type=" + trvokAreaReq.getAreaType();
			String sign = MD5Util.md5Encode(params + KONGGANG_AREA_SIGN_KEY);//获取空港区域秘钥
			String requstUrl = KONGGANG_REQUST_URL + 
			"report.svc/SearchAreaInfo?" + params + "&sign=" + sign;//空港请求区域信息URL
			log.info("request url is " + requstUrl);
			//GET请求空港获取区域接口 
			String resStr = HttpUtil.doGet(requstUrl);
			log.info("response information is " + resStr);
			JSONObject resJson = JSONObject.parseObject(resStr);
			if("0".equals(resJson.getString("result"))){//result为0，则获取data区域信息
				 String data = resJson.getString("data");
				 //字段名称转换
				 data = data.replaceAll("\"AirportId\"","\"airportId\"");
				 data = data.replaceAll("\"AirportName\"","\"airportName\"");
				 data = data.replaceAll("\"Code\"","\"code\"");
				 TrvokAreaRes trvokAreaRes = new TrvokAreaRes();
				 List<AirportSummary> list = JSONObject.parseArray(data, AirportSummary.class);  
				 trvokAreaRes.setAirportList(list);
				 baseResponse.setData(trvokAreaRes);
				 baseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
				 baseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
				 return baseResponse;
			}else{
				log.error("request konggang getTrvokArea error is " + resJson.getString("description"));
				baseResponse.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return baseResponse;
			}
		}catch(Exception e){
			log.error("request konggang getTrvokArea error is " + e.getMessage());
			baseResponse.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			baseResponse.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return baseResponse;
		}
	}
	
	/**
	 * 根据    机场编号获   和 休息室类型 取机场详情
	 * @param airportId  type
	 * @return
	 */
	public BaseResponse<TrvokAirportRes> getTrvokAirportInfo(TrvokAirportReq trvokAirportReq){
		log.info("begin execut getTrvokAirportInfo.......param ： type is" 
				+ trvokAirportReq.getAreaType() + "airportId is " + trvokAirportReq.getAirportId());
		BaseResponse<TrvokAirportRes> baseResponse = new BaseResponse<TrvokAirportRes>();
		try {
			String params = "random=" + TripUtil.getRandom() + "&type=" + trvokAirportReq.getAreaType()
			+ "&airport_id=" + trvokAirportReq.getAirportId();
			String sign = MD5Util.md5Encode(params + KONGGANG_AREA_SIGN_KEY);//KONGGANG_AREA_SIGN_KEY 获取空港区域秘钥
			String requstUrl = KONGGANG_REQUST_URL + 
			"report.svc/SearchAreaInfoById?" + params + "&sign=" + sign;//空港请求机票详情URL
			log.info("request url is " + requstUrl);
			//GET请求空港获取机票详情接口 
			String resStr = HttpUtil.doGet(requstUrl);
			log.info("response information is " + resStr);
			JSONObject resJson = JSONObject.parseObject(resStr);
			if("0".equals(resJson.getString("result"))){//result为0，则获取data机票详情
				 TrvokAirportRes airportInfoRes = new TrvokAirportRes();
				 List<LoungeDetail> LoungeDetailList = new ArrayList<LoungeDetail>(); 
				 String imgSrc = KONGGANG_IMG_URL;
				 resStr = resJson.getString("data");
				 JSONArray json = JSONArray.parseArray(resStr);
				 if(json.size()>0){
				   for(int i=0;i<json.size();i++){
				     JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组  转换为list集合
				     LoungeDetail LoungeDetail =  new LoungeDetail();
				     LoungeDetail.setServices(job.getString("services"));
				     LoungeDetail.setAreaName(job.getString("area_name"));
				     LoungeDetail.setAirportId(job.getString("Airport_id"));
				     LoungeDetail.setAirportName(job.getString("Airport_Name"));
				     LoungeDetail.setAirportTerminal(job.getString("Airport_Terminal"));
				     LoungeDetail.setLoungeAddress(job.getString("Lounge_Address"));
				     LoungeDetail.setLoungeDesc(job.getString("Lounge_Desc"));
				     LoungeDetail.setHoursSection(job.getString("Hours_section"));
				     LoungeDetail.setRegulations(job.getString("Regulations"));
				     LoungeDetail.setFacilities(job.getString("Facilities"));
				     LoungeDetail.setImage(imgSrc + job.getString("Image"));
				     LoungeDetail.setDeparturePort(job.getString("Departure_Port"));
				     LoungeDetail.setRegion(job.getString("Region"));
				     LoungeDetail.setRecentlyGate(job.getString("Recently_gate"));
				     LoungeDetail.setEnabled(job.getString("enabled"));
				     LoungeDetail.setImage2(imgSrc + job.getString("Image2"));
				     LoungeDetail.setImage3(imgSrc + job.getString("Image3"));
				     LoungeDetail.setImage4(imgSrc + job.getString("Image4"));
				     LoungeDetail.setImage5(imgSrc + job.getString("Image5"));
				     LoungeDetail.setInternational(job.getString("international"));
				     LoungeDetailList.add(LoungeDetail);
				   }
				 }
				 airportInfoRes.setAirportInfo(LoungeDetailList);
				 baseResponse.setData(airportInfoRes);
				 baseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
				 baseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
				 return baseResponse;
			}else{
				log.error("request konggang getTrvokAirportInfo error is " + resJson.getString("description"));
				baseResponse.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return baseResponse;
			}
		}catch(Exception e){
			log.error("request konggang getTrvokAirportInfo error is " + e.getMessage());
			baseResponse.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			baseResponse.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return baseResponse;	
		}
	}
	
	/**
	 *  根据   产品标识符   和   银行订单号   获取券码
	 * @param  商户网站唯一订单号 outTradeNo  产品标识符 sku
	 * @return
	 */
	public BaseResponse<TrovkCodeRes> getTrvokVerifyCode(TrovkCodeReq trovkCodeReq){
		log.info("begin execut getVerifyCode.......param ： outTradeNo  is" 
				+ trovkCodeReq.getOutTradeNo() + "sku is " + trovkCodeReq.getSku());
		BaseResponse<TrovkCodeRes> baseResponse =  new BaseResponse<TrovkCodeRes>();
		try {
			String params = "out_trade_no=" + trovkCodeReq.getOutTradeNo() + "&partner=" + //KONGGANG_PARTNER 获取空港签约合作方身份ID
					KONGGANG_PARTNER + "&sku=" + trovkCodeReq.getSku();
			String sign = MD5Util.md5Encode(params + KONGGANG_WCF_SIGN_KEY);//KONGGANG_WCF_SIGN_KEY 获取空港WCF密钥
			String requstUrl = KONGGANG_REQUST_URL + 
			"API/YinLian/YinLianPD.svc/add?" + params + "&sign=" + sign;//空港请求机票详情URL
			log.info("request url is " + requstUrl);
			//GET请求获取空港服务券码
			String resStr = HttpUtil.doGet(requstUrl);
			log.info("response information is " + resStr);
			JSONObject resJson = JSONObject.parseObject(resStr);
			if("00".equals(resJson.getString("code"))){//result为0，则获取data机票详情
				 TrovkCodeRes trovkCodeRes = new TrovkCodeRes();
				 trovkCodeRes.setVerifyCode( resJson.getString("verify_code"));
				 baseResponse.setData(trovkCodeRes);
				 baseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
				 baseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
				 return baseResponse;
			}else{
				log.error("request konggang getVerifyCode error is " + resJson.getString("info"));
				baseResponse.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return baseResponse;
			}
		}catch(Exception e){
			log.error("request konggang getVerifyCode error is " + e.getMessage());
			baseResponse.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			baseResponse.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return baseResponse;	
		}	
	}
	
	/**
	 * 根据券码调用空港接口激活券码
	 * @param verifyCode
	 * @return
	 */
	public BaseResponse<TrovkActivatRes> activationTrvokCode(TrovkActivatReq activatCodeReq){
		log.info("begin execut activationCode.......param ： verifyCode  is" + activatCodeReq.getVerifyCode() + ", expire is " + activatCodeReq.getExpire());
		BaseResponse<TrovkActivatRes> baseResponse = new BaseResponse<TrovkActivatRes>();
		TrovkActivatRes activatCodeRes = new TrovkActivatRes();
		try {
			String params = "partner=" + KONGGANG_PARTNER + //KONGGANG_PARTNER 获取空港签约合作方身份ID
			"&verify_code=" + activatCodeReq.getVerifyCode() + "&expire=" + activatCodeReq.getExpire();
			String sign =  MD5Util.md5Encode(params + KONGGANG_WCF_SIGN_KEY);//获取空港WCF密钥
			String requstUrl = KONGGANG_REQUST_URL + 
			"API/YinLian/YinLianPD.svc/confirm?" + params + "&sign=" + sign;//空港请求机票详情URL
			log.info("request url is " + requstUrl);
			//GET请求获取空港服务券码
			String resStr = HttpUtil.doGet(requstUrl);
			log.info("response information is " + resStr);
			JSONObject resJson = JSONObject.parseObject(resStr);
			if("00".equals(resJson.getString("code"))){//result为0，则获取data机票详情
				activatCodeRes.setResult(true);
				baseResponse.setData(activatCodeRes);
				baseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
				return baseResponse;			
			}else{
				log.error("request konggang activationCode error is " + resJson.getString("info"));
				activatCodeRes.setResult(false);
				baseResponse.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return baseResponse;		
			}
		}catch(Exception e){
			log.error("request konggang activationCode error is " + e.getMessage());
			activatCodeRes.setResult(false);
			baseResponse.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			baseResponse.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return baseResponse;	
		}	
	}
	
	
	/**
	 * 根据券码调用空港接口禁用券码
	 * @param verifyCode
	 * @return
	 */
	public BaseResponse<TrovkDisableRes> disableCode(TrovkDisableReq trovkDisableReq){
		log.info("begin execut disableCode.......param ： verifyCode  is" + trovkDisableReq.getVerifyCode());
		TrovkDisableRes trovkDisableRes = new TrovkDisableRes();
		BaseResponse<TrovkDisableRes> baseResponse = new BaseResponse<TrovkDisableRes>();
		try {
			String params = "partner=" + KONGGANG_PARTNER + //KONGGANG_PARTNER 获取空港签约合作方身份ID
			"&verify_code=" + trovkDisableReq.getVerifyCode();
			String sign = MD5Util.md5Encode(params + KONGGANG_WCF_SIGN_KEY);//获取空港WCF密钥
			String requstUrl = KONGGANG_REQUST_URL + 
			"API/YinLian/YinLianPD.svc/enableDis?" + params + "&sign=" + sign;//空港请求机票详情URL
			log.info("request url is " + requstUrl);
			//GET请求获取空港服务券码
			String resStr = HttpUtil.doGet(requstUrl);
			log.info("response information is " + resStr);
			JSONObject resJson = JSONObject.parseObject(resStr);
			if("00".equals(resJson.getString("code"))){//result为0，则获取data机票详情
				trovkDisableRes.setResult(true);
				baseResponse.setData(trovkDisableRes);
				baseResponse.setResponseCode(ResponseCodeMsg.SUCCESS.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.SUCCESS.getMsg());
				return baseResponse;
			}else{
				log.error("request konggang disableCode error is " + resJson.getString("info"));
				trovkDisableRes.setResult(false);
				baseResponse.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
				baseResponse.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return baseResponse;
			}
		}catch(Exception e){
			log.error("request konggang disableCode error is " + e.getMessage());
			trovkDisableRes.setResult(false);
			baseResponse.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
			baseResponse.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
			return baseResponse;
		}	
	}
	
//	public static void main(String[] args) {
//		TripBiz tb = new TripBiz();
////		List<AreaResVO> areaResVOs = tb.getAirport("1");
////		List<AirportInfoResVO> AirportInfoResVO = tb.getAirportInfo("50","1");
////		String code = getVerifyCode("123","2010001");//246253326
////		System.out.println(activationCode("246253326","2017-12-19"));
//		System.out.println(disableCode("246253326"));
////		System.out.println(code);
//	}
}
