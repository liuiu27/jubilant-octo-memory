package com.cupdata.ikang.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RuiUtils {

	/**
	 * 判断对象数组是否为空
	 * 
	 * @param objs
	 *            需要判断的对象数组
	 * @return boolean
	 */
	public static boolean isWindows() {
		String osName = System.getProperty("os.name");
		if (osName == null) {
			osName = "";
		} else {
			osName = osName.toUpperCase();
		}
		if (osName.contains("WINDOWS")) {
			return true;
		}
		return false;
	}

	private static String valueSplitOpr = ":::";
	private static String fieldSplitOpr = ";;;";
	private static List<String> includeReturnTypes;

	static {
		includeReturnTypes = new ArrayList<String>();
		includeReturnTypes.add("String");
		includeReturnTypes.add("Integer");
		includeReturnTypes.add("Long");
		includeReturnTypes.add("Date");
		includeReturnTypes.add("Double");
	}


	protected final static Log log = LogFactory.getLog(RuiUtils.class);

	public static String getCupdNo() {
		return "";
	}

	public static Double getComissionPrice() {
		return 0.0;
	}

	public static Double getComissionRate() {
		return 0.0;
	}

	public static Double getTransPrice() {
		return 0.0;
	}

	/**
	 * 判断字符串是否为null 或者 trim后为"";
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTrimEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
	

	/**
	 * 判断字符串是否 不 为null 而且 trim后 不 等于"";
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotTrimEmpty(String str) {
		return !isTrimEmpty(str);
	}
	
	public static String trim(String str){
		if(str == null){
			return "";
		}
		return str.trim();
	}
	
	/**
	 * 字符串截取
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String subString(String str,int start,int end){
		if(isTrimEmpty(str)){
			return str;
		}
		if(str.length() <= start || str.length() < end){
			return str;
		}
		return str.substring(start,end);
	}
	
	
	/**
	 * 判断list是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmptyList(List list){
		if(list == null || list.size() == 0){
			return true;
		}
		return false;
	}

	public static String logTimeUsed(Date d1, Date d2, String string) {
		return "[" + string + "] 使用时间为" + (d2.getTime() - d1.getTime()) + "毫秒";
	}

	/**
	 * 
	 * @param string
	 *            传入的字符
	 * @param filler
	 *            用来填充的数字
	 * @param totalLength
	 *            总的字符长度
	 * @param atEnd
	 *            数字房子头部或者结尾
	 * @return
	 */
	public static String fillString(String string, char filler,
			int totalLength, boolean atEnd) {
		byte[] tempbyte = string.getBytes();
		int currentLength = tempbyte.length;
		int delta = totalLength - currentLength;
		for (int i = 0; i < delta; i++) {
			/**
			 * 将数字放在头部
			 */
			if (atEnd) {
				string += filler;
			} else {
				/**
				 * 将数字放在结尾
				 */
				string = filler + string;
			}
		}
		return string;
	}

	/**
	 * 判断某一字符出现的次数
	 * @param str
	 * @param find
	 * @return
	 */
	public static int findCountChar(String str, char find) {
		int count = 0;
		if (isNotTrimEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == find)
					count++;
			}
		}
		return count;
	}
	// public static boolean createUpdateDetail(Object oldObj,Object
	// newObj,AbsCrudBoImpl bo,String... excludeMethods) throws Exception
	// {
	// //TODO obj掉订单接口更新ORDER
	// TblUpdateRecord record = new TblUpdateRecord();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	// record.setClassName(oldObj.getClass().getName());
	// StringBuffer sb = new StringBuffer();
	// for(Method oldMethod : oldObj.getClass().getMethods())
	// {
	// String oldName = oldMethod.getName();
	//
	// if(oldName.startsWith("get"))
	// {
	// boolean excludeFlag = false;
	// for(String excludeMethod : excludeMethods)
	// {
	// if(oldName.substring(3).toUpperCase().equals(excludeMethod.toUpperCase()))
	// {
	// excludeFlag = true;
	// break;
	// }
	// }
	// if(excludeFlag)
	// {
	// continue;
	// }
	// if("getId".equals(oldName))
	// {
	// Long id = (Long)oldMethod.invoke(newObj, new Object[0]);
	// if(id != null)
	// {
	// record.setDataId(id);
	// }
	// }
	// for(Method newMethod : newObj.getClass().getMethods())
	// {
	// String newName = newMethod.getName();
	// if(newName.startsWith("get") && newName.equals(oldName) &&
	// oldMethod.getReturnType().getSimpleName().equals(newMethod.getReturnType().getSimpleName()))
	// {
	// if(newMethod.getReturnType() != null)
	// {
	// String returnTypeName = newMethod.getReturnType().getSimpleName();
	// Object oldValue = oldMethod.invoke(oldObj, new Object[0]);
	// Object newValue = newMethod.invoke(newObj, new Object[0]);
	// if(includeReturnTypes.contains(returnTypeName))
	// {
	// if (!objectEqual(oldValue, newValue))
	// {
	// String fieldName = newName.substring(3);
	// fieldName = fieldName.substring(0, 1).toLowerCase() +
	// fieldName.substring(1);
	// if(oldValue instanceof Date)
	// {
	// String dateString = sdf.format(oldValue);
	// sb.append(fieldName + valueSplitOpr + dateString + fieldSplitOpr);
	// }
	// else
	// {
	// sb.append(fieldName + valueSplitOpr + oldValue + fieldSplitOpr);
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// if(sb.toString().endsWith(fieldSplitOpr))
	// {
	// String serializaStr = sb.substring(0,sb.lastIndexOf(fieldSplitOpr));
	// record.setSerializaStr(serializaStr);
	// bo.addData(record);
	// return true;
	// }
	// return false;
	// }

	private static boolean objectEqual(Object newObj, Object oldObj) {
		if (newObj == null || "".equals(newObj)) {
			if (oldObj == null || "".equals(oldObj)) {
				return true;
			}
		} else {
			return newObj.equals(oldObj);
		}
		return false;
	}

	public static String getNoNullString(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 
	 * @param sourceObj
	 *            被拷贝的对象
	 * @param targetObj
	 *            拷贝到的对象
	 */
	@SuppressWarnings("unchecked")
	public static void copyPropertites(Object sourceObj, Object targetObj) {
		try {
			Class sourceClass = sourceObj.getClass();
			Class targetClass = targetObj.getClass();
			Method[] sourceMethods = sourceClass.getMethods();
			Method[] targetMethods = targetClass.getMethods();
			for (Method rm : sourceMethods) {
				String rmName = rm.getName();
				if (!includeReturnTypes.contains(rm.getReturnType()
						.getSimpleName())) {
					continue;
				}
				// 被拷贝对象的方法需要是以get开头
				if (rmName.startsWith("get")) {
					for (Method wm : targetMethods) {
						String wmName = wm.getName();
						// 拷贝到的对象的方法需以set开头
						if (!wmName.startsWith("set")
								|| !rmName.substring(3).equals(
										wmName.substring(3))) {
							continue;
						}
						Class[] parameters = wm.getParameterTypes();
						if (parameters == null || parameters.length != 1) {
							continue;
						}
						if (rm.getReturnType() != null
								&& rm.getReturnType().getSimpleName()
										.equals(parameters[0].getSimpleName())) {
							Object value = rm.invoke(sourceObj, new Object[0]);
							wm.invoke(targetObj, new Object[] { value });
						}

					}
				}
			}

		} catch (Exception e) {
			log.error("copyProperties error........");
			e.printStackTrace();
		}
	}

	public static String getExceptionLogStr(Exception exception, int length) {
		if (exception == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer(exception.getMessage() == null ? ""
				: exception.getMessage());
		StackTraceElement[] trace = exception.getStackTrace();
		for (int i = 0, traceLength = trace.length; i < traceLength; i++) {
			if (sb.length() < length) {
				sb.append("\n" + trace[i]);
			}
		}
		String resultStr = sb.toString();
		resultStr = resultStr.length() > length ? resultStr
				.substring(0, length) : resultStr;
		return resultStr;
	}

	/**
	 * 根据自定义短信格式返回要发送的短消息内容
	 * 
	 * @param s
	 * @param convertMap
	 * @return
	 */
	public static String getMessage(String s, Map<String, String> convertMap) {
		String custMessage = s;

		if (custMessage != null) {
			String regEx = "\\$\\{[a-zA-Z]*\\}";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(custMessage);
			String replaceValue;
			while (m.find()) {
				String replaceStr = m.group();
				String key = replaceStr.substring(2, replaceStr.length() - 1);
				replaceValue = convertMap.get(key);
				if (replaceValue == null) {
					replaceValue = "";
				}
				custMessage = custMessage.replace(replaceStr, replaceValue);
			}

		}
		return custMessage;
	}

	public static String getTotalRepayment(String cost, String repay, int num) {
		BigDecimal costAmt = new BigDecimal(cost).divide(new BigDecimal(100));
		BigDecimal repayAmt = new BigDecimal(repay).divide(new BigDecimal(100))
				.multiply(new BigDecimal(num));
		return costAmt.add(repayAmt).toString();
	}

	public static String getMonthRepayment(String cost, String repay, int num) {
		BigDecimal costAmt = new BigDecimal(cost).divide(new BigDecimal(
				num * 100));
		BigDecimal repayAmt = new BigDecimal(repay).divide(new BigDecimal(100));
		return costAmt.add(repayAmt).toString();
	}

	public static String formatDouble(Double d, String format) {
		if (format == null || "".equals(format.trim())) {
			format = "0.00";
		}
		DecimalFormat f = new DecimalFormat(format);
		if (d > 0) {
			d = d + 0.00000000000001d;
		} else if (d < 0) {
			d = d - 0.00000000000001d;
		}
		return f.format(d);
	}

	public static String formatDouble(Double d) {
		return formatDouble(d, null);
	}

	/**
	 * 格式化卡号 1234*****1234
	 * 
	 * @return
	 */
	public static String formatCardNo(String cardNo) {
		try {
			if (isNotTrimEmpty(cardNo) && cardNo.length() > 8) {
				return cardNo.substring(0, 4) + "********"
				+ cardNo.substring(cardNo.length() - 4);
			}else if(isNotTrimEmpty(cardNo) && cardNo.length() > 4){
				return "****"
				+ cardNo.substring(cardNo.length() - 4);
			}
			
		} catch (Exception e) {
			
		}
		return cardNo;
	}

	/**
	 * 隐藏用户名
	 * @author zr
	 * @param username
	 * @return
	 */
	public static String formatUsername(String username) {
		if (isNotTrimEmpty(username)) {
			String hideStr = "";
			for (int i = 0; i < (username.length() % 2 == 0 ? username.length() / 2
					: username.length() / 2 + 1); ++i) {
				hideStr += "*";
			}
			return username.substring(0, username.length() / 2).concat(hideStr);
		}
		return username;
	}
	
	
	/**
	 * 通过证件号获取生日和性别
	 * @param zjNo 15位或18位证件号
	 * @return  
	 * @throws Exception
	 */
	public static String[] getBirthAndGender(String zjNo) throws Exception{
		String[] identityAry = new String[]{"",""};
		if (RuiUtils.isNotTrimEmpty(zjNo) && zjNo.toUpperCase().matches("[0-9]{15}|[0-9]{17}([0-9]|X)")){
			//15位身份证
			if (zjNo.length() == 15){
				String birth = zjNo.substring(6,12);
				String gender = Integer.parseInt(zjNo.substring(14))%2 == 0?"女":"男";
				identityAry[0] = "19" + birth;
				identityAry[1] = gender;
			} else if (zjNo.length() == 18){
			//18位身份证
				String birth = zjNo.substring(6,14);
				String gender = Integer.parseInt(zjNo.substring(16,17))%2==0?"女":"男";
				identityAry[0] = birth;
				identityAry[1] = gender;
			}
		}
		return identityAry;
	}

	/**
	 * 替换字符串特定位置的字符
	 * @author DS_Leng
	 * 2015-4-28 下午3:29:29
	 * @param orgStr 源字符串
	 * @param beginIndex 开始位置
	 * @param endIndex 结束位置
	 * @param maxLength 要显示内容的全长度
	 * @param p 要替换成p字符
	 * @return
	 */
	public static String formatStrPattern(String orgStr, int beginIndex, int endIndex, int maxLength, char p) {
		if (StringUtils.isBlank(orgStr)) {
			return "";
		}
		int len = orgStr.length();
		if (beginIndex > len || maxLength > len || maxLength > len) {
			throw new ArrayIndexOutOfBoundsException(" -> " + maxLength + ",超过字符串长度！");
		}
		char[] chars = orgStr.toCharArray();
		for (int i = beginIndex; i < endIndex; i++) {
			chars[i] = p;
		}
		return new String(chars).substring(0, maxLength);
	}
	
	public static void main(String[] args) {
		// System.out.println(formatDouble(0.12100000000000000000000000009));
		// System.out.println(formatDouble(-0.1210000000000000000009));
		System.out.println(formatUsername("dhzhs001"));
	}
}
