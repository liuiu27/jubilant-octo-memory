package com.cupdata.tencent.utils;

import com.cupdata.sip.common.api.tencent.request.QQCheckOpenReq;
import com.cupdata.sip.common.api.tencent.request.QQOpenReq;
import com.cupdata.sip.common.api.tencent.response.QQCheckOpenRes;
import com.cupdata.sip.common.api.tencent.response.QQOpenRes;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.HttpUtil;
import com.cupdata.sip.common.lang.utils.MD5Util;
import com.cupdata.tencent.feign.ConfigFeignClient;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;

/**
 * 
 * @ClassName: QQRechargeUtils
 * @Description: QQ会员充值工具类
 * @author LinYong
 * @date 2017年6月29日 上午10:21:22
 */
public class QQRechargeUtils {

	protected static Logger log = Logger.getLogger(QQRechargeUtils.class);
	
	/**
	 * QQ会员开通鉴权
	 */
	public static QQCheckOpenRes qqCheckOpen(QQCheckOpenReq req, ConfigFeignClient configFeignClient){
	    log.info("QQ会员鉴权,Uin:"+req.getUin()+",Serviceid:"+req.getServiceid()+",Servernum:"+req.getServernum());
		QQCheckOpenRes qqCheckOpenRes = new QQCheckOpenRes();
		//step1.开通鉴权url获取
		String checkOpenUrl = null;
		if(CommonUtils.isWindows()){
			//如果获取数据信息为空
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_CHECK_OPEN_URL"))){
				qqCheckOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqCheckOpenRes;
			}
			checkOpenUrl = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_CHECK_OPEN_URL").getParaValue();
		}else{
			//如果获取数据信息为空
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_CHECK_OPEN_URL"))){
				qqCheckOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqCheckOpenRes;
			}
			checkOpenUrl = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_CHECK_OPEN_URL").getParaValue();
		}
		log.info("QQ会员开通鉴权url:"+checkOpenUrl);

		//step2.会员充值key获取
		String key = null;
		if(CommonUtils.isWindows()){
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_KEY"))){
				qqCheckOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqCheckOpenRes;
			}
			key = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_KEY").getParaValue();
		}else{
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_KEY"))){
				qqCheckOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqCheckOpenRes;
			}
			key = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_KEY").getParaValue();
		}
		log.info("QQ会员开通key:"+key);

		//step3.QQ会员充值source
		if(StringUtils.isBlank(req.getSource())){
			if(CommonUtils.isWindows()){
				if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_SOURCE"))){
					qqCheckOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
					return qqCheckOpenRes;
				}
				req.setSource(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_SOURCE").getParaValue());
			}else{
				if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_SOURCE"))){
					qqCheckOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
					return qqCheckOpenRes;
				}
				req.setSource(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_SOURCE").getParaValue());
			}
		}
		log.info("QQ会员充值source:"+req.getSource());

		//step4.生成签名
		String sign = MD5Util.sign(req.getServernum() + req.getServiceid() + req.getUin() + req.getAmount() + req.getSource() + req.getPaytype(), key, "utf-8").toUpperCase();
		req.setSign(sign);
		Object[] obj = {req};
		String reqStr = CommonUtils.beanToString(obj, "=", "&", null, true, null); 
		checkOpenUrl = checkOpenUrl + "?" + reqStr;

		//step5.调用腾讯充值接口
		QQCheckOpenRes res = null;
		try {
			long l1 = System.currentTimeMillis();
			String resStr = HttpUtil.doGet(checkOpenUrl);
			long l2 = System.currentTimeMillis();
			log.info("腾讯鉴权结果:"+resStr);
			log.info("调用腾讯鉴权接口耗时:"+(l2 - l1)+"ms");
			res = parseQQCheckOpenXml(resStr);
		} catch (Exception e) {
			log.error("", e);
		}
		return res;
	}
	
	/**
	 * QQ会员开通
	 */
	public static QQOpenRes qqOpen(QQOpenReq req, ConfigFeignClient configFeignClient){
        log.info("QQ会员开通,Uin"+req.getUin()+",Serviceid:"+req.getServiceid()+",Servernum"+req.getServernum()+",Txparam:"+req.getTxparam());
		QQOpenRes qqOpenRes = new QQOpenRes();

		//step1.QQ充值接口
		String openUrl = null;
		if(CommonUtils.isWindows()){
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"QQ_OPEN_URL").getParaValue())){
				qqOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqOpenRes;
			}
			openUrl = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_URL").getParaValue();
		}else{
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"QQ_OPEN_URL").getParaValue())){
				qqOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqOpenRes;
			}
			openUrl = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_URL").getParaValue();
		}

		//step2.会员充值key
		String key = null;
		if(CommonUtils.isWindows()){
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"QQ_OPEN_KEY"))){
				qqOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqOpenRes;
			}
			key = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_KEY").getParaValue();
		}else{
			if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"QQ_OPEN_KEY"))){
				qqOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
				return qqOpenRes;
			}
			key = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_KEY").getParaValue();
		}

        //step3.会员充值source
		if(StringUtils.isBlank(req.getSource())){
			if(CommonUtils.isWindows()){
				if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"QQ_OPEN_SOURCE"))){
					qqOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
					return qqOpenRes;
				}
				req.setSource(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_SOURCE").getParaValue());
			}else{
				if (CommonUtils.isNullOrEmptyOfObj(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"QQ_OPEN_SOURCE"))){
					qqOpenRes.setResult(ResponseCodeMsg.FAILED_TO_GET.getMsg());
					return qqOpenRes;
				}
				req.setSource(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "QQ_OPEN_SOURCE").getParaValue());
			}
		}

		//step4.生成签名
		String sign = MD5Util.sign(req.getServernum() + req.getServiceid() + req.getUin() + req.getAmount() + req.getTxparam() + req.getPrice() + req.getCommand() + req.getTimestamp() + req.getSource() + req.getPaytype(), key, "utf-8").toUpperCase();
		req.setSign(sign);
		
		//step5.转换请求参数
		StringBuilder reqParams = new StringBuilder();
		reqParams.append("<data>");
		reqParams.append("<servernum>").append(req.getServernum()).append("</servernum>");
		reqParams.append("<serviceid>").append(req.getServiceid()).append("</serviceid>");
		reqParams.append("<uin>").append(req.getUin()).append("</uin>");
		reqParams.append("<amount>").append(req.getAmount()).append("</amount>");
		reqParams.append("<txparam>").append(req.getTxparam()).append("</txparam>");
		reqParams.append("<price>").append(req.getPrice()).append("</price>");
		reqParams.append("<command>").append(req.getCommand()).append("</command>");
		reqParams.append("<source>").append(req.getSource()).append("</source>");
		reqParams.append("<paytype>").append(req.getPaytype()).append("</paytype>");
		reqParams.append("<timestamp>").append(req.getTimestamp()).append("</timestamp>");
		reqParams.append("<sign>").append(req.getSign()).append("</sign>");
		reqParams.append("</data>");

		//step6.开始请求接口
		QQOpenRes res = null;
		try {
			long l1 = System.currentTimeMillis();
			String resStr = HttpUtil.doPost(openUrl, reqParams.toString());
			long l2 = System.currentTimeMillis();
			log.info("XML格式的QQ会员开通响应数据为" + resStr+",耗时为:"+(l2 - l1));
			res = parseQQOpenXml(resStr);
		} catch (Exception e) {
			log.error("解析XML格式的QQ会员开通响应数据出现异常", e);
		}
		return res;
	}
	
	/**
	 * 解析XML格式的QQ鉴权响应数据
	 * @param xmlStr
	 * @return
	 */
	private static QQCheckOpenRes parseQQCheckOpenXml(String xmlStr){
        log.info("解析xml格式的鉴权");
		QQCheckOpenRes checkOpenRes = new QQCheckOpenRes();
		try {
            Document document = DocumentHelper.parseText(xmlStr);
            Element root = document.getRootElement();
		     if (CommonUtils.isWindows()){
                 checkOpenRes.setResult("0");
            }else {
                 checkOpenRes.setResult(root.element("result").getText());
                 checkOpenRes.setDesc(root.element("desc").getText());
             }
            if("0".equals(checkOpenRes.getResult())){
                checkOpenRes.setTxparam(root.element("txparam").getText());
            }
			return checkOpenRes;
		} catch (DocumentException e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析xml格式的QQ开通响应数据
	 * @param xmlStr
	 * @return
	 */
	private static QQOpenRes parseQQOpenXml(String xmlStr){
        log.info("响应鉴权数据");
		QQOpenRes openRes = new QQOpenRes();
		try {
			Document document = DocumentHelper.parseText(xmlStr);
			Element root = document.getRootElement();
			openRes.setResult(root.element("result").getText());
			openRes.setDesc(root.element("desc").getText());
			return openRes;
		} catch (DocumentException e) {
			log.error("", e);
			return null;
		}
	}
	
	/*public static void main(String[] args) {
		QQCheckOpenReq checkOpenReq = new QQCheckOpenReq();
		String servernum = "15857128524";
		String serviceid = "01";
		String uin = "707316407";
		String amount = "1";
		String source = "10017";
		String paytype = "1";
		checkOpenReq.setServernum(servernum);
		checkOpenReq.setServiceid(serviceid);
		checkOpenReq.setUin(uin);
		checkOpenReq.setAmount(amount);
		checkOpenReq.setSource(source);
		checkOpenReq.setPaytype(paytype);
		System.out.print("会员");
		QQCheckOpenRes checkOpenRes = QQRechargeUtils.qqCheckOpen(checkOpenReq);
		log.info("开通结果:"+checkOpenRes.getResult());
		if("0".equals(checkOpenRes.getResult())){
			QQOpenReq openReq = new QQOpenReq();
			openReq.setServernum(servernum);
			openReq.setServiceid(serviceid);
			openReq.setUin(uin);
			openReq.setAmount(amount);
			openReq.setSource(source);
			openReq.setPaytype(paytype);
			openReq.setTxparam(checkOpenRes.getTxparam());
			openReq.setPrice("1000");
			openReq.setCommand("1");
			openReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmss"));
			QQOpenRes openRes = QQRechargeUtils.qqOpen(openReq);
			System.out.println("开通状态" + openRes.getResult());
		}
		
	}*/
}
