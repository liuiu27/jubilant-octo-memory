package com.cupdata.commons.utils;

import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cupdata.commons.constant.TimeConstants;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
* @ClassName: CommonUtils 
* @Description: 公共的基础工具类
* @author LinYong 
* @date 2016年8月10日 下午5:09:00 
*
 */
public class CommonUtils {
	/**
	 * 日志
	 */
	protected static Logger log = Logger.getLogger(CommonUtils.class);
	
	/**
	 * 格式化卡号 
	 * 格式化后的卡号形式为：1234*****1234
	 * @return
	 */
	public static String formatCardNo(String cardNo) {
		if (StringUtils.isNotEmpty(cardNo) && cardNo.length() > 8) {
			return cardNo.substring(0, 4) + " **** **** " + cardNo.substring(cardNo.length() - 4);
		}
		return cardNo;
	}
	
	/**
	 * 获取卡号末四位
	 * @return
	 */
	public static String getCardNoEndFour(String cardNo) {
		if (StringUtils.isNotEmpty(cardNo) && cardNo.length() > 4) {
			return cardNo.substring(cardNo.length() - 4);
		}
		return cardNo;
	}
	
	/**
	 * 格式化手机号 
	 * 格式化后的手机号形式为：158****1234
	 * 
	 * @return
	 */
	public static String formatMobileNo(String mobileNo) {
		if (StringUtils.isNotEmpty(mobileNo) && mobileNo.length() > 7) {
			return mobileNo.substring(0, 3) + " **** " + mobileNo.substring(mobileNo.length() - 4);
		}
		return mobileNo;
	}
	
	/**
	 * 格式化姓名
	 * @return
	 */
	public static String formatName(String name) {
		if (StringUtils.isNotEmpty(name) && name.length() >= 2 ) {
			return "**" + name.substring(name.length() - 1);
		}
		return name;
	}
	
	/**
	 * 获取支付串非空字符
	 * @param object
	 * @return
	 */
	public static String getStringNotNullValue(Object object) {
		if (object == null) {
			return "";
		} else {
			return object.toString().trim();
		}
	}
	
	/**
	 * 读取一个属性文件
	 * @param propName 属性文件名称
	 * @return 返回属性文件
	 */
	public static Properties getProperties(String propName) throws Exception{
		Properties p = null;
		InputStream in = ClassLoader.getSystemResourceAsStream(propName);
		if (in == null) {
			in = CommonUtils.class.getResourceAsStream(propName);
		}
		if (in == null) {
			in = CommonUtils.class.getClassLoader().getResourceAsStream(propName);
		}
		p = getProperties(in);
		return p;
	}
	
	/**
	 * 读取一个属性文件
	 * @param in 文件流
	 * @return 返回属性文件
	 */
	private static Properties getProperties(InputStream in) throws Exception {
		Properties p = new Properties();
		if (in != null) {
			p.load(in);
			in.close();
		}
		log.info("属性文件：" + p);
		return p;
	}
	
