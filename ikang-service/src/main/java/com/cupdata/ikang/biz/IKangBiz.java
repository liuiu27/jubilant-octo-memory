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

import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.ikang.constant.IKangConstant;
import com.cupdata.ikang.utils.JsonUtil;
import com.cupdata.ikang.vo.CheckDataBackInfo;
import com.cupdata.ikang.vo.CooperationMessageInfo;
import com.cupdata.ikang.vo.FescoOrderDay;
import com.cupdata.ikang.vo.IKangOrderInfo;

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
	public static CooperationMessageInfo getAppointDates(String hospid) throws Exception{
		String requestPath = IKangConstant.getAppointURL().concat("?hospid=".concat(hospid).concat(IKangConstant.getUrlData()));
		String JSONData = doGet(requestPath);
		CooperationMessageInfo messageInfo = new CooperationMessageInfo();
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
		return messageInfo;
	}
	
	/**
	 * 保存远程订单接口
	 * @param order
	 * @param orderInfo
	 * @return
	 * @throws Exception
	 */
	public static CooperationMessageInfo saveIKangOrder(IKangOrderInfo orderInfo) throws Exception{
		String xmlinfo = wrapXMLData(orderInfo);
		String requestPath = IKangConstant.getSaveOrderURL("银行号").concat("?xmlinfo=".concat(URLEncoder.encode(xmlinfo,"UTF-8")).concat(IKangConstant.getUrlData()));
		String JSONData = doGet(requestPath);
		return (CooperationMessageInfo) JsonUtil.getObject4JsonString(JSONData, CooperationMessageInfo.class);
	}
	
	
	/**
	 * 获取到检查询接口
	 * @param wsOrderId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public static CooperationMessageInfo appointCheckData(String wsOrderId) throws Exception{
		String  requestPath = IKangConstant.getCheckDataBackURL().concat("?orderid=").concat(wsOrderId).concat(IKangConstant.getUrlData());
		String JSONData = doGet(requestPath);
		JSONObject data = JSONObject.fromObject(JSONData);
		CooperationMessageInfo messageInfo = new CooperationMessageInfo();
		String code = data.getString("code");
		if("1".equals(code)){
			List<CheckDataBackInfo> list = JsonUtil.getList4Json(CommonUtils.getStringNotNullValue(data.get("list")), CheckDataBackInfo.class);
			String message = data.getString("message");
			messageInfo.setCode(code);
			messageInfo.setCheckDataList(list);
			messageInfo.setMessage(message);
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
		return postXml.toString();
	}
	
	private static String doGet(String requestPath) throws Exception
	{
		Date d1 = new Date(); 
		StringBuilder sb = new StringBuilder();
		HttpURLConnection connection = null;
		BufferedReader br = null;
		URL url;
		log.info("IKang=====AppointURL()::" + requestPath);
		try {
			url = new URL(requestPath); // 把字符串转换为URL请求地址
			connection = (HttpURLConnection) url
					.openConnection();// 打开连接
			connection.connect();// 连接会话
			// 获取输入流
			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {// 循环读取流
				sb.append(line);
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("失败!");
		} finally{
			if(null != br){
				br.close();// 关闭流
			}
			if(null != connection){
				connection.disconnect();// 断开连接
			}
		}
		log.info(" post response=" + sb.toString());
		Date d2 = new Date();
		log.info(" wspost operation waste time " + (d2.getTime() - d1.getTime()) + " ms");
		return sb.toString();
	}
	
}
