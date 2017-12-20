package com.cupdata.trvok.utils;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TripUtil {
	
    /**
     * 判断请求参数是否非法 并解密 返回 jsonObj 与 error
     */
    public static Map<String, Object> isValidDecrypted(String reqJson, String bankCode, String orgNo, String method) {
        log.info(" request method is " + method + "param : reqJson is "
                + reqJson + " bankCode is " + bankCode
                + " , orgNo is " + orgNo);
        if (StringUtils.isBlank(orgNo)) {
            orgNo = bankCode;
        }
        Map<String, Object> map = new HashMap<>(3);

        return map;
    }
    /**
	 * 获取随机数
	 * @return
	 */
	public static String getRandom(){
		return String.valueOf(Math.random());
	}

}
