package com.cupdata.lakala.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.constant.SysConfigParaNameEn;
import com.cupdata.commons.utils.*;
import com.cupdata.lakala.fein.CacheFeignClient;
import com.cupdata.lakala.vo.LakalaVoucherReq;
import com.cupdata.lakala.vo.LakalaVoucherRes;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName: LakalaVoucherUtil 
 * @Description: 拉卡拉券码相关业务工具类
 * @author LinYong 
 * @date 2017年11月15日 下午4:35:18 
 *  
 */
public class LakalaVoucherUtil {

	@Autowired
	private static CacheFeignClient cacheFeignClient ;


	public static Logger log = Logger.getLogger(LakalaVoucherUtil.class);
	
	/**
	 * 拉卡拉获取券码
	 * @param mobileNo
	 * @param orderNo
	 * @param voucherItem
	 * @param orderTitle 
	 * @return
	 */
	public static LakalaVoucherRes obtainvValidTicketNo(String mobileNo, String orderNo, String voucherItem, String orderTitle){
		//设置响应信息
		LakalaVoucherRes lakalaVoucherRes = null;
		try {
			log.info("调用lakala拉卡拉发券接口，mobile=" + mobileNo + "，orderNo=" + orderNo + "，voucherItem=" + voucherItem + "，orderTitle" + orderTitle);
			//合作商户号
			String partner_id = null;
			if(CommonUtils.isWindows()){
				partner_id = "unionpay";
			}else{
				//判空处理
				if ("".equals(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_VOUCHER_PARTNER").getData())
						|| null==cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_VOUCHER_PARTNER").getData()){
					//打印日志并设置响应结果:合作商户信息获取失败
					log.error(ResponseCodeMsg.ILLEGAL_PARTNER);
					lakalaVoucherRes.setRes(false);
					return lakalaVoucherRes;
				}
				//从配置信息表中根据银行号和合作方参数来获得指定的合作商户信息
				partner_id = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_VOUCHER_PARTNER").getData().getSysConfig().getParaValue();
			}
			
			Long time = CommonUtils.getLongValue((DateTimeUtil.getCurrentTime().getTime() / 1000 / 600) * 600);
			String sign_generate_time = String.valueOf(time);//时间戳
			String version = "1";//接口版本

			//封装拉卡拉接口请求数据
			LakalaVoucherReq req = new LakalaVoucherReq();
			req.setPartner_id(partner_id);
			req.setVersion(version);
			req.setSign_generate_time(sign_generate_time);
			req.setOrder_id(orderNo);
			req.setPhone(mobileNo);
			req.setItem_id(voucherItem);
			req.setTitle(orderTitle);
			Object[] objs = {req};
			String signStr = CommonUtils.beanToString(objs, "=", "&", "asc", true, null);//待簽名字符串
			String sign = MD5Util.sign(signStr, "", "utf-8");//md5簽名
			req.setSign(sign);//签名
			
			String reqJsonStr = JSONObject.toJSON(req).toString();//请求参数
			log.info("调用lakala拉卡拉发券接口，获取券码的请求参数为" + reqJsonStr);

			//3DES加密key
			String desKey = null;
			if(CommonUtils.isWindows()){
				desKey = "5045783eb5294a48"; 
			}else{
				//判空处理
				if ("".equals(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_GET_VOUCHER_3DES_KEY").getData())
						|| null==cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_GET_VOUCHER_3DES_KEY").getData()){
					//打印日志并设置响应结果：3DES加密key明文数据参数获取失败
					log.error(ResponseCodeMsg.FAIL_TO_GET_3DES);
					lakalaVoucherRes.setRes(false);
					return lakalaVoucherRes;
				}
				//从配置信息表中根据银行号和3DES加密参数来获得指定的密钥数据
				desKey = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_GET_VOUCHER_3DES_KEY").getData().getSysConfig().getParaValue();
			}
			desKey = DESUtil.append(desKey, desKey.length(), 24);

			//3DES加密偏移量
			String desIv = null;
			if(CommonUtils.isWindows()){
				desIv = "661e659d";
			}else{
				//判空处理
				if ("".equals(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_GET_VOUCHER_3DES_IV").getData())
						|| null==cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_GET_VOUCHER_3DES_IV").getData()){
					//打印日志并设置响应结果：3DES加密偏移量数据获取失败
					log.error(ResponseCodeMsg.FAIL_TO_GET_3DES_IV.getCode());
					lakalaVoucherRes.setRes(false);
					return lakalaVoucherRes;
				}
				//从配置信息表中根据银行号和3DES加密偏移量参数来获得指定的密钥数据
				desIv = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_GET_VOUCHER_3DES_IV").getData().getSysConfig().getParaValue();
			}
			desIv = DESUtil.substring(desIv, 0, 8);
			
			String encodeParam = DESUtil.encode(desKey, reqJsonStr, desIv, DESUtil.DESEDE_ALGORITHM, DESUtil.DESEDE_CBC_PKCS5PADDING);//3des加密之后的密文
			
			//拉卡拉发券接口地址
			String lakalaVoucherUrl = null;
			if(CommonUtils.isWindows()){
				lakalaVoucherUrl = "http://vouchers.hicardhome.com/vouchers/getvoucher/p/" + partner_id;
			}else{
				//判空处理
				if ("".equals(cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_REQUEST_URL").getData())
						|| null==cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_REQUEST_URL").getData()){
					//打印日志并设置响应结果：拉卡拉发券接口获取失败
					log.error(ResponseCodeMsg.ILLEGAL_PARTNER);
					lakalaVoucherRes.setRes(false);
					return lakalaVoucherRes;
				}
				//从配置信息表中根据银行号和3DES加密偏移量参数来获得指定的密钥数据
				desIv = cacheFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE,"LAKALA_REQUEST_URL").getData().getSysConfig().getParaValue();
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("param", encodeParam);
			String resStr = HttpUtil.doPost(lakalaVoucherUrl, param, ContentType.MULTIPART_FORM_DATA);
			log.info("调用lakala拉卡拉发券接口，获取券码的响应信息为" + resStr);
			if(StringUtils.isNotBlank(resStr)){
				log.info("打印判断信息："+StringUtils.isNotBlank(resStr));
				lakalaVoucherRes = JSONObject.parseObject(resStr, LakalaVoucherRes.class);
			}
		} catch (Exception e) {
			log.error("调用拉卡拉发券接口，出现异常", e);
		}
		return lakalaVoucherRes;
	}
}
