package com.cupdata.trvok.utils;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.cupdata.commons.utils.AESUtil;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年1月30日 下午1:40:49
*/
@Slf4j
public class CddUtil {
	/**
	 * AES  UrlEncode 加密 
	 * @param str 加密字符串
	 * @param key 加密秘钥
	 * @return
	 */
	public static String aesUrlEncode(String str,String key) {
		if(!StringUtils.isBlank(str)&&!StringUtils.isBlank(key)) {
			try {
				str = AESUtil.Encrypt(str, key);
				return URLEncoder.encode(str, "UTF8");
			} catch (Exception e) {
				log.error("AESUtil.Encrypt error is" + e.getMessage());
			}
		}
		log.error("params is null!");
		return null;
	}
	
	public static void main(String[] args) {
	}
}
