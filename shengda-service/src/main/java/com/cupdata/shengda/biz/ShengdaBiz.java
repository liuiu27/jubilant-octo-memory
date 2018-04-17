package com.cupdata.shengda.biz;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.shengda.utils.PCThreeDESUtil;
import com.cupdata.shengda.vo.ShengdaDrivingOrderReq;
import com.cupdata.shengda.vo.ShengdaDrivingOrderRes;
import com.cupdata.shengda.vo.ShengdaOrderReq;
import com.cupdata.shengda.vo.ShengdaOrderRes;
import com.cupdata.shengda.vo.ShengdaWashingOrderReq;
import com.cupdata.shengda.vo.ShengdaWashingOrderRes;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月30日 下午2:35:44
*/
@Slf4j
@Service
public class ShengdaBiz {
	
	public ShengdaOrderRes createSupplierOrder(ShengdaOrderReq req) throws Exception {
		try{
			String key = "bc7dfba8080ca5f49309478c64ca695a";
			String key3Des = "C205BC5839533270jUN1d77Y";
			String url = "http://101.231.154.154:8042/ShengDaAutoPlatform";
			
			//拼接完整的创建订单URL地址      //TODO 根据产品编号判断是否为 洗车权益 或者 是 如果为酒后代驾/道路救援权益
			String productNo = "";
			if(req instanceof ShengdaWashingOrderReq){//如果为洗车权益
				url += "/car!receiveOrder";
			}else if(req instanceof ShengdaDrivingOrderReq){//如果为酒后代驾/道路救援权益
				url += "/designatedDriving!receiveBespeak";
			}
			log.info("从系统中，获取盛大MD5签名的key为" + key + "，3des加密的key为" + key3Des + "，URL为" + url);
			
			String reqJsonStr = JSONObject.fromObject(req).toString();
			log.info("调用盛大下单接口的请求参数json为" + reqJsonStr);
			
			String reqSign = DigestUtils.md5Hex((reqJsonStr + key).getBytes("UTF-8")).toUpperCase();//签名
			log.info("调用盛大下单接口的请求参数MD5签名为" + reqSign);
			
			String reqResult = PCThreeDESUtil.encrypt(reqJsonStr+ "|" + reqSign, key3Des);
			log.info("调用盛大下单接口的请求参数3DES加密之后的密文为" + reqResult);
			
			String reqEncryptJsonStr = "encryptJsonStr=" + reqResult;
			log.info("最终调用盛大下单接口的请求数据为" + reqEncryptJsonStr);
			
			//请求盛大下单接口
			String resString = HttpUtil.doPost(url, reqEncryptJsonStr, "application/x-www-form-urlencoded");
			log.info("获取盛大下单接口的响应数据（密文）为" + resString);
			
			if(StringUtils.isBlank(resString)){
				log.info("获取盛大下单接口的响应数据（密文）为空！");
				return null;
			}
			
			String resEncryptJsonStr = (String) JSONObject.fromObject(resString).get("encryptJsonStr");
			String resResult = PCThreeDESUtil.decrypt(resEncryptJsonStr, key3Des);
			log.info("获取盛大下单接口的响应数据（明文）为" + resResult);
			
			String[] strs = resResult.split("\\|");
			String resJsonStr = strs[0];
			String resSign = strs[1];
			String calculatedSign = DigestUtils.md5Hex((resJsonStr + key).getBytes("UTF-8") ).toUpperCase();
			
			ShengdaOrderRes res = null;
			if(req instanceof ShengdaWashingOrderReq){//如果为洗车权益
				res = (ShengdaWashingOrderRes) JSONObject.toBean(JSONObject.fromObject(resJsonStr), ShengdaWashingOrderRes.class);
			}else if(req instanceof ShengdaDrivingOrderReq){//如果为酒后代驾/道路救援权益
				res = (ShengdaDrivingOrderRes) JSONObject.toBean(JSONObject.fromObject(resJsonStr), ShengdaDrivingOrderRes.class);
			}
			
			//如果获取的签名与计算所得的签名不一致
			if(!calculatedSign.equals(resSign)){
				log.info("从盛大下单接口的响应报文中获取的签名为" + resSign + "与计算所得的签名" + calculatedSign + "不一致！");
				res.setResultCode("ERROR");//TODO  系统常量
				res.setResultDesc("签名不一致");
			}
			return res;
		}catch(Exception e){
			e.printStackTrace();
			log.info("调用盛大下单接口出现异常！");
			return null;
		}
	}
	
	public static void main(String[] args) {
		ShengdaBiz shengdaUtils = new ShengdaBiz();

		
		ShengdaWashingOrderReq washingOrderReq = new ShengdaWashingOrderReq("WZYH", "WZYH", "223fdsfsf1232131", "03", "15857128524");
		try {
			//调用盛大创建订单接口
			ShengdaOrderRes res = shengdaUtils.createSupplierOrder(washingOrderReq);
			System.out.println(res.getResultCode() + "   " + res.getOrder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
