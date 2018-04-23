package com.cupdata.ikang.biz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cupdata.ikang.constant.IKangConstant;
import com.cupdata.ikang.utils.JsonUtil;
import com.cupdata.ikang.vo.CheckDataBackInfo;
import com.cupdata.ikang.vo.CooperationMessageInfo;
import com.cupdata.ikang.vo.FescoOrderDay;
import com.cupdata.ikang.vo.IKangOrderInfo;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.HttpUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月30日 下午2:35:44
*/
@Slf4j
@Service
public class IKangBiz {
	/**
	 * 获取排期接口
	 * @param hospid
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public static CooperationMessageInfo getAppointDates(String hospid){
		CooperationMessageInfo messageInfo = new CooperationMessageInfo();
		try {
			String requestPath = IKangConstant.getAppointURL().concat("?hospid=".concat(hospid).concat(IKangConstant.getUrlData()));
			String JSONData = HttpUtil.doGet(requestPath);
			JSONObject data = JSONObject.fromObject(JSONData);
			String message = data.getString("message");
			String code = data.getString("code");
			if("1".equals(code)){
				String jsonList = CommonUtils.getStringNotNullValue(data.get("list"));
				JSONArray jsonArray = JSONArray.fromObject(jsonList);
				List<FescoOrderDay> list = new ArrayList<FescoOrderDay>();
				for(int i = 0;i<jsonArray.size();i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					FescoOrderDay orderDay = new FescoOrderDay();
					String strRegDate = jsonObject.getString("strRegdate");
					Long maxNum = jsonObject.getLong("maxNum");
					Long usedNum = jsonObject.getLong("usedNum");
					orderDay.setMaxNum(maxNum);
					orderDay.setStrRegDate(strRegDate);
					orderDay.setUsedNum(usedNum);
					list.add(orderDay);
				}
				messageInfo.setList(list);
			}
			messageInfo.setCode(code);
			messageInfo.setMessage(message);
		} catch (Exception e) {
			log.error("getAppointDates error is ：" + e.getMessage());
		}
		return messageInfo;
	}
	
	
	
	public static void main(String[] args) {
//		IKangBiz.getAppointDates("089");
		
		IKangOrderInfo iKangOrderInfo = new IKangOrderInfo();
		iKangOrderInfo.setBankCode("");
		iKangOrderInfo.setCardnumber("1111");
		iKangOrderInfo.setContacttel("fdsa");
		iKangOrderInfo.setCupdCostPrice(9.9);
		iKangOrderInfo.setHospid("089");
		iKangOrderInfo.setIdnumber("NONEM1034612");
		iKangOrderInfo.setMarried("asd");
		iKangOrderInfo.setName("666");
		iKangOrderInfo.setNote("1");
		iKangOrderInfo.setPackagecode("NONEM1034612");
		iKangOrderInfo.setPurseId("iKangOrderInfo");
		iKangOrderInfo.setRegdate("20180423");
		iKangOrderInfo.setReportaddress("shanghai");
		iKangOrderInfo.setSex("男");
		iKangOrderInfo.setSupplyerCostPrice(9.9);
		iKangOrderInfo.setThirdnum("222");
		
		IKangBiz.saveIKangOrder(iKangOrderInfo);
		
//		IKangBiz.appointCheckData("666");
	}
	
	/**
	 * 保存远程订单接口
	 * @param order
	 * @param orderInfo
	 * @return
	 * @throws Exception
	 */
	public static CooperationMessageInfo saveIKangOrder(IKangOrderInfo orderInfo){
		try {
			String xmlinfo = wrapXMLData(orderInfo);
			String requestPath = IKangConstant.getSaveOrderURL().concat("?xmlinfo=".concat(URLEncoder.encode(xmlinfo,"UTF-8")).concat(IKangConstant.getUrlData()));
			String JSONData = HttpUtil.doGet(requestPath);
//			return (CooperationMessageInfo) JsonUtil.getObject4JsonString(JSONData, CooperationMessageInfo.class);
		}catch (Exception e) {
			log.error("CooperationMessageInfo error is :" + e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * 获取到检查询接口
	 * @param wsOrderId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public static CooperationMessageInfo appointCheckData(String wsOrderId){
		CooperationMessageInfo messageInfo = new CooperationMessageInfo();
		try {
			String  requestPath = IKangConstant.getCheckDataBackURL().concat("?orderid=").concat(wsOrderId).concat(IKangConstant.getUrlData());
			String JSONData = HttpUtil.doGet(requestPath);
			JSONObject data = JSONObject.fromObject(JSONData);
			String code = data.getString("code");
			if("1".equals(code)){
				List<CheckDataBackInfo> list = JsonUtil.getList4Json(CommonUtils.getStringNotNullValue(data.get("list")), CheckDataBackInfo.class);
				String message = data.getString("message");
				messageInfo.setCode(code);
				messageInfo.setCheckDataList(list);
				messageInfo.setMessage(message);
			}
		} catch (Exception e) {
			log.error("CooperationMessageInfo error is :" + e.getMessage());
		}
		return messageInfo;
	}
	
	/*<info>
	<cardnumber><![CDATA[???]]></cardnumber>  爱康卡号
	<regdate><![CDATA[yyyy-mm-dd]]></regdate>  必须是“yyyy-mm-dd”格式
	<packagecode><![CDATA[???]]></packagecode> 套餐CODE
	<hospid><![CDATA[???]]></hospid>           体检中心ID
	<name><![CDATA[???]]></name>             体检人姓名
	<sex><![CDATA[???]]></sex>                 体检人性别 0为女，1为男
	<married><![CDATA[???]]></married>     体检人婚否 0为未婚，1为女婚
	<contacttel><![CDATA[???]]></contacttel>      体检人联系方式
	<idnumber><![CDATA[???]]></idnumber>      体检人证件号码	
    <reportaddress><![CDATA[???]]></reportaddress > 报告递送地址
	<thirdnum><![CDATA[???]]></thirdnum>        外部预约流水号
	</info>
	 */
	public static String wrapXMLData(IKangOrderInfo orderInfo){
		StringBuffer postXml = new StringBuffer("");
		postXml.append("<info>").append("<cardnumber>").append("").append("</cardnumber>")
								.append("<regdate>").append(orderInfo.getRegdate()).append("</regdate>")
								.append("<packagecode>").append(orderInfo.getPackagecode()).append("</packagecode>")
								.append("<hospid>").append(orderInfo.getHospid()).append("</hospid>")
								.append("<name>").append(orderInfo.getName()).append("</name>")
								.append("<sex>").append(orderInfo.getSex()).append("</sex>")
								.append("<married>").append(orderInfo.getMarried()).append("</married>")
								.append("<contacttel>").append(orderInfo.getContacttel()).append("</contacttel>")
								.append("<idnumber>").append(orderInfo.getIdnumber()).append("</idnumber>")
								.append("<reportaddress>").append("").append("</reportaddress>")
								.append("<thirdnum>").append(orderInfo.getThirdnum()).append("</thirdnum>")
								.append("<note>").append("").append("</note>")
								.append("</info>");
		log.info(postXml.toString());
		return postXml.toString();
	}
}