	/**
	 * 将javabean转为map类型，然后返回一个map类型的值
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object obj) { 
	    Map<String, Object> params = new HashMap<String, Object>(); 
	    try { 
	      PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
	      PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
	      for (int i = 0; i < descriptors.length; i++) { 
	        String name = descriptors[i].getName(); 
	        if (!StringUtils.equals(name, "class")) { 
	          params.put(name, propertyUtilsBean.getNestedProperty(obj, name)); 
	        } 
	      } 
	    } catch (Exception e) { 
	      e.printStackTrace(); 
	    } 
	    return params; 
	}
	
	/**
	 * 将javabean转为字符串，javabean中的属性仅支持java基本类型
	 * 举例：如果bean的属性包括：(long)id、(String)name、(int)age、(String)sex、(String)remark，属性值分别为1001、lion、26、male（其中remark属性的值为空）
	 * kvSqp为=，sep为|，order为asc，isFilterNull为true，ignoreKey为id
	 * 则最终生成的字符串为age=26|name=lion|sex=male
	 * @param obj bean对象
	 * @param kvSep key（属性名）和value（属性值）之间的分隔符，可以为空字符串或者null
	 * @param sep 分隔符，可以为空，可以为空字符串或者null
	 * @param order 排序标识，支持asc升序以及desc降序，根据key（属性名）字母进行排序，如果为null，则不保证按照一定顺序排列
	 * @param isFilterNull 是否过滤属性值为null的属性，如果为true则进行过滤空值，否则不进行过滤
	 * @param ignoreKeys 忽略的key（属性名）数组，可以为空
	 * @return
	 */
	public static String beanToString(Object[] objs, String kvSep, String sep, String order, boolean isFilterNull, String[] ignoreKeys){
		//如果kvSep或者sep为null，则转换成空字符串
		if(kvSep == null){
			kvSep = "";
		}
		if(sep == null){
			sep = "";
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(objs == null || objs.length == 0){
			return null;
		}
		for(Object obj : objs){
			if(obj != null){
				params.putAll(beanToMap(obj));
			}
		}
		Map<String, Object> beanSortedMap = sortMapByKey(params, order);
		StringBuffer beanString = new StringBuffer();//bean转化成的字符串
		for(Map.Entry<String, Object> entry : beanSortedMap.entrySet()){    
			//判断key是否在忽略的key数组中，如果不在，才能进行字符串拼接
			if(!isStrArrayContainsStr(ignoreKeys, entry.getKey())){
				if(!isNullOrEmptyOfObj(entry.getValue())){//如果value不为null
					beanString.append(entry.getKey() + kvSep + String.valueOf(entry.getValue()) + sep);
				}else if(isNullOrEmptyOfObj(entry.getValue()) && !isFilterNull){//如果value为空以及isFilterNull为true
					beanString.append(entry.getKey() + kvSep + sep);
				}
			}
		} 
		//去除字符串结尾多余的一个sep
		if(beanString.length() > 0){
			beanString.delete(beanString.length() - sep.length(), beanString.length());
		}
		log.info("将bean对象转换成字符串为" + beanString.toString());
		return beanString.toString();
	}
	
	/**
	 * 判断字符串数组strArray中是否包含否一个字符串str
	 * @param strArray 字符串数组
	 * @param str 字符串
	 * @return
	 */
	public static boolean isStrArrayContainsStr(String[] strArray, String str){
		//如果strArray为null或者strArray长度为0或者str为null或者str为""，则返回false
		if(strArray == null || strArray.length <= 0 || StringUtils.isEmpty(str)){
			return false;
		}
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断一个对象是否为空。
	 * 其中，如果obj为String，则判断字符串是否为null或者trim后为"";
	 * @return
	 */
	public static boolean isNullOrEmptyOfObj(Object obj){
		//如果obj为null，则返回true
		if(null == obj){
			return true;
		}
		
		//如果obj为String，则判断是否为空
		if(obj instanceof String){
			return StringUtils.isEmpty((String)obj);
		}else if(obj instanceof Character){
			return StringUtils.isEmpty(String.valueOf(obj));
		}else{
			return false;
		}
	}
	
	/**
	 * 使用 Map按key进行排序
	 * @param map 
	 * @param order 顺序标识，支持升序asc以及降序desc，如果为null，则不进行排序
	 * @return
	 */
	public static Map<String, Object> sortMapByKey(Map<String, Object> map, String order) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		if(StringUtils.isEmpty(order)){
			return map;
		}
		
		Map<String, Object> sortMap = null;
		//如果为升序
		if("asc".equals(order)){
			//treeMap默认为升序
			sortMap = new TreeMap<String, Object>();
		}else if("desc".equals(order)){//降序
			sortMap = new TreeMap<String, Object>(new MapKeyComparator());
		}
		sortMap.putAll(map);
		return sortMap;
	}
	
	/**
	 * 
	* @ClassName: MapKeyComparator 
	* @Description: 降序比较器
	* @author LinYong 
	* @date 2016年8月3日 上午11:43:15 
	*
	 */
	private static class MapKeyComparator implements Comparator<String>{
		@Override
		public int compare(String str1, String str2) {
			return str2.compareTo(str1);
		}
	}
	
	/**
	 * 生成0~9随机数字和A-Za-z的字母组合
	 * @param length [生成随机数的长度]
	 * @return
	 */
	public static String getCharAndNum(int length) {
		StringBuffer val = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val.append((char) (choice + random.nextInt(26)));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val.append(String.valueOf(random.nextInt(10)));
			}
		}
		return val.toString();
	}
	
	/**
	 * 获取随机的数字字符串
	 * @param length 长度
	 * @return
	 */
	public static String getRandomNum(int length) {
		StringBuffer val = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val.append(String.valueOf(random.nextInt(10)));
		}
		return val.toString();
	}
	
	/**
	 * 判断对象数组是否为空
	 *
	 * @param objs 需要判断的对象数组
	 * @return boolean
	 */
	public static boolean isWindows() {
		String osName = System.getProperty("os.name");
        if (osName == null) {
        	osName = "";
        }
        else {
        	osName = osName.toUpperCase();
        }
        if (osName.contains("WINDOWS")) {
        	return true;
        }
        return false;
	}
	
	/**
	 * 数据库中读取的Object类型，如果不为null,转换成Long型并返回
	 * @param object
	 * @return
	 */
	public static Long getLongValue(Object object) {
		if (object == null) {
			return 0l;
		} else {
			if (StringUtils.isEmpty(object.toString().trim())) {
				return 0l;
			}
			String longStr = object.toString();
			if (longStr.contains(".")) {
				return Long.valueOf(longStr.substring(0, longStr.indexOf(".")));
			}
			return Long.valueOf(longStr);
		}
	}
	
	/**
	 * Object类型，如果不为null,转换成Integer型并返回
	 * @param object
	 * @return
	 */
	public static Integer getIntegerValue(Object object) {
		if (object == null) {
			return 0;
		} else {
			if (StringUtils.isEmpty(object.toString().trim())) {
				return 0;
			}
			String longStr = object.toString();
			if (longStr.contains(".")) {
				return Integer.valueOf(longStr.substring(0, longStr.indexOf(".")));
			}
			return Integer.valueOf(longStr);
		}
	}
	
	/**
	 * 将map转换成json格式数据并返回
	 * @param out
	 * @param map
	 */
	public static void printOut(PrintWriter out, Map map) {
		if (out != null) {
			JSONObject obj = new JSONObject();
			obj.putAll(map);
			out.write(obj.toString());
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 将json数据返回
	 * @param out
	 * @param map
	 */
	public static void printOut(PrintWriter out, JSONObject jsonObject) {
		if (out != null) {
			out.write(jsonObject.toString());
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 将object数据转换成json格式数据并返回
	 * @param out
	 * @param map
	 */
	public static void printOut(PrintWriter out, Object object) {
		if (out != null) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			out.write(jsonObject.toString());
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 将字符串数据返回
	 * @param out
	 * @param str
	 */
	public static void printOut(PrintWriter out, String str) {
		log.info("响应数据为" + str);
		if (out != null) {
			out.write(str);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 获取当前服务器配置的环境变量SERVER_FLAG的数据
	 * @return MASTER or SLAVE or other
	 */
	public static String getServerFlag() {
		String serverFlag = null;
		try {
			//参数设置jboss路径为/app/ecomnew/jboss-4.2.2.GA/bin/runNode1.conf或者runNode2.conf或者runNode3.conf或者runNode4.conf
			serverFlag = System.getProperty("SERVER_FLAG");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStringNotNullValue(serverFlag);
	}
	
	/**
	 * 判断是否为手机号
	 * 如果是手机号，则返回true，否则返回false
	 * @param mobileNo
	 * @return
	 */
	public static boolean isMobileNo(String mobileNo){
		if(!StringUtils.isNotEmpty(mobileNo)){
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$");  
		Matcher m = p.matcher(mobileNo);  
		return m.matches(); 
	}
	
	/**
	 * 判断是否为纯数字字符串
	 * @param num
	 * @return
	 */
	public static boolean isAllNumberChar(String num){
		if (StringUtils.isBlank(num)) {
			return false;
		}
		for(int i = 0; i < num.length(); i++){
			if (!Character.isDigit(num.charAt(i))){
			    return false;
			}
		}
		return true;
	}
	
	/**
	 * 格式化浮点型数字
	 * @param d 浮点型
	 * @param format 格式
	 * @return
	 */
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

	/**
	 * 格式化浮点型数字，默认格式为0.00
	 * @param d
	 * @return
	 */
	public static String formatDouble(Double d) {
		return formatDouble(d, null);
	}
	
	/**
	 * 参数分隔符
	 */
	private static final String SEP_SIGN = "&";
	private static final String SEP_EQ = "=";
	public static Map<String, String> parseURLParams(String paramsStr) {
		Map<String, String> paramMap = new HashMap<String, String>();
		String[] paramKey = StringUtils.isNotBlank(paramsStr) ? paramsStr.trim().split(SEP_SIGN) : null;
		if (paramKey != null && paramKey.length > 0) {
			for (String param : paramKey) {
				if (StringUtils.isNotBlank(param)) {
					String[] _param = param.trim().split(SEP_EQ);
					if (_param.length == 2) {
						paramMap.put(_param[0], _param[1]);
						log.info("解析加密请求参数，得到结果为" + _param[0] + "=" + _param[1]);
					}
				}
			}
		}
		return paramMap;
	}
	
	/**
	 * 判断对象数组是否为空
	 * 
	 * @param objs
	 *            需要判断的对象数组
	 * @return boolean
	 */
	public static boolean arrayIsNotEmpty(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断对象数组是否为空
	 * 
	 * @param objs
	 *            需要判断的对象数组
	 * @return boolean
	 */
	public static boolean arrayIsEmpty(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取真实ip
	 * @param request
	 * @return
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 采用散列算法（MD5、SHA-1、SHA-256），对字符串签名
	 * @param strSrc 要签名的字符串
	 * @param encName 签名类型[支持MD5、SHA-1、SHA-256，默认使用SHA-256]
	 * @param charsetName 字符编码类型[支持gbk、utf-8等，默认为utf-8]           
	 * @return
	 */
	public static String encrypt(String strSrc, String encName, String charsetName) throws Exception{
		MessageDigest md = null;
		String strDes = null;
		
		try {
			//如果charsetName为null或者空，则charsetName设置为默认值utf-8
			if(StringUtils.isEmpty(charsetName)){
				charsetName = "utf-8";
			}
			//如果encName为null或者空，则encName设置为默认值SHA-256
			if (StringUtils.isEmpty(encName)) {
				encName = "SHA-256";
			}
			byte[] bt = strSrc.getBytes(charsetName);
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}
	
	/**
	 * 将字节数组转换成十六进制的字符串
	 * @param bts
	 * @return
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	/**
	 * SHA256签名
	 * @param strSrc 需要签名的字符串
	 * @return
	 */
	public static String SHA256(String strSrc)throws Exception{
		return encrypt(strSrc, "SHA-256", "utf-8");
	}
	
	/** 
     * 根据18位身份证号，计算出生日期以及性别
     * @return 
     * @throws Exception 
     */  
    public static Map<String, String> getBirthdayGenderBy18CertNo(String certNo) throws Exception {  
    	if(null == certNo || certNo.length() != 18){
    		return null;
    	}
    	
        Map<String, String> map = new HashMap<String, String>();  
        String birthday = certNo.substring(6, 14);  
        String sex;  
        if (Integer.parseInt(certNo.substring(16).substring(0, 1)) % 2 == 0) {//获取性别
            sex = "F";  //女
        } else {  
            sex = "M";  //男
        }  
        map.put("gender", sex);  
        map.put("birthday", birthday);  
        return map;  
    }  
  
    /**
     * 排序工具，list为集合，properties是变参，根据传入的参数排序
     * @param list
     * @param properties
     */
	public static void comparatorList(List list, String... properties) {
		List<BeanComparator> sortFields = new ArrayList<BeanComparator>();
		for (String property : properties) {
			sortFields.add(new BeanComparator(property));
		}
		ComparatorChain multiSort = new ComparatorChain(sortFields);
		Collections.sort(list, multiSort);
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
	
	public static String getHostAddress() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();
			log.info("ip:" + ip); 
		} catch (UnknownHostException e) {
			log.error("get IP error " + e.getMessage());
		} 
		return ip;
	}
	
	//生成流水号  年月日时分秒加8位随机数
	public static String serialNumber() {
		String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), TimeConstants.DATE_PATTERN_5) + CommonUtils.getCharAndNum(8);
		System.out.println(timestamp);
		return "";
	} 
	
	public static void main(String[] args) throws Exception {
		
		serialNumber();
//		System.out.println(getRandomNum(6));
		
//		Character c = null;
//		boolean isEmpty = isNullOrEmptyOfObj(c);
//		System.out.println(isEmpty);
		
//		String mobiles="18757128524";  
//		Pattern p = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$");  
//		Matcher m = p.matcher(mobiles);  
//		System.out.println(m.matches());  
		
//		String name = "林永";
//		System.out.println(formatName(name));
		
		
//		String str = String.format("%,d%n", 72);
//		System.out.println(str);
		
//		String s = "1234qwer林永";
//		try {
//			System.out.println(SHA256(s));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		String s = "MyM7x4j/P18=";
//		System.out.println(s.length());
		
//		String s = SHA256(UserInfoEncrypt.decode("hWpPpAzBc/8="));
//		System.out.println(s);
		
//		Map<String, String> map = CommonUtils.getBirthdayGenderBy18CertNo("320923199002286356");
//		System.out.println(map.get("gender").charAt(0) + "   " + map.get("birthday"));
		
/*		String name = "林永";
		System.out.println(formatName(name));
		
		Double d = Double.valueOf("1234.11");
		System.out.println(formatDouble(Double.valueOf("0"), "###,##0.00"));*/
		
		
		/*String cardNo = "6227780500026100-111";
		boolean is = isAllNumberChar(cardNo);*/

//		String body = "{\"responseCode\":\"000000\",\"responseMsg\":\"成功\",\"data\":{\"airportList\":[{\"airportId\":\"539\",\"airportName\":\"北海福成机场\",\"airportCode\":\"BHY\"},{\"airportId\":\"50\",\"airportName\":\"长春龙嘉国际机场\",\"airportCode\":\"CGQ\"},{\"airportId\":\"247\",\"airportName\":\"长沙黄花国际机场\",\"airportCode\":\"CSX\"},{\"airportId\":\"356\",\"airportName\":\"常州机场\",\"airportCode\":\"CZX\"},{\"airportId\":\"504\",\"airportName\":\"成都双流国际机场\",\"airportCode\":\"CTU\"},{\"airportId\":\"301\",\"airportName\":\"大理荒草坝机场\",\"airportCode\":\"DLU\"},{\"airportId\":\"54\",\"airportName\":\"大连周水子国际机场\",\"airportCode\":\"DLC\"},{\"airportId\":\"386\",\"airportName\":\"大同云冈机场\",\"airportCode\":\"DAT\"},{\"airportId\":\"311\",\"airportName\":\"德宏芒市机场\",\"airportCode\":\"LUM\"},{\"airportId\":\"68\",\"airportName\":\"鄂尔多斯伊金霍洛机场\",\"airportCode\":\"DSN\"},{\"airportId\":\"43\",\"airportName\":\"福州长乐国际机场\",\"airportCode\":\"FOC\"},{\"airportId\":\"32\",\"airportName\":\"桂林两江国际机场\",\"airportCode\":\"KWL\"},{\"airportId\":\"47\",\"airportName\":\"哈尔滨太平国际机场\",\"airportCode\":\"HRB\"},{\"airportId\":\"256\",\"airportName\":\"海口美兰国际机场\",\"airportCode\":\"HAK\"},{\"airportId\":\"65\",\"airportName\":\"杭州萧山国际机场\",\"airportCode\":\"HGH\"},{\"airportId\":\"49\",\"airportName\":\"合肥新桥国际机场\",\"airportCode\":\"HFE\"},{\"airportId\":\"55\",\"airportName\":\"呼和浩特白塔国际机场\",\"airportCode\":\"HET\"},{\"airportId\":\"450\",\"airportName\":\"黄山机场\",\"airportCode\":\"TXN\"},{\"airportId\":\"244\",\"airportName\":\"济南遥墙国际机场\",\"airportCode\":\"TNA\"},{\"airportId\":\"462\",\"airportName\":\"九寨黄龙机场\",\"airportCode\":\"JZH\"},{\"airportId\":\"64\",\"airportName\":\"昆明长水国际机场\",\"airportCode\":\"KMG\"},{\"airportId\":\"62\",\"airportName\":\"拉萨贡嘎机场\",\"airportCode\":\"LXA\"},{\"airportId\":\"396\",\"airportName\":\"兰州中川机场\",\"airportCode\":\"LHW\"},{\"airportId\":\"303\",\"airportName\":\"丽江三义机场\",\"airportCode\":\"LJG\"},{\"airportId\":\"526\",\"airportName\":\"连云港白塔埠机场\",\"airportCode\":\"LYG\"},{\"airportId\":\"458\",\"airportName\":\"柳州白莲机场\",\"airportCode\":\"LZH\"},{\"airportId\":\"381\",\"airportName\":\"牡丹江海浪机场\",\"airportCode\":\"MDG\"},{\"airportId\":\"51\",\"airportName\":\"南昌昌北国际机场\",\"airportCode\":\"KHN\"},{\"airportId\":\"26\",\"airportName\":\"南京禄口国际机场\",\"airportCode\":\"NKG\"},{\"airportId\":\"44\",\"airportName\":\"南宁吴圩国际机场\",\"airportCode\":\"NNG\"},{\"airportId\":\"66\",\"airportName\":\"宁波栎社国际机场\",\"airportCode\":\"NGB\"},{\"airportId\":\"57\",\"airportName\":\"青岛流亭国际机场\",\"airportCode\":\"TAO\"},{\"airportId\":\"69\",\"airportName\":\"泉州晋江国际机场\",\"airportCode\":\"JJN\"},{\"airportId\":\"53\",\"airportName\":\"沈阳桃仙国际机场\",\"airportCode\":\"SHE\"},{\"airportId\":\"264\",\"airportName\":\"石家庄正定机场\",\"airportCode\":\"SJW\"},{\"airportId\":\"1\",\"airportName\":\"首都机场\",\"airportCode\":\"PEK\"},{\"airportId\":\"58\",\"airportName\":\"太原武宿国际机场\",\"airportCode\":\"TYN\"},{\"airportId\":\"310\",\"airportName\":\"腾冲驼峰机场\",\"airportCode\":\"TCZ\"},{\"airportId\":\"42\",\"airportName\":\"天津滨海国际机场\",\"airportCode\":\"TSN\"},{\"airportId\":\"67\",\"airportName\":\"温州龙湾机场\",\"airportCode\":\"WNZ\"},{\"airportId\":\"392\",\"airportName\":\"乌海机场\",\"airportCode\":\"WUA\"},{\"airportId\":\"63\",\"airportName\":\"乌鲁木齐地窝堡国际机场\",\"airportCode\":\"URC\"},{\"airportId\":\"470\",\"airportName\":\"无锡苏南硕放国际机场\",\"airportCode\":\"WUX\"},{\"airportId\":\"48\",\"airportName\":\"武汉天河国际机场\",\"airportCode\":\"WUH\"},{\"airportId\":\"60\",\"airportName\":\"西安咸阳国际机场\",\"airportCode\":\"XIY\"},{\"airportId\":\"296\",\"airportName\":\"西宁曹家堡机场\",\"airportCode\":\"XNN\"},{\"airportId\":\"302\",\"airportName\":\"西双版纳嘎洒机场\",\"airportCode\":\"JHG\"},{\"airportId\":\"500\",\"airportName\":\"锡林浩特机场\",\"airportCode\":\"XIL\"},{\"airportId\":\"41\",\"airportName\":\"厦门高崎国际机场\",\"airportCode\":\"XMN\"},{\"airportId\":\"52\",\"airportName\":\"徐州观音机场\",\"airportCode\":\"XUZ\"},{\"airportId\":\"269\",\"airportName\":\"宜昌三峡机场\",\"airportCode\":\"YIH\"},{\"airportId\":\"56\",\"airportName\":\"银川河东机场\",\"airportCode\":\"INC\"},{\"airportId\":\"362\",\"airportName\":\"永州零陵机场\",\"airportCode\":\"LLF\"},{\"airportId\":\"46\",\"airportName\":\"郑州新郑国际机场\",\"airportCode\":\"CGO\"},{\"airportId\":\"61\",\"airportName\":\"重庆江北国际机场\",\"airportCode\":\"CKG\"}]}}";
//		String body = "{\"responseCode\":\"000000\",\"responseMsg\":\"SUCCESS\",\"data\":{\"orderNo\":\"31738173981\", \"orgOrderNo\":\"DHKJDHJ11313QQEQE\",\"productNo\":\"0281881\",\"mobileNo\":\"15857128524\",\"airportList\":[{\"airportId\":\"539\",\"airportName\":\"北海福成机场\",\"airportCode\":\"BHY\"},{\"airportId\":\"50\",\"airportName\":\"长春龙嘉国际机场\",\"airportCode\":\"CGQ\"}]}}";
//		com.alibaba.fastjson.JSONObject jsonObj = com.alibaba.fastjson.JSONObject.parseObject(body);
//		com.alibaba.fastjson.JSONObject resultJsonObj = new com.alibaba.fastjson.JSONObject();
//		resultJsonObj.put("responseCode", jsonObj.get("responseCode"));
//		resultJsonObj.put("responseMsg", jsonObj.get("responseMsg"));
//
//		String data = String.valueOf(jsonObj.get("data"));
//		com.alibaba.fastjson.JSONObject dataJsonObj = com.alibaba.fastjson.JSONObject.parseObject(data);
//		Set<Map.Entry<String, Object>> dataSet = dataJsonObj.entrySet();
//		Set<String> keySet = dataJsonObj.keySet();
//		Iterator<String> it = keySet.iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			resultJsonObj.put(key, dataJsonObj.get(key));
//		}
//
//		System.out.print(resultJsonObj.toJSONString());
//		getHostAddress();
	}
}
