package com.cupdata.iyooc.utils;

import com.cupdata.iyooc.feign.ConfigFeignClient;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.RSAUtils;

import java.security.PublicKey;

/**
 * 优积付的RSA 公 私钥，加密，解密
 * @author ztang
 *
 */
public class ParkingPayRSA {

	/**
	 * 对传入的json字符串进行 rsa 公钥加密
	 * @param body
	 * @return
	 * @throws Exception 
	 */
	public static String encryptPubKey(String body,ConfigFeignClient configFeignClient) throws Exception{
		PublicKey uppPubKey;
		if (CommonUtils.isWindows()) {
			uppPubKey = RSAUtils.getPublicKeyFromString("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTz9cJ43hxlDlWZOIEiyLah+8fUfaF8NMZWvjiaUAGiuyBvg+LyWR6pgpd/oI5oX/kBsMRy8t41h39ZL01Az1FqSDkfLyAZALF6GAQ8qxnCmYuXKZf3mRO/OxcGVX6taBUoan6QpV/OsPnz30Li4YlP+ontzm6chS7V1WRHFgWYwIDAQAB");
		} else {
			uppPubKey = RSAUtils.getPublicKeyFromString(configFeignClient.getSysConfig("PARKING_PAY_PUBLIC_KEY_STR", SysConfigParaNameEn.HUAJIFEN_BANK_CODE).getParaValue());
		}
		String str=RSAUtils.encrypt(body, uppPubKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
		return str;
	}
	
	/**
	 * 对传入的加密字符串进行 rsa 公钥解密
	 * @param body
	 * @return
	 * @throws Exception 
	 */
	public static String decryptPriKey(String body,ConfigFeignClient configFeignClient) throws Exception{
		PublicKey uppPubKey;
		if (CommonUtils.isWindows()) {
			uppPubKey = RSAUtils.getPublicKeyFromString("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTz9cJ43hxlDlWZOIEiyLah+8fUfaF8NMZWvjiaUAGiuyBvg+LyWR6pgpd/oI5oX/kBsMRy8t41h39ZL01Az1FqSDkfLyAZALF6GAQ8qxnCmYuXKZf3mRO/OxcGVX6taBUoan6QpV/OsPnz30Li4YlP+ontzm6chS7V1WRHFgWYwIDAQAB");
		} else {
			uppPubKey = RSAUtils.getPublicKeyFromString(configFeignClient.getSysConfig("PARKING_PAY_PUBLIC_KEY_STR",SysConfigParaNameEn.HUAJIFEN_BANK_CODE).getParaValue());
		}
		String str = RSAUtils.decrypt(body,uppPubKey ,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
		return str;
	}
	
	public static void main(String[] args) throws Exception {
		String ss = "NWoe03fjJJmBAM2DorLqwEwTWMJEmn41ZugvtdjSHN7A6P6DZqWUCoVPOFG/3c+dV6pU1s6YEuPJ\nX6gTM2MGz6bGXoGOK2lxKl2SRmX8kfGttVDnL4DlxBhh2K6txJcWJroKSIaMEDROjxx7UCAEQDV8\nH10f+EYz2gbgorFauSg=";
		PublicKey uppPubKey = RSAUtils.getPublicKeyFromString("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5zNEnevalt3qK8jJR4+qFKB1OHYEMbkv25ehvkoOr6cv59IS73jUbu0zk73QXuVnS+LCcFBTSWWITxslKrk/GCg8j98+iaSxU/xVSM7k7aqXy+0HGZyhdxWZdkepBPhZKnDsr99fnOiQNWC4yJljdCfS0nhQYq8L+F3eJuEEdLwIDAQAB");
		String str = RSAUtils.decrypt(ss, uppPubKey ,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
		System.out.println(str);
	}
	
}
